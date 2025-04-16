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

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ListeElectoraleUI {

	
	private Resultat resultatListe ;
	private JLabel labelListe ;
	private JTextField score ;
	private JTextField pourcent ;
	private JLabel nbSiege ;
	
	public ListeElectoraleUI(Resultat r, JPanel panelListe) {
		super() ;
		resultatListe = r ;
		
		Font font = new Font("Verdana", Font.BOLD, 14);
		labelListe = new JLabel(resultatListe.getListeElectorale().getNom()) ;
		labelListe.setFont(font) ;
		labelListe.setPreferredSize(new Dimension(300, 30)) ;
		panelListe.add(labelListe) ;
		
		score = new JTextField(10) ;
		score.setPreferredSize(new Dimension(300, 30)) ;
		score.setFont(font) ;
		panelListe.add(score) ;
		
		pourcent = new JTextField(10) ;
		pourcent.setPreferredSize(new Dimension(300, 0)) ;
		pourcent.setFont(font) ;
		panelListe.add(pourcent) ;
		
		nbSiege = new JLabel("?") ;
		font = new Font("Verdana", Font.BOLD, 24);
		nbSiege.setFont(font) ;
		nbSiege.setPreferredSize(new Dimension(300, 30)) ;
		panelListe.add(nbSiege) ;
	}

	public String getNbVoix() {
		return score.getText() ;
	}
	
	public String getPourcentage() {
		return pourcent.getText() ;
	}
	
	public void displayResultat() {
		score.setText(String.valueOf(resultatListe.getNbVoix())) ;
		pourcent.setText(String.valueOf(resultatListe.getPourcentage())) ;
		nbSiege.setText(String.valueOf(resultatListe.getNbSiege())) ;
	}
	
}
