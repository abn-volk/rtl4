package org.tzi.rtl.tgg.mm;

import org.tzi.use.uml.sys.MLink;
import org.tzi.use.uml.sys.MObject;

public class MCorrLink {
	/**
	 * @uml.property  name="fCorrObject"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private MObject fCorrObject;
	/**
	 * @uml.property  name="fSourceLink"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private MLink fSourceLink;
	/**
	 * @uml.property  name="fTargetLink"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private MLink fTargetLink;
	public MCorrLink(MObject corrObject, MLink sourceLink, MLink targetLink) {
		fCorrObject = corrObject;
		fSourceLink = sourceLink;
		fTargetLink = targetLink;
	}
	public Object getCorrObject() {
		return fCorrObject;
	}
	public Object getSourceLink() {
		return fSourceLink;
	}
	public Object getTargetLink() {
		return fTargetLink;
	}

}
