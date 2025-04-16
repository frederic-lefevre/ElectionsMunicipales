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

import java.util.logging.Logger;

import javax.swing.SwingWorker;


public class CalculSieges   extends SwingWorker<String,InformationsCalcul> {

	private InformationsCalculPanel calculInfos;
	private Election election;
	private Logger eLog;

	public CalculSieges(Election e, InformationsCalculPanel icp, Logger l) {

		election = e;
		eLog = l;
		calculInfos = icp;
	}

	@Override
	public String doInBackground() {

		eLog.info("Début du calcul");

		// Calculer
		if (election.validInput()) {
			if (calculInfos.isCalculMaxSiege()) {
				election.maxSiege();
			} else {
				election.calculSiege();
			}
		} else {
			eLog.info("Entrée invalide");
		}

		eLog.info("Fin du calcul");
		return "";
	}

	@Override
	public void done() {

		// afficher
		if (election.validInput()) {
			calculInfos.displayResultat();
		} else {
			eLog.info("Pas de résultat; entrée invalide");
		}
		eLog.info("Fin affichage");

	}

}
