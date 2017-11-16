package org.tzi.rtl.trafo.incremental;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.tzi.rtl.gui.plugins.tgg.ActionFindAllMatchForward;
import org.tzi.rtl.gui.plugins.tgg.Rules;
import org.tzi.rtl.tgg.mm.MTggRule;
import org.tzi.use.gui.main.MainWindow;
import org.tzi.use.main.Session;
import org.tzi.use.uml.mm.MAssociation;
import org.tzi.use.uml.mm.MClass;
import org.tzi.use.uml.sys.MLink;
import org.tzi.use.uml.sys.MLinkEnd;
import org.tzi.use.uml.sys.MObject;
import org.tzi.use.uml.sys.events.AttributeAssignedEvent;
import org.tzi.use.uml.sys.events.LinkDeletedEvent;
import org.tzi.use.uml.sys.events.LinkInsertedEvent;
import org.tzi.use.uml.sys.events.ObjectCreatedEvent;
import org.tzi.use.uml.sys.events.ObjectDestroyedEvent;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class ChangeListenerDialog extends JPanel {

	private static final long serialVersionUID = 72384374L;
	
	private Map<String, Set<MTggRule>> sourceRHSClassRules = new HashMap<>();
	// private Map<String, Set<MTggRule>> sourceRHSClassRules = new HashMap<>();
	private Map<String, Set<MTggRule>> sourceAssociationRules = new HashMap<>();
	private Map<String, Set<MTggRule>> attrRules = new HashMap<>();
	
	private PrintWriter fLogWriter;
	private Session fSession;
	private EventBus fEventBus;
	private boolean running;
	
	public ChangeListenerDialog(MainWindow parent, Session session) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		JLabel label1 = new JLabel("<html>Keep this window open to track incremental changes and update the model accordingly.</html>");
		add(label1);
		fLogWriter = parent.logWriter();
		fSession = session;
		fEventBus = session.system().getEventBus();
		startTracking();
	}
	
	public void startTracking() {
		fEventBus.register(this);
		setRules(Rules.getTggRuleCollection().getTggRules());
	}

	public void setRules(Collection<MTggRule> rules) {
		for (MTggRule rule : rules) {
			fLogWriter.println(String.format("Rule: %s", rule.name().toString()));
			List<MObject> corrObjects = rule.getCorrRule().getAllObjects();
			fLogWriter.println(String.format("Correlation objects: %s", corrObjects.toString()));
			List<MObject> rhsObjects = rule.getSourceRule().getRHS().getObjects();
			fLogWriter.println(String.format("LHS source objects: %s", rhsObjects.toString()));
			List<MObject> rhsObjectsWithLinks = rule.getSourceRule().getRHS().getLinks().stream().flatMap(lnk -> lnk.linkedObjects().stream()).collect(Collectors.toList());
			fLogWriter.println(String.format("LHS objs with links: %s", rhsObjectsWithLinks.toString()));
			rhsObjects.removeAll(rhsObjectsWithLinks);
			Set<MClass> lhsClassesToWatch = rhsObjects.stream().map(o -> o.cls()).collect(Collectors.toSet());
			fLogWriter.println(String.format("Classes to watch: %s", lhsClassesToWatch.toString()));
			for (MClass cls : lhsClassesToWatch) {
				Set<MTggRule> ruleSet = sourceRHSClassRules.get(cls.name());
				if (ruleSet == null) {
					ruleSet = new HashSet<MTggRule>();
					sourceRHSClassRules.put(cls.name(), ruleSet);
				}
				ruleSet.add(rule);
			}
			List<MObject> sourceObjects = rule.getSourceRule().getAllObjects();
			Set<MAssociation> sourceAssociations = rule.getSourceRule().getNewLinks().stream().filter(lnk -> {
				for (MLinkEnd e : lnk.linkEnds()) {
					if (! sourceObjects.contains(e.object())) return false;
				}
				return true;
			}).map(lnk -> lnk.association()).collect(Collectors.toSet());
			fLogWriter.println(String.format("Source associations: %s", sourceAssociations.toString()));
			for (MAssociation asc : sourceAssociations) {
				Set<MTggRule> ruleSet = sourceAssociationRules.get(asc.name());
				if (ruleSet == null) {
					ruleSet = new HashSet<MTggRule>();
					sourceAssociationRules.put(asc.name(), ruleSet);
				}
				ruleSet.add(rule);
			}
			List<MObject> newRHSObjs = rule.getSourceRule().getRHS().getObjects();
			for (MLink corrLink : rule.getCorrRule().getRHS().getLinks()) {
				for (MObject end : corrLink.linkedObjects()) {
					if (newRHSObjs.contains(end)) {
						Set<MTggRule> ruleSet = attrRules.get(end.cls().name());
						if (ruleSet == null) {
							ruleSet = new HashSet<MTggRule>();
							attrRules.put(end.cls().name(), ruleSet);
						}
						ruleSet.add(rule);
					}
				}
			}
			/* 
			List<MObject> rhsObjects = rule.getSourceRule().getRHS().getObjects();
			Set<MClass> rhsClassesToWatch = lhsObjects.stream().map(o -> o.cls()).collect(Collectors.toSet());
			for (MClass cls : rhsClassesToWatch) {
				Set<MTggRule> ruleSet = sourceRHSClassRules.get(cls.name());
				if (ruleSet == null) {
					ruleSet = new HashSet<MTggRule>();
					sourceRHSClassRules.put(cls.name(), ruleSet);
				}
				ruleSet.add(rule);
			}
			*/
		}
		fLogWriter.println("Class - rule mappings: " + sourceRHSClassRules.toString());
		fLogWriter.println("Association - rule mappings: " + sourceAssociationRules.toString());
		fLogWriter.println("Attr - rule mappings: " + attrRules.toString());
	}
	
	@Subscribe
    public void onObjectCreated(ObjectCreatedEvent e) {
		if (!running) {
			MObject obj = e.getCreatedObject();
			Set<MTggRule> matchedRules = sourceRHSClassRules.get(obj.cls().name());
			fLogWriter.println(String.format("Object %s:%s created. Rules %s matched.", obj.name(), obj.cls().name(), matchedRules.toString()));
			if (matchedRules != null) {
				incrementalTransform(matchedRules);
			}
		}
	}
	
	@Subscribe
    public void onObjectDestroyed(ObjectDestroyedEvent e) {
		
	}
	
	@Subscribe
    public void onAttributeAssignment(AttributeAssignedEvent e) {
	}
	
	@Subscribe
	public void onLinkInserted(LinkInsertedEvent e) {
		if (!running) {
			MAssociation asc = e.getAssociation();
			fLogWriter.println("Inserted: " + asc.name());
			Set<MTggRule> possibleRules = sourceAssociationRules.get(asc.name());
			if (possibleRules != null) {
				fLogWriter.println("Possible rules found: " + possibleRules.toString());
				incrementalTransform(possibleRules);
			}
		}
	}
	
	@Subscribe
	public void onLinkDeleted(LinkDeletedEvent e) {
	}
	
	@Subscribe
	public void onMatchEvent(MatchEvent e) {
		running = e.isRunning();
	}
	
	private void incrementalTransform(Set<MTggRule> possibleRules) {
		fLogWriter.println("++++++++++++++++++++++++++++++");
        fLogWriter.println("Auto forward transformation...");
        ActionFindAllMatchForward.findAllMatchesForRules(fLogWriter, fSession, possibleRules);
        ActionFindAllMatchForward.setStep(-1);
        ActionFindAllMatchForward.nextStep(fLogWriter, fSession);
        while (ActionFindAllMatchForward.getCurrentMatch() != null){
        	ActionFindAllMatchForward.runMatch(fSession, fLogWriter);
        }
        fLogWriter.println("Transformation complete.");
	}
	
	public void unsubscribe() {
		fEventBus.unregister(this);
	}

}
