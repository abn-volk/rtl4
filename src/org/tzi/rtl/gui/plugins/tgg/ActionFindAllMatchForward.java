package org.tzi.rtl.gui.plugins.tgg;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import org.tzi.use.uml.sys.MSystemState;

public class ActionFindAllMatchForward  implements IPluginActionDelegate {
	static TggRuleCollection fTggRuleCollection;
	static List<Matching> result;
	private static Matching currentMatch;
	static int size = 0;
	static int step = 0;
	static boolean mustRematch = true; // after state changed, we have to re-find matches
    Session fSession;
    PrintWriter fLogWriter;
	public ActionFindAllMatchForward(){
		
	}

    public ActionFindAllMatchForward(Session fSession, PrintWriter fLogWriter) {
        this.fSession = fSession;
        this.fLogWriter = fLogWriter;
    }

    public void performAction() {
        if (fSession != null && fLogWriter != null) {
            fLogWriter.println("++++++++++++++++++++");
            fLogWriter.println("Find all match forward ...");
            findAllMatch(fLogWriter, fSession);
            step = -1;
            nextStep(fLogWriter, fSession);
            fLogWriter.println("Done.");
        }
    }

	@Override
	public void performAction(IPluginAction pluginAction) {
		fSession = pluginAction.getSession();
		MainWindow window = pluginAction.getParent();
		fLogWriter = window.logWriter();

        performAction();
	}

	public static void findAllMatch(PrintWriter fLogWriter, Session fSession){
    	fTggRuleCollection = Rules.getTggRuleCollection();
    	findAllMatchesForRules(fLogWriter, fSession, fTggRuleCollection.getTggRules());
	}
	
	public static void findAllMatchesForRules(PrintWriter fLogWriter, Session fSession, Collection<MTggRule> rules) {
		MatchingEachPart.setListMatch(null);
    	MatchingEachPart.getMatchHasRun().clear();
    	Matching firstMatch = new MatchingEachPart();
    	firstMatch.setfSystemState(fSession.system().state());
		List<Matching> matches = new ArrayList<Matching>();
    	// find all match with current state
		for (MTggRule rule : rules) {
    		matches.addAll(findMatching(firstMatch.getfSystemState(), rule, fSession));
    	}
    	result = matches;
    	size = result.size();
    	if (size==0)
    		fLogWriter.println("No match has found.");
    	else{
    		fLogWriter.println("Found " + size + " matches.");
        	setCurrentMatch(result.get(0));
        	//fLogWriter.print("Select match 1: ");
        	//fLogWriter.println(currentMatch.getRule().name() + ": " + currentMatch.getObjectList4LHS() );
    	}
    	setMustRematch(false);
	}
	
	public static List<Matching> findMatching(MSystemState fSystemState, MTggRule rule, Session fSession){
		MClass cls = fSession.system().model().getClass("RuleCollection");
		MOperation op = cls.operation(rule.name() + RTLKeyword.forwardTransform, true);
		System.out.println("Checking " + rule.name() + "..............");
		MatchingEachPart matching = new MatchingEachPart(op);
		matching.setRule(rule);
		return matching.findMatching(fSystemState);
	}
	
	public static Matching nextStep(PrintWriter fLogWriter, Session fSession){
		if (isMustRematch()){
    		findAllMatch(fLogWriter, fSession);
    		step = -1;
    	}
		if (size > 0){
			step++;
			if (step != size){
				setCurrentMatch(result.get(step)); 
				fLogWriter.print("Select match " + (step + 1) + ": ");
	        	fLogWriter.println(getCurrentMatch().getRule().name() + ": " + getCurrentMatch().getObjectList4LHS());
			}
			else
				step--;
			return getCurrentMatch();
		}
		return null;
	}
	
	public static Matching previousStep(PrintWriter fLogWriter, Session fSession){
		if (isMustRematch()){
    		findAllMatch(fLogWriter, fSession);
    		step = size;
    	}
		if (size > 0){
			step--;
			if (step != -1){
				setCurrentMatch(result.get(step)); 
				fLogWriter.print("Select match " + (step + 1) + ": ");
	        	fLogWriter.println(getCurrentMatch().getRule().name() + ": " + getCurrentMatch().getObjectList4LHS());
			}
			else
				step++;
			return getCurrentMatch();
		}
		return null;
	}

	public static void runMatch(Session _fSession, PrintWriter fLogWriter){
		if (getCurrentMatch() != null){
    		setMustRematch(true);
    		getCurrentMatch().runOperation(fLogWriter);
    		if (getCurrentMatch().isHasFailed()){
    			fLogWriter.println("Run match fail.");
    			setCurrentMatch(null);
    		}
    		else{
    			fLogWriter.println("Run match success.");
    			setCurrentMatch(null);
    			nextStep(fLogWriter, _fSession);
    		}
    		//currentMatch = null;
    	}
    	else
    		fLogWriter.println("No match selected.");
	}
	
	/**
	 * @return the mustRematch
	 */
	public static boolean isMustRematch() {
		return mustRematch;
	}

	/**
	 * @param mustRematch the mustRematch to set
	 */
	public static void setMustRematch(boolean _mustRematch) {
		mustRematch = _mustRematch;
	}

    public static void setStep(int s) {
        step = s;
    }

	public static Matching getCurrentMatch() {
		return currentMatch;
	}

	public static void setCurrentMatch(Matching currentMatch) {
		ActionFindAllMatchForward.currentMatch = currentMatch;
	}
}
