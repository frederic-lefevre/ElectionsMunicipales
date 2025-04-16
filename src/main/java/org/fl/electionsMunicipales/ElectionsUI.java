/*
 * MIT License
 *
 * Copyright (c) 2022 Frédéric Lefèvre
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package org.fl.electionsMunicipales;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class ElectionsUI  extends JFrame {

	private static final long serialVersionUID = 5558620192220743683L;
	
	private static final String DEFAULT_PROP_FILE = "election.properties";
	
	private InformationsCalculPanel calcul, calculMax ;
	
	public ElectionsUI() {
		
		Control ctrl = new Control(DEFAULT_PROP_FILE) ;
		
		if (ctrl.init()) {
			Election e = new Election(ctrl.getProps(), ctrl.getElectionLog()) ;
			
			setBounds(50, 50, 1000, 700);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setTitle("Calculateur de sièges") ;
			
			JPanel siegePanel = new JPanel() ;
			siegePanel.setLayout(new BoxLayout(siegePanel, BoxLayout.Y_AXIS));		
			calcul = new InformationsCalculPanel(e, false, ctrl.getElectionLog()) ;
			siegePanel.add(calcul.getCalculInfos()) ;
			
			JPanel maxSiegePanel = new JPanel() ;
			maxSiegePanel.setLayout(new BoxLayout(maxSiegePanel, BoxLayout.Y_AXIS));
			calculMax = new InformationsCalculPanel(e, true, ctrl.getElectionLog()) ;
			maxSiegePanel.add(calculMax.getCalculInfos()) ;
			
			JTabbedPane operationTab = new JTabbedPane() ;
			Font font = new Font("Verdana", Font.BOLD, 14);
			operationTab.setFont(font) ;
			operationTab.addTab("Calcul sièges", siegePanel) ;
			operationTab.addTab("Calcul maximum de sièges en fonction d'un pourcentage", maxSiegePanel) ;
				
			getContentPane().add(operationTab) ;
			
			StartCalcul sc = new StartCalcul(e, calcul, ctrl.getElectionLog()) ;
			calcul.getCalculControl().getBoutonCalcul().addActionListener(sc) ;
			StartCalcul scm = new StartCalcul(e, calculMax, ctrl.getElectionLog()) ;
			calculMax.getCalculControl().getBoutonCalcul().addActionListener(scm) ;
			
		} else {
			System.out.println("Initialisation failed") ;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ElectionsUI window = new ElectionsUI();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
