package actions;

import java.awt.event.ActionEvent;

import gui.*;

import javax.swing.AbstractAction;

/**
 * Close the active document.
 */
public class ShowAbout extends AbstractAction {
	private static final long serialVersionUID = 5083860489087944835L;

	@Override
	public void actionPerformed(ActionEvent arg0) {
		AboutFrame.showMe();
	}
}