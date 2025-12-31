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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartCalcul  implements ActionListener {

	private final CalculControl calculControl;
	private final Election election;
	private final InformationsCalculPanel calculInfos;

	public StartCalcul(Election e, InformationsCalculPanel icp) {
		election = e;
		calculInfos = icp;
		calculControl = icp.getCalculControl();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		calculControl.setTriggered(true);

		// Lire les chiffres rentr�s
		calculInfos.resetMessageCalcul();
		if (calculInfos.isCalculMaxSiege()) {
			if (calculInfos.setPourcentages()) {
				election.setPourcentagesIn();
			} else {
				election.setInvalidInput();
				calculInfos.setLblCalculInfos(calculInfos.getMessageCalcul());
			}
		} else {
			if (calculInfos.setNbvoix()) {
				election.setVoixIn();
			} else if (calculInfos.setPourcentages()) {
				election.setPourcentagesIn();
			} else {
				election.setInvalidInput();
				calculInfos.setLblCalculInfos(calculInfos.getMessageCalcul());
			}
		}

		CalculSieges cs = new CalculSieges(election, calculInfos);
		cs.execute();
	}
}
