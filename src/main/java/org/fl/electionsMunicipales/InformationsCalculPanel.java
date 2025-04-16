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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class InformationsCalculPanel {

	private JPanel calculInfos ;
	private JTextArea lblCalculInfos ;
	private Election election;
	private ArrayList<ListeElectoraleUI> listesUi ;
	private CalculControl calculControl;
	private Logger eLog;
	private JTextField jExpr;
	private StringBuilder messageCalcul ;
	private final static String newLine = System.getProperty("line.separator") ;
	private boolean calculMaxSiege ;
	private final static String MessageStandard = "<html><p>Remplissez:<ul><li>les nombres de voix (qui ont priorité sur les pourcentages)</li><li>ou les pourcentages et le total des voix exprimées</li></ul></p></html>" ;
	private final static String MessageMaxStandard = "<html><p>Remplissez:<ul><li>le total des voix exprimées</li><li>le pourcentage de la liste 1</li><li>les pourcentages minimum des listes 2 et 3</li></ul></p></html>" ;

	
	public InformationsCalculPanel(Election e, boolean cms, Logger l) {

		election = e ;
		eLog = l ;
		calculMaxSiege = cms ;
		calculInfos = new JPanel() ;
		calculInfos.setLayout(new BoxLayout(calculInfos, BoxLayout.Y_AXIS));
		calculInfos.setBackground(Color.WHITE) ;
		calculInfos.setPreferredSize(new Dimension(900, 800)) ;
		calculInfos.setBorder(BorderFactory.createMatteBorder(10,10,10,10,Color.WHITE)) ;
		calculInfos.setAlignmentX(Component.LEFT_ALIGNMENT) ;
		
		Font font = new Font("Verdana", Font.BOLD, 14);
		
		// Consigne
		JPanel consignePane  = new JPanel() ;
		String msg ;
		if (calculMaxSiege) {
			msg = MessageMaxStandard;
		} else {
			msg = MessageStandard;
		}
		JLabel consigne = new JLabel(msg) ;
		consigne.setFont(font) ;
		consignePane.setBackground(Color.WHITE) ;
		consigne.setAlignmentX(Component.LEFT_ALIGNMENT) ;
		consignePane.add(consigne) ;
		calculInfos.add(consignePane) ;
		
		//---------------------
		// Tableau des listes
		JPanel listes = new JPanel() ;
		listes.setLayout(new GridLayout(election.getResultats().size()+1, 4, 10, 10));
		listes.setAlignmentX(Component.CENTER_ALIGNMENT) ;
		
		
		// Colonne des noms
		JLabel j1 = new JLabel("Listes") ;
		j1.setFont(font) ;
		j1.setPreferredSize(new Dimension(300, 50)) ;
		
		// Colonne des nombre des voix
		JLabel j2 = new JLabel("Nombre de voix") ;
		j2.setFont(font) ;
		j2.setPreferredSize(new Dimension(300, 50)) ;
		
		// Colonne des pourcentage
		JPanel jPourcent = new JPanel() ;
		jPourcent.setLayout(new BoxLayout(jPourcent, BoxLayout.Y_AXIS));
		
		JPanel jExprime = new JPanel() ;
		jExprime.setLayout(new BoxLayout(jExprime, BoxLayout.X_AXIS));
		JLabel j5 = new JLabel("Voix exprimées") ;
		j5.setFont(font) ;
		jExprime.add(j5) ;
		jExpr = new JTextField(10) ;
		jExpr.setPreferredSize(new Dimension(300, 30)) ;
		jExpr.setFont(font) ;
		jExprime.add(jExpr) ;
		jPourcent.add(jExprime) ;
		JLabel j3 = new JLabel("Pourcentages") ;
		j3.setFont(font) ;
		j3.setPreferredSize(new Dimension(300, 50)) ;
		jPourcent.add(j3) ;
		
		// Colonne des si�ges
		JLabel j4 = new JLabel("Sièges") ;
		j4.setFont(font) ;
		j4.setAlignmentX(Component.CENTER_ALIGNMENT) ;
		j4.setPreferredSize(new Dimension(300, 50)) ;
		
		listes.add(j1) ;
		listes.add(j2) ;
		listes.add(jPourcent) ;
		listes.add(j4) ;
		listesUi = new ArrayList<ListeElectoraleUI>();
		for (Resultat r : election.getResultats()) {
			ListeElectoraleUI lUi = new ListeElectoraleUI(r, listes) ;
			listesUi.add(lUi) ;
		}
		calculInfos.add(listes) ;
		//-------------------------------------
		
		// Zone du message d'information
		font = new Font("Verdana", Font.BOLD, 14);
		lblCalculInfos = new JTextArea("");
		lblCalculInfos.setFont(font) ;
		lblCalculInfos.setAlignmentX(Component.RIGHT_ALIGNMENT) ;
		lblCalculInfos.setBackground(Color.WHITE) ;
		lblCalculInfos.setForeground(Color.RED) ;
		lblCalculInfos.setEditable(false) ;
		
		JScrollPane scrollPanel = new JScrollPane (lblCalculInfos);
	    scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    scrollPanel.setPreferredSize(new Dimension(800,400)) ;
		scrollPanel.setBackground(Color.WHITE) ;
		calculInfos.add(scrollPanel) ;
		
		// Bouton de lancement du calcul
		calculControl= new CalculControl() ;
		calculInfos.add(calculControl.getProcCtrl()) ;
	}

	public JPanel getCalculInfos() {
		return calculInfos;
	}

	public void setLblCalculInfos(String info) {
		lblCalculInfos.setText(info);
	}
	
	public void displayResultat() {
		jExpr.setText(String.valueOf(election.calculExprimes())) ;
		for (int i=0; i < election.getResultats().size(); i++) {
			listesUi.get(i).displayResultat() ;
		}
		lblCalculInfos.append(election.getInfosCalcul()) ;
	}

	public CalculControl getCalculControl() {
		return calculControl;
	}
	
	public boolean setNbvoix() {
		boolean res = true ;
		String m ;
		for (int i=0; i < election.getResultats().size(); i++) {
			try {
				int x = Integer.parseInt(listesUi.get(i).getNbVoix()) ;
				eLog.info("Le nombre de voix de la liste " + i + " est: " + x) ;
				election.getResultats().get(i).setNbVoix(x) ;
			} catch (NumberFormatException e) {
				m = "Le nombre de voix de la liste " + election.getResultats().get(i).getListeElectorale().getNom() + " n'est pas un nombre " ;
				eLog.warning("Le nombre de voix de la liste " + i + " n'est pas un entier: " + e) ;
				messageCalcul.append(m).append(newLine) ;
				res = false ;
			}
		}
		return res ;
	}
	
	public boolean setPourcentages() {
		boolean res = true ;
		String m ;
		
		try {
			int y = Integer.parseInt(jExpr.getText()) ;
			election.setExprimes(y) ;
		} catch (NumberFormatException e) {
			m = "Le nombre de voix exprimées n'est pas un entier " ;
			messageCalcul.append(m).append(newLine) ;
			eLog.warning(m + e) ;
			res = false ;
		}
		double sommePourcent = 0 ;
		for (int i=0; i < election.getResultats().size(); i++) {
			try {
				double x = Double.parseDouble(listesUi.get(i).getPourcentage()) ;
				eLog.info("Le pourcentage de la liste " + i + " est: " + x) ;
				election.getResultats().get(i).setPourcentage(x) ;
				sommePourcent = sommePourcent + x ;
			} catch (NumberFormatException e) {
				m = "Le pourcentage de la liste " + election.getResultats().get(i).getListeElectorale().getNom() + " n'est pas un nombre " ;
				eLog.warning(m + e) ;
				messageCalcul.append(m).append(newLine) ;
				res = false ;
			}
		}
		if ((sommePourcent != 100.0d) && !isCalculMaxSiege()) {
			m = "La somme des pourcentages n'est pas égale à 100" ;
			eLog.warning(m) ;
			messageCalcul.append(m).append(newLine) ;
			res = false ;
		}
		return res ;
	}

	public void appendMessageInfos(String s) {
		if (! lblCalculInfos.getText().isEmpty()) {
			lblCalculInfos.append(newLine) ;
		}
		lblCalculInfos.append(s) ;	
	}
	
	public String getMessageCalcul() {
		return messageCalcul.toString();
	}

	public void resetMessageCalcul() {
		messageCalcul = new StringBuilder("");
		setLblCalculInfos(getMessageCalcul()) ;
	}

	public boolean isCalculMaxSiege() {
		return calculMaxSiege;
	}
}
