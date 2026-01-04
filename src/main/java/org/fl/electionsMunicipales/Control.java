/*
 * MIT License

Copyright (c) 2017, 2026 Frederic Lefevre

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
import java.util.List;
import java.util.logging.Logger;

import org.fl.util.AdvancedProperties;
import org.fl.util.RunningContext;

public class Control {

	private static final Logger electionLog = Logger.getLogger(Control.class.getName());
			
	private static Control controlInstance;
	
	private int inscrits;
	private int totalSieges;
	private int siegesRepartis;
	private int seuil;
	private List<ListeElectorale> listesElectorales;
	
	private static Control getInstance() {
		if (controlInstance == null) {
			controlInstance = new Control(ElectionsUI.getRunningContext());
		}
		return controlInstance;
	}
	
	private Control() {
	}

	private Control(RunningContext  runningContext) {
		
		AdvancedProperties electionsProperties = runningContext.getProps();
		
		try {
			inscrits = Integer.parseInt(electionsProperties.getProperty("election.nbInscrits"));
		} catch (Exception e) {
			electionLog.warning("Exception reading property election.nbInscrits: " + e);
			inscrits = 5000;
		}

		try {
			totalSieges = Integer.parseInt(electionsProperties.getProperty("election.nbSieges"));
		} catch (Exception e) {
			electionLog.warning("Exception reading property election.nbSieges: " + e);
			totalSieges = 29;
		}

		try {
			seuil = Integer.parseInt(electionsProperties.getProperty("election.seuil"));
		} catch (Exception e) {
			electionLog.warning("Exception reading property election.seuil: " + e);
			seuil = 20;
		}

		String elm = electionsProperties.getProperty("election.modeCalcul.conseilMunicipal");
		if (elm.equals("true")) {
			siegesRepartis = totalSieges / 2;
		} else {
			siegesRepartis = totalSieges;
		}
		
		listesElectorales = new ArrayList<>();
		
		String propListeName = "election.nomListe";
		int numListe = 1;
		String nomListe;
		do {
			nomListe = electionsProperties.getProperty(propListeName + numListe);
			if (nomListe != null) {
				electionLog.info("Liste trouvée : " + nomListe);
				listesElectorales.add(new ListeElectorale(nomListe));
			}
			numListe++;
		} while  (nomListe != null);
		
	}
	
	public static int getInscrits() {
		return getInstance().inscrits;
	}
	
	public static int getITotalSieges() {
		return getInstance().totalSieges;
	}
	
	public static int getSeuil() {
		return getInstance().seuil;
	}
	
	public static int getSiegesRepartis() {
		return getInstance().siegesRepartis;
	}
	
	public static List<ListeElectorale> getListesElectorales() {
		return getInstance().listesElectorales;
	}
}
