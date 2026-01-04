/*
 * MIT License
 *
 * Copyright (c) 2017, 2026 Frédéric Lefèvre
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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.fl.util.swing.ApplicationTabbedPane;

public class ElectionsUI extends JFrame {

	private static final long serialVersionUID = 5558620192220743683L;
	
	private static final String DEFAULT_PROP_FILE = "election.properties";
	
	private static final Logger logger = Logger.getLogger(ElectionsUI.class.getName());
	
	private ElectionsUI() {

		setBounds(50, 50, 1700, 900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Calculateur de sièges");

		ApplicationTabbedPane electionTabs = new ApplicationTabbedPane(Control.getRunningContext());

		try {
			Election election = new Election(Control.getProps());

			JPanel siegePanel = new JPanel();
			siegePanel.setLayout(new BoxLayout(siegePanel, BoxLayout.Y_AXIS));
			InformationsCalculPanel calcul = new InformationsCalculPanel(election, false);
			siegePanel.add(calcul.getCalculInfos());

			JPanel maxSiegePanel = new JPanel();
			maxSiegePanel.setLayout(new BoxLayout(maxSiegePanel, BoxLayout.Y_AXIS));
			InformationsCalculPanel calculMax = new InformationsCalculPanel(election, true);
			maxSiegePanel.add(calculMax.getCalculInfos());

			JTabbedPane operationTab = new JTabbedPane();
			Font font = new Font("Verdana", Font.BOLD, 14);
			operationTab.setFont(font);
			operationTab.addTab("Calcul de sièges en fonction des voix", siegePanel);
			operationTab.addTab("Calcul maximum de sièges en fonction d'un pourcentage", maxSiegePanel);

			StartCalcul sc = new StartCalcul(election, calcul);
			calcul.getCalculControl().getBoutonCalcul().addActionListener(sc);
			StartCalcul scm = new StartCalcul(election, calculMax);
			calculMax.getCalculControl().getBoutonCalcul().addActionListener(scm);

			electionTabs.add(operationTab, "Calcul de sièges", 0);
			electionTabs.setSelectedIndex(0);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception during application startup", e);
		}

		getContentPane().add(electionTabs);		
	}

	public static String getPropertyFile() {
		return DEFAULT_PROP_FILE;
	}
	
	public static void main(String[] args) {
		
		Control.init(DEFAULT_PROP_FILE);
		
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
