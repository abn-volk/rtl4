package org.tzi.rtl.gui.plugins.tgg;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.tzi.gui.ChangeListenerDialog;
import org.tzi.use.gui.main.ViewFrame;
import org.tzi.use.runtime.gui.IPluginAction;
import org.tzi.use.runtime.gui.IPluginActionDelegate;

public class ActionIncrementalUpdate implements IPluginActionDelegate {
	
	@Override
	public void performAction(IPluginAction pluginAction) {
		ChangeListenerDialog dialog = new ChangeListenerDialog(pluginAction.getParent(), pluginAction.getSession());
		URL url = getClass().getResource("/resources/delta.png");
		ViewFrame vf = new ViewFrame("Model incremental update", null, "");
		vf.setFrameIcon(new ImageIcon(url));
		vf.addInternalFrameListener(new InternalFrameListener() {
			@Override
			public void internalFrameActivated(InternalFrameEvent arg0) {}
			@Override
			public void internalFrameClosed(InternalFrameEvent arg0) {
				dialog.unsubscribe();
			}
			@Override
			public void internalFrameClosing(InternalFrameEvent arg0) {}
			@Override
			public void internalFrameDeactivated(InternalFrameEvent arg0) {}
			@Override
			public void internalFrameDeiconified(InternalFrameEvent arg0) {}
			@Override
			public void internalFrameIconified(InternalFrameEvent arg0) {}
			@Override
			public void internalFrameOpened(InternalFrameEvent arg0) {}
		});
		vf.setContentPane(dialog);
		pluginAction.getParent().addNewViewFrame(vf);
	}
}
