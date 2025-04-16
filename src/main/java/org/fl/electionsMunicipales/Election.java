/*
 * MIT License

Copyright (c) 2017, 2025 Frederic Lefevre

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package org.fl.electionsMunicipales;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.logging.Logger;

public class Election {

	private ArrayList<Resultat> resultats ;
	
	private int inscrits;
	private int totalSieges;
	private int siegesRepartis;
	private int seuil ;
	private Logger electionLog ;
	private int donneesEntre ;
	private int exprimes ;
	private StringBuilder infosCalcul ;
	private final static int VOIX_IN = 0; 
	private final static int POURCENTAGES_IN = 1; 
	private final static int INVALID_INPUT = 2;
	private final static String newLine = System.getProperty("line.separator");
	
	public Election(Properties props, Logger el) {
		
		electionLog = el;
		try {
			inscrits = Integer.parseInt(props.getProperty("election.nbInscrits"));
		} catch (Exception e) {
			electionLog.warning("Exception reading property election.nbInscrits: " + e);
			inscrits = 5000;
		}

		try {
			totalSieges = Integer.parseInt(props.getProperty("election.nbSieges"));
		} catch (Exception e) {
			electionLog.warning("Exception reading property election.nbSieges: " + e);
			totalSieges = 29;
		}

		try {
			seuil = Integer.parseInt(props.getProperty("election.seuil"));
		} catch (Exception e) {
			electionLog.warning("Exception reading property election.seuil: " + e);
			seuil = 20;
		}

		String elm = props.getProperty("election.modeCalcul.conseilMunicipal");
		if (elm.equals("true")) {
			siegesRepartis = totalSieges / 2;
		} else {
			siegesRepartis = totalSieges;
		}

		// Listes électorales
		resultats = new ArrayList<Resultat>();

		boolean listeRemain = true;
		String propListeName = "election.nomListe";
		String nomListe;
		int numListe = 1;
		
		while (listeRemain) {
			nomListe = props.getProperty(propListeName + numListe) ;
			if (nomListe == null) {
				listeRemain = false ;
			} else {
				electionLog.info("Liste trouvée : " + nomListe) ;
				resultats.add(new Resultat(new ListeElectorale(nomListe))) ;
				numListe = numListe + 1 ;
			}
		}
	}

	public ArrayList<Resultat> getResultats() {
		return resultats;
	}

	public void addResultat(Resultat r) {
		resultats.add(r);
	}

	public int getInscrits() {
		return inscrits;
	}

	public void setInscrits(int inscrits) {
		this.inscrits = inscrits;
	}

	public int calculExprimes() {
		int x = 0;
		for (Resultat r : resultats) {
			x = x + r.getNbVoix();
		}
		return x;
	}

	public void calculSiege() {
		
		infosCalcul = new StringBuilder() ;
		
		// Remettre le nombre de siège à zéro
		for (Resultat r : resultats) {
			r.resetSiege() ;
		}
		
		if (donneesEntre == POURCENTAGES_IN) {
			infosCalcul.append("Le calcul est réalisé à partir des pourcentages et du nombre de voix exprimées").append(newLine) ;
			for (Resultat r : resultats) {
				r.setNbVoix((int)Math.round(r.getPourcentage()*exprimes/100)) ;
			}
		} else {
			infosCalcul.append("Le calcul est réalisé à partir du nombre de voix de chaque liste").append(newLine) ;
			exprimes = calculExprimes();
			for (Resultat r : resultats) {
				r.setPourcentage((double)(r.getNbVoix()*100)/(double)exprimes) ;
			}
		}

		// seuil des 5%
		int seuilVoix = exprimes/seuil ;
		Resultat gagnant = getGagnant() ;
		
		// le gagnant obtient la moiti� des si�ges (+1)
		gagnant.addSiege(totalSieges - siegesRepartis) ;
		
		// Calcul des voix des listes ayant fait plus de 5%
		// et determination des listes ayant droit � des si�ges
		ArrayList<Resultat> resultatsAvecSieges = new ArrayList<Resultat>() ;
		int nbVoixCinq = 0 ;
		for (Resultat r : resultats) {
			if (r.getNbVoix() >= seuilVoix) {
				nbVoixCinq = nbVoixCinq + r.getNbVoix() ;
				resultatsAvecSieges.add(r) ;
			} else {
				infosCalcul.append("La liste " + r.getListeElectorale().getNom() + " est en dessous du seuil pour obtenir un siège").append(newLine) ;
			}
		}
		
		// calcul du quotient electoral
		double quotientElectoral = (double)nbVoixCinq/(double)siegesRepartis ;
		electionLog.fine("nbVoixCinq="+nbVoixCinq) ;
		electionLog.fine("siegesRepartis="+siegesRepartis) ;
		infosCalcul.append("Le Quotient electoral est " + quotientElectoral).append(newLine) ;
		
		// Attribution des sièges, premier calcul et calcul du reste
		int nbPremierSiege = attributionPremierSieges(quotientElectoral, resultatsAvecSieges, true) ;
		
		// tri des listes suivant la plus forte moyenne
		ComparateurDeReste compReste = new ComparateurDeReste() ;
		
		// attribution des sieges restants
		int nbSiegeRestant = siegesRepartis - nbPremierSiege;
		infosCalcul.append("Pour le second calcul, le nombre de siège restant à répartir est " + nbSiegeRestant).append(newLine) ;
		while (nbSiegeRestant > 0) {
			attributionUnSiegeRestant(resultatsAvecSieges, compReste, true) ;
			nbSiegeRestant = nbSiegeRestant - 1 ;
		}
		electionLog.info(infosCalcul.toString()) ;
	}
	
	private int attributionPremierSieges(double qe, ArrayList<Resultat> resultats, boolean display) {
		
		// Attribution des sièges, premier calcul et calcul du reste
		int nbPremierSiege = 0 ;
		for (Resultat r : resultats) {
			double sd = r.getNbVoix()/qe ;
			int s = (int)Math.floor(sd) ;
			nbPremierSiege = nbPremierSiege + s;
			r.setReste((double)r.getNbVoix()/(s + 1)) ;
			r.addSiegeProportionnel(s) ;
			if (display) {
				infosCalcul.append("Au premier calcul, la liste " + r.getListeElectorale().getNom() + " obtient " + r.getNbSiege() + " sièges, avec un reste de " + r.getReste()).append(newLine) ;
			}
		}
		return nbPremierSiege;
	}
	
	private void attributionUnSiegeRestant(ArrayList<Resultat> resultats, ComparateurDeReste cr, boolean display) {
		
		// attribution d'un siege a la liste ayant la plus forte moyenne
		Collections.sort(resultats, cr) ;
		resultats.get(0).addSiegeProportionnel(1) ;
		if (display) {
			infosCalcul.append("Au second calcul, la liste " + resultats.get(0).getListeElectorale().getNom() + " obtient 1 siège").append(newLine) ;
		}
		
		// calcul des moyenne
		for (Resultat r : resultats) {
			r.setReste((double)r.getNbVoix()/((double)r.getNbSiegeProportionnel() + 1)) ;
			if (display) {
				infosCalcul.append("Au second calcul, la liste " + r.getListeElectorale().getNom() + " a " + r.getNbSiege() + " sièges, avec un reste de " + r.getReste()).append(newLine) ;
			}
		}
	}

	public Resultat getGagnant() {
		
		Resultat res = resultats.get(0) ;
		for (Resultat r : resultats) {
			if (r.getNbVoix() > res.getNbVoix()) {
				res = r ;
			}
		}
		return res ;
	}
	
	public void setPourcentagesIn() {
		donneesEntre = POURCENTAGES_IN ;
	}
	
	public void setVoixIn() {
		donneesEntre = VOIX_IN ;
	}

	public void setInvalidInput() {
		donneesEntre = INVALID_INPUT ;
	}
	
	public boolean validInput() {
		return donneesEntre != INVALID_INPUT ;
	}
	
	public void setExprimes(int exprimes) {
		this.exprimes = exprimes;
	}

	public String getInfosCalcul() {
		
		return infosCalcul.toString() ;
	}
	
	public void maxSiege() {
		
		infosCalcul = new StringBuilder() ;
		
		// Affecter les voix  à partir des pourcentages
		for (Resultat r : resultats) {
			r.setNbVoix((int)Math.round(r.getPourcentage()*exprimes/100)) ;
		}
		
		 ArrayList<Resultat> resultatsTmp = new ArrayList<Resultat>() ;

		for (int i=0; i < resultats.size(); i++) {
			resultatsTmp.add(new Resultat(resultats.get(i))) ;
		}
		
		// seuil des 5%
		int seuilVoix = exprimes/seuil ;
		
		// somme des voix des listes 2 et 3
		int s23 = exprimes - resultatsTmp.get(0).getNbVoix();
		for (int i=3; i < resultatsTmp.size(); i++) {
			s23 = s23 - resultatsTmp.get(i).getNbVoix() ;
		}
		int minVoix1 = resultatsTmp.get(1).getNbVoix() ;
		int minVoix2 = resultatsTmp.get(2).getNbVoix() ;
		double minPourcent1 = resultatsTmp.get(1).getPourcentage() ;
		double minPourcent2 = resultatsTmp.get(2).getPourcentage() ;
		
		// nb siege max
		int maxSiege = 0 ;		
		
		for (int i=minVoix1; i <= s23-minVoix2; i++) {
			// Remettre le nombre de siège à zéro
			for (Resultat r : resultatsTmp) {
				r.resetSiege() ;
			}
				
			resultatsTmp.get(1).setNbVoix(i) ;
			resultatsTmp.get(2).setNbVoix(s23-i) ;
			
			Resultat res = resultatsTmp.get(0) ;
			for (Resultat r : resultatsTmp) {
				if (r.getNbVoix() > res.getNbVoix()) {
					res = r ;
				}
			}
			Resultat gagnant = res ;	
				
			// le gagnant obtient la moitié des sièges (+1)
			gagnant.addSiege(totalSieges - siegesRepartis) ;
				
			// Calcul des voix des listes ayant fait plus de 5%
			// et determination des listes ayant droit à des sièges
			ArrayList<Resultat> resultatsAvecSieges = new ArrayList<Resultat>() ;
			int nbVoixCinq = 0 ;
			for (Resultat r : resultatsTmp) {
				if (r.getNbVoix() >= seuilVoix) {
					nbVoixCinq = nbVoixCinq + r.getNbVoix() ;
					resultatsAvecSieges.add(r) ;		
				}
			}
				
			// calcul du quotient electoral
			int quotientElectoral = nbVoixCinq/siegesRepartis ;
				
			// Attribution des sièges, premier calcul et calcul du reste
			int nbPremierSiege = attributionPremierSieges(quotientElectoral, resultatsAvecSieges, true) ;
			
			// tri des listes suivant la plus forte moyenne
			ComparateurDeReste compReste = new ComparateurDeReste() ;
			
			// attribution des sièges restants
			int nbSiegeRestant = siegesRepartis - nbPremierSiege;
			while (nbSiegeRestant > 0) {
				attributionUnSiegeRestant(resultatsAvecSieges, compReste, true) ;
				nbSiegeRestant = nbSiegeRestant - 1 ;
			}	
			
			if (maxSiege < resultatsTmp.get(0).getNbSiege()) {
				maxSiege = resultatsTmp.get(0).getNbSiege() ;
				for (int j=0; j < resultatsTmp.size(); j++) {					
					resultats.get(j).modify(resultatsTmp.get(j)) ;
				}
			}
		}
		// recalcul des pourcentage avec la solution trouvée
		for (Resultat r : resultats) {
			r.setPourcentage((float)(r.getNbVoix()*100)/exprimes) ;
		}
		infosCalcul.append( "Maximum de siège pour la liste " + resultats.get(0).getListeElectorale().getNom() + " avec le pourcentage " +  resultats.get(0).getPourcentage() + "% =" + maxSiege) ;
		infosCalcul.append(newLine) ;
		infosCalcul.append("sachant que la liste " + resultats.get(1).getListeElectorale().getNom() + " a un pourcentage minimum de " +  minPourcent1) ;
		infosCalcul.append(newLine) ;
		infosCalcul.append("et la liste " + resultats.get(2).getListeElectorale().getNom() + " a un pourcentage minimum de " +  minPourcent2) ;
		
		electionLog.info(infosCalcul.toString()) ;
		
	}
}
