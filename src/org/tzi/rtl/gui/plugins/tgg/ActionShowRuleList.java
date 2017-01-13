package org.tzi.rtl.gui.plugins.tgg;

import org.tzi.use.runtime.gui.IPluginAction;
import org.tzi.use.runtime.gui.IPluginActionDelegate;

public class ActionShowRuleList implements IPluginActionDelegate{

	@Override
	public void performAction(IPluginAction pluginAction) {
		Rules.showRules(pluginAction.getParent());
	}

}
