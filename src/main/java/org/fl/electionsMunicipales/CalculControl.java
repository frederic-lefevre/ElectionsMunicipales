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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class CalculControl {

	private JButton boutonCalcul;
	private JPanel procCtrl;

	private boolean triggered;
	private InformationsCalculPanel infosCalcul;

	public CalculControl() {

		String buttonText = "<html><p>Calcul des sièges</p></html>";

		triggered = false;

		procCtrl = new JPanel();
		procCtrl.setLayout(new BoxLayout(procCtrl, BoxLayout.X_AXIS));
		procCtrl.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, Color.BLACK));

		boutonCalcul = new JButton(buttonText);
		Font font = new Font("Verdana", Font.BOLD, 18);
		boutonCalcul.setFont(font);
		boutonCalcul.setBackground(Color.GREEN);
		boutonCalcul.setPreferredSize(new Dimension(450, 100));

		procCtrl.add(boutonCalcul);

	}

	public JPanel getProcCtrl() {
		return procCtrl;
	}

	public JButton getBoutonCalcul() {
		return boutonCalcul;
	}

	public InformationsCalculPanel getInfosCalculPanel() {
		return infosCalcul;
	}

	public boolean isTriggered() {
		return triggered;
	}

	public boolean isActive() {
		return boutonCalcul.isEnabled();
	}

	public void activate() {
		triggered = false;
		boutonCalcul.setBackground(new Color(27, 224, 211));
		boutonCalcul.setEnabled(true);
	}

	public void deactivate() {
		boutonCalcul.setEnabled(false);
	}

	public void setTriggered(boolean triggered) {
		this.triggered = triggered;
	}

}
