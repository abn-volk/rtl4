package org.tzi.rtl.trafo.incremental;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.tzi.use.uml.sys.MObject;

/**
 * Stores information about a successful transform
 */

public class PerformedTransformation {
	
	/* Allows retrieving a correlation objects from a RHS object */
	private Map<String, String> rightToCorr;
	/* Allows retrieving object name from RHS target param name */
	private Map<String, String> paramToObj;
	/* Allows retrieving the OCL code that updates mapped attributes */
	private Map<String, String> corrToOcl;
	/* Allows retrieving param name from source */
	private Map<String, String> sourcetToParam;

	public PerformedTransformation() {
		rightToCorr = new HashMap<>();
		paramToObj = new HashMap<>();
		corrToOcl = new HashMap<>();
		sourcetToParam = new HashMap<>();
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
		return sourcetToParam;
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
			sourcetToParam.put(entry.getValue().name(), entry.getKey());
		}
	}
	
	public String getParamFromSource(String sourceName) {
		return sourcetToParam.get(sourceName);
	}
	
	
	
}
