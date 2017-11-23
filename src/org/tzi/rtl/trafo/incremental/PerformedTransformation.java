package org.tzi.rtl.trafo.incremental;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.tzi.use.uml.sys.MLink;
import org.tzi.use.uml.sys.MObject;

/**
 * Stores information about a successful transform
 */

public class PerformedTransformation {
	
	private static int index = 0;
	
	/* Allows retrieving a correlation objects from a RHS object */
	private Map<String, String> rightToCorr = new HashMap<>();
	/* Allows retrieving object name from RHS target param name */
	private Map<String, String> paramToObj = new HashMap<>();
	/* Allows retrieving the OCL code that updates mapped attributes */
	private Map<String, String> corrToOcl = new HashMap<>();
	/* Allows retrieving param name from source */
	private Map<String, String> sourceToParam = new HashMap<>();
	/* Transformation index */
	private int id;
	/* Source objects */
	private List<String> sourceObjects = new ArrayList<>();
	/* Target objects */
	private List<String> targetObjects = new ArrayList<>();
	/* Links */
	private Set<MLink> sourceLinks = new HashSet<>();

	public PerformedTransformation() {
		id = index++;
	}
	
	public Map<String, String> getRightToCorr() {
		return rightToCorr;
	}

	public Map<String, String> getParamToObj() {
		return paramToObj;
	}

	public Map<String, String> getCorrToOcl() {
		return corrToOcl;
	}
	
	public Map<String, String> getSourceToParam() {
		return sourceToParam;
	}
		
	public String getCorrFromRight(String right) {
		return rightToCorr.get(right);
	}
	
	public void addRightToCorr(String right, String corr) {
		rightToCorr.put(right, corr);
	}
	
	public void addCorrOcl(String corr, String ocl) {
		corrToOcl.put(corr, "!set " + ocl);
	}
	
	public String getCorrOcl(String corr) {
		return corrToOcl.get(corr);
	}
	
	public void addParamToObjMapping(String param, String obj) {
		paramToObj.put(param, obj);
	}
	
	public String getObjFromParam(String param) {
		return paramToObj.get(param);
	}
	
	public void addSourceToParamMappings(Map<String, MObject> mappings) {
		for (Entry<String, MObject> entry : mappings.entrySet()) {
			sourceToParam.put(entry.getValue().name(), entry.getKey());
		}
	}
	
	public String getParamFromSource(String sourceName) {
		return sourceToParam.get(sourceName);
	}
	
	public List<String> getSourceObjects() {
		return sourceObjects;
	}

	public void addSourceObjects(Collection<MObject> objs) {
		for (MObject obj : objs) {
			sourceObjects.add(obj.name());
		}
	}

	public List<String> getTargetObjects() {
		return targetObjects;
	}

	public void addTargetObject(String objName) {
		this.targetObjects.add(objName);
	}

	public int getId() {
		return id;
	}
	
	public Set<MLink> getSourceLinks() {
		return sourceLinks;
	}
	
	public void addSourceLinks(Set<MLink> links) {
		sourceLinks.addAll(links);
	}
	
	
}
