/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

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

public class Resultat {

	private ListeElectorale le ;
	private int nbVoix ;
	
	// nombre de siege total
	private int nbSiege ;
	// nombre de siège obtenus à la proportionnelle
	private int nbSiegeProportionnel ;
	
	private double reste;
	private double pourcentage;
	
	public Resultat(ListeElectorale l) {
		le = l ;
		nbSiege = 0 ;
	}
	
	public Resultat(Resultat r) {
		le = r.le ;
		nbVoix = r.nbVoix ;
		nbSiege = r.nbSiege ;
		nbSiegeProportionnel = r.nbSiegeProportionnel ;
		reste = r.reste ;
		pourcentage = r.pourcentage ;
	}

	public void modify(Resultat r) {
		le = r.le ;
		nbVoix = r.nbVoix ;
		nbSiege = r.nbSiege ;
		nbSiegeProportionnel = r.nbSiegeProportionnel ;
		reste = r.reste ;
		pourcentage = r.pourcentage ;
	}
	
	public int getNbVoix() {
		return nbVoix;
	}

	public void setNbVoix(int nbVoix) {
		this.nbVoix = nbVoix;
	}

	public int getNbSiege() {
		return nbSiege;
	}

	public void addSiege(int s) {
		nbSiege = nbSiege + s;
	}

	public void resetSiege() {
		nbSiege = 0;
		nbSiegeProportionnel = 0;
	}

	public double getReste() {
		return reste;
	}

	public void setReste(double reste) {
		this.reste = reste;
	}

	public ListeElectorale getListeElectorale() {
		return le;
	}
	
	public void setSiege(int s) {
		nbSiege = s;
	}

	public double getPourcentage() {
		return pourcentage;
	}

	public void setPourcentage(double pourcentage) {
		this.pourcentage = pourcentage;
	}

	public int getNbSiegeProportionnel() {
		return nbSiegeProportionnel;
	}

	public void addSiegeProportionnel(int s) {
		nbSiegeProportionnel = nbSiegeProportionnel + s;
		nbSiege = nbSiege + s;
	}

}
