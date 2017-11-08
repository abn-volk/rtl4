package org.tzi.rtl.tgg.mm;

import java.util.List;
public class MTggRuleMultiApplication extends MTggRuleApplication {
	private List<String> ruleApplications;
	private Object fConditions;
	
	public MTggRuleMultiApplication(){
		
	}
	
	public MTggRuleMultiApplication(List<String> _ruleApps, Object _cond){
		setRuleApplications(_ruleApps);
		setfConditions(_cond);
	}

	public void setfConditions(Object fConditions) {
		this.fConditions = fConditions;
	}

	public Object getfConditions() {
		return fConditions;
	}


	public void setRuleApplications(List<String> ruleApplications) {
		this.ruleApplications = ruleApplications;
	}


	public List<String> getRuleApplications() {
		return ruleApplications;
	}
}
