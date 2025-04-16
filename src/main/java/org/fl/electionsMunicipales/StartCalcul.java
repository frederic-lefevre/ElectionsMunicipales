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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

public class StartCalcul  implements ActionListener {

	private CalculControl calculControl ;
	private Election election;
	private Logger eLog ;
	private InformationsCalculPanel calculInfos ;
	
	public StartCalcul(Election e,  InformationsCalculPanel icp, Logger l) {
		election = e ;
		calculInfos = icp ;
		calculControl = icp.getCalculControl() ;
		eLog = l ;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		eLog.info("Calcul demandé") ;
		calculControl.setTriggered(true) ;
		
		// Lire les chiffres rentr�s
		calculInfos.resetMessageCalcul() ;
		if (calculInfos.isCalculMaxSiege()) {
			if (calculInfos.setPourcentages()) {
				election.setPourcentagesIn() ;
			} else {
				election.setInvalidInput() ;
				calculInfos.setLblCalculInfos(calculInfos.getMessageCalcul()) ;
			}
		} else {
			if (calculInfos.setNbvoix()) {
				election.setVoixIn() ;
			} else if (calculInfos.setPourcentages()) {
				election.setPourcentagesIn() ;
			} else {
				election.setInvalidInput() ;
				calculInfos.setLblCalculInfos(calculInfos.getMessageCalcul()) ;
			}
		}
		
		CalculSieges cs = new CalculSieges(election, calculInfos, eLog) ;
		cs.execute() ;
	}
}
