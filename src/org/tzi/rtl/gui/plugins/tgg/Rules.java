package org.tzi.rtl.gui.plugins.tgg;



import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.tzi.rtl.tgg.manager.RTLRuleTree;
import org.tzi.rtl.tgg.mm.TggRuleCollection;
import org.tzi.use.gui.main.MainWindow;

public class Rules {
	private static TggRuleCollection fTggRules = new TggRuleCollection();
    private static MainWindow fMainWindow = null;
    private static JFrame fRuleFrame = null;
    
    public static TggRuleCollection getTggRuleCollection() { return fTggRules; }
    
    public static void setRTLRuleFileName (String fileName) {
        if (fileName.length() > 0)
            fTggRules.setHTMLFile(fileName);
    }
    
    public static void setRTLRule (TggRuleCollection rules) {
        fTggRules = rules;
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				fRuleFrame = RTLRuleTree.createAndShowGUI(fTggRules, fMainWindow);
			}
		});
    }

	public static void setMainWindow(MainWindow fParent) {
		fMainWindow = fParent;
	}
	
	public static void showRules (MainWindow fParent) {
		if (fTggRules.getTggRules().size() == 0)
			JOptionPane.showMessageDialog(fParent, "No rules available.");
		else {
			if (fRuleFrame == null)
				fRuleFrame = RTLRuleTree.createAndShowGUI(fTggRules, fParent);
			else {
				fRuleFrame.setVisible(true);
			}
		}
	}
	
	public static void closeRuleWindow () {
		if (fRuleFrame != null)
			fRuleFrame.dispatchEvent(new WindowEvent(fRuleFrame, WindowEvent.WINDOW_CLOSING));
	}
	
}
