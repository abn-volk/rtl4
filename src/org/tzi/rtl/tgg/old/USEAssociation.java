package org.tzi.rtl.tgg.old;

import java.util.ArrayList;

public class USEAssociation {
	ArrayList<String> description;
	ArrayList<USEMultiplicity> multiplicities;
	String name;
	
	public USEAssociation() {
		this.name = "";
		description = new ArrayList<String>();
		multiplicities = new ArrayList<USEMultiplicity>();
	}
	
	/**
	Parser
		association ConnectsFrom between
  			ActivityEdge[0..*] role outEdge
  			ActivityNode[0..1] role source
		end
	 */
	public void parser() {
		multiplicities.clear();
		if(description.size() > 2) {
			String line = description.get(0);
			String[] s = line.split(" ");
			if(s.length >= 2)
				this.name = s[1];
			for(int i = 1; i < description.size() - 1; i++) {
				line = description.get(i);
				/**
				 * Parser input to USEMultiplicity:	ProcessAssignment[0..*] role process
				 */
				if(line.contains("role")) {
					USEMultiplicity m = USEMultiplicity.parser(line.trim());
					multiplicities.add(m);
				}
			}
		}
	}
	
	public String toString() {
		String res = "";
		for(int i = 0; i < description.size(); i++)
			res += description.get(i) + "\n";
		return res;
	}
	
	public void appendDescription(String newline) {
		this.description.add(newline);
	}
	
	public void setDescription(ArrayList<String> description) {
		this.description.addAll(description);
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
	public USEMultiplicity getRole(int i) {
		if( i > multiplicities.size() )
			return null;
		else
			return multiplicities.get(i-1);
	}
}
