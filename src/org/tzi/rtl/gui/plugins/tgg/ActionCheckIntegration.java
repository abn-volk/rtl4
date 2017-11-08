package org.tzi.rtl.gui.plugins.tgg;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.tzi.rtl.tgg.mm.MTggRule;
import org.tzi.rtl.tgg.mm.TggRuleCollection;
import org.tzi.rtl.tgg.parser.RTLKeyword;
import org.tzi.rtl.trafo.matching.Matching;
import org.tzi.rtl.trafo.matching.MatchingEachPart;
import org.tzi.use.gui.main.MainWindow;
import org.tzi.use.main.Session;
import org.tzi.use.runtime.gui.IPluginAction;
import org.tzi.use.runtime.gui.IPluginActionDelegate;
import org.tzi.use.uml.mm.MClass;
import org.tzi.use.uml.mm.MOperation;
import org.tzi.use.uml.sys.MObject;
import org.tzi.use.uml.sys.MSystemException;
import org.tzi.use.uml.sys.MSystemState;


/**
 * RTL1.1
 * @author Khoa-Hai Nguyen
 *
 */
public class ActionCheckIntegration implements IPluginActionDelegate {
	TggRuleCollection fTggRuleCollection;
	boolean result = true;
	Set <MObject> input; // all input object
	Session fSession;
	PrintWriter fLogWriter;
	public ActionCheckIntegration() {

	}

    public ActionCheckIntegration(Session fSession, PrintWriter fLogWriter) {
        this.fSession = fSession;
        this.fLogWriter = fLogWriter;
    }

    public void performAction() {
        if (fSession != null && fLogWriter != null) {
            fLogWriter.println("++++++++++++++++++++");
            fLogWriter.println("Check integration ...");
            initParams();
            Matching first = new MatchingEachPart();
            first.setfSystemState(fSession.system().state());
            long start = System.currentTimeMillis();
            // begin test case
            fSession.system().beginVariation();
            fSession.system().setRunningTestSuite(true);
            result = execute(first);
            // end test case
            try {
                fSession.system().endVariation();
            } catch (MSystemException ex) {
                ex.printStackTrace();
            }
            float elapsedTimeSec = (System.currentTimeMillis()-start)/1000F;
            fLogWriter.println("Time: " + elapsedTimeSec);
            System.err.println("Time: " + elapsedTimeSec);
            if (result){
                System.err.println("Test case passed");
                fLogWriter.println("Test case passed");
            }
            else{
                fLogWriter.println("Test case failed");
                System.err.println("Test case failed");
            }
            // release static object
            MatchingEachPart.listMatch.clear();
            MatchingEachPart.getMatchHasRun().clear();
            fLogWriter.println("Done.");
        }
    }

	public void performAction(IPluginAction pluginAction) {
		fSession = pluginAction.getSession();
		MainWindow fMainWindow = pluginAction.getParent();
		fLogWriter = fMainWindow.logWriter();

		performAction();
	}


	public boolean execute(Matching curMatch){
    	System.err.println(curMatch.getObjectList4LHS());
    	List<Matching> matches = new ArrayList<Matching>();
    	// find all match with current state
    	if (!result){
    		for (MTggRule rule : fTggRuleCollection.getTggRules()) {
	    		//matches.addAll(findNextMatching(curMatch.getfSystemState(), rule, _first));
	    		// for each match
	    		if (!result){
	        		matches = findNextMatching(curMatch.getfSystemState(), rule);
	            	for (Matching matching : matches) {
	            		fSession.system().beginVariation();
	            		fSession.system().setRunningTestSuite(true);
	            		if (!result){
	    					matching.runOperation(fLogWriter);
	    					matching.setPrevious(curMatch);
	    					if (!matching.isHasFailed()){
	    						if (checkTestCase(matching)){
	    							//System.err.println(fSession.system().getVariationPointsStates().size());
	    							result = true;
	    							return true;
	    						}
	    						fSession.system().beginVariation();
	    						fSession.system().setRunningTestSuite(true);
	    						execute(matching);
	    						try {
	    							fSession.system().endVariation();
	    						} catch (MSystemException e) {
	    							e.printStackTrace();
	    						}
	    					}
	            		}
	            		try {
	    					fSession.system().endVariation();
	    				} catch (MSystemException e) {
	    					e.printStackTrace();
	    				}
	    			}
	    		}
			}
    	}
    	return result;
    }
    
    public List<Matching> findNextMatching(MSystemState fSystemState, MTggRule rule){
		MClass cls = fSession.system().model().getClass("RuleCollection");
		MOperation op = cls.operation(rule.name() + RTLKeyword.integration, true);
		fLogWriter.println("Checking " + rule.name() + "..............");

		MatchingEachPart matching = new MatchingEachPart(op);
		matching.setRule(rule);
		List<Matching> result = matching.findMatching(fSystemState);
		matching = null;
		return result;
	}


	public void initParams(){
		fTggRuleCollection = Rules.getTggRuleCollection();
    	MatchingEachPart.setListMatch(null);
    	MatchingEachPart.getMatchHasRun().clear();
    	result = false;
    	input = new HashSet<MObject>();
    	input.addAll(fSession.system().state().allObjects());
	}
	
	/*
	 * All objects on input have transformed
	 */
	public boolean checkTestCase(Matching match){
		for (MObject obj : input) {
			boolean flag = false;
			//System.err.println(obj.name());
			if (obj != null && !obj.name().equals("rc")){
				Matching tmp = match;
				while(tmp != null && tmp.getPrevious() != null){
					if (tmp.getObjectList4LHS() != null && tmp.getObjectList4LHS().containsValue(obj)){
						flag = true;
						break;
					}
					tmp = tmp.getPrevious();
				}
				if (!flag)
					return false;
			}
		}
		// Check invariant??????
		return true;
	}
}
