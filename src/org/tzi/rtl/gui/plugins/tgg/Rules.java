package org.tzi.rtl.gui.plugins.tgg;



import java.beans.PropertyVetoException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.tzi.rtl.tgg.manager.RTLRuleTree;
import org.tzi.rtl.tgg.mm.TggRuleCollection;
import org.tzi.use.gui.main.MainWindow;
import org.tzi.use.gui.main.ViewFrame;

public class Rules {
	private static TggRuleCollection fTggRules = new TggRuleCollection();
    private static MainWindow fMainWindow = null;
    private static ViewFrame fViewFrame = null;
    
    public static TggRuleCollection getTggRuleCollection() { return fTggRules; }
    
    public static void setRTLRuleFileName (String fileName) {
        if (fileName.length() > 0)
            fTggRules.setHTMLFile(fileName);
    }
    
    public static void setRTLRule (TggRuleCollection rules) {
        fTggRules = rules;
    }

	public static void setMainWindow(MainWindow fParent) {
		fMainWindow = fParent;
	}
	
	public static void showRules(MainWindow fParent) {
		if (fTggRules.getTggRules().size() == 0)
			JOptionPane.showMessageDialog(fParent, "No rules available.");
		else {
			if (fViewFrame == null) {
				URL url = Rules.class.getResource("/resources/rtl.png");
				fViewFrame = new ViewFrame("Transformation rules", null, "");
				fViewFrame.setFrameIcon(new ImageIcon(url));
				fViewFrame.setContentPane(new RTLRuleTree(fTggRules, fMainWindow));
				fViewFrame.pack();
				fParent.addNewViewFrame(fViewFrame);
			}
			else 
				fViewFrame.show();
		}
	}
	
	public static void closeRuleWindow () {
		if (fViewFrame != null)
			try {
				fViewFrame.setClosed(true);
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
	}
	
}
