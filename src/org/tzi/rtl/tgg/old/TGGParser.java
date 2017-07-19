package org.tzi.rtl.tgg.old;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.tzi.rtl.tgg.old.TGGInvariant;
import org.tzi.rtl.tgg.old.USEAssociation;
import org.tzi.rtl.tgg.old.USEClass;
import org.tzi.rtl.tgg.parser.RTLKeyword;
import org.tzi.rtl.tgg.old.RTLRegex;
import org.tzi.rtl.tgg.old.TGGRule;

public class TGGParser {

	String sourceMM;
	String targetMM;
	String tggFileName;
	String pathDir;
	String model;
	String transformation;
	static ArrayList<USEClass> useClasses;
	static ArrayList<USEAssociation> associations;
	static ArrayList<TGGRule> rules;
	static ArrayList<TGGInvariant> invariants;
	static String modelConstraint;
	String generateUSE;
	String generateTGGs;
	PrintWriter fLogWriter;
	String nameOfUseFile;
	String nameOfTGGsRuleFile;

	public TGGParser(PrintWriter fLogWriter) {
		if(useClasses == null)
			useClasses = new ArrayList<USEClass>();
		if(associations == null)
			associations = new ArrayList<USEAssociation>();
		if(rules == null)
			rules = new ArrayList<TGGRule>();
		if(invariants == null)
			invariants = new ArrayList<TGGInvariant>();
		modelConstraint = "";
		generateUSE = "";
		generateTGGs = "";
		if(fLogWriter == null)
			this.fLogWriter = new PrintWriter(System.out, true);
		else
			this.fLogWriter = fLogWriter;
		nameOfUseFile = null;
		nameOfTGGsRuleFile = null;
	}

	public TGGParser(String pathDir, String sourceMM, String targetMM, String tggFileName, PrintWriter fLogWriter) {
		this.sourceMM = sourceMM;
		this.pathDir = pathDir;
		this.tggFileName = tggFileName;
		this.targetMM = targetMM;
		if(useClasses == null)
			useClasses = new ArrayList<USEClass>();
		if(associations == null)
			associations = new ArrayList<USEAssociation>();
		if(rules == null)
			rules = new ArrayList<TGGRule>();
		if(invariants == null)
			invariants = new ArrayList<TGGInvariant>();
		modelConstraint = "";
		generateUSE = "";
		generateTGGs = "";
		if(fLogWriter == null)
			this.fLogWriter = new PrintWriter(System.out, true);
		else
			this.fLogWriter = fLogWriter;
		nameOfUseFile = null;
		nameOfTGGsRuleFile = null;
	}

	/**
	 * Check if an class is exists.
	 */
	public boolean checkClassExists(String type) {
		USEClass c;
		for(int i = 0; i < useClasses.size(); i++) {
			c = useClasses.get(i);
			if(c.getName().equals(type))
				return true;
		}
		return false;
	}

	/**
	We need to check if an association is exists.
	 */

	public boolean checkAssociationExists(String type) {
		USEAssociation a;
		for(int i = 0; i < associations.size(); i++) {
			a = associations.get(i);
			if(a.getName().equals(type))
				return true;
		}
		return false;
	}

	/**
	We need to check if an invariants is exists
	 */
	public boolean checkInvariantExists(TGGInvariant inv) {
		for(int i = 0; i < invariants.size(); i++) {
			if(invariants.get(i).equals(inv))
				return true;
		}
		return false;
	}

	/**
	 * This function reads the input file and return all TGGRules
	 */
	public void getTGGRules() {
		String fullPath = tggFileName;
		TGGRule rule = null;
		String ruleName = null;
		boolean isRule = false;
		try {
			BufferedReader br = new BufferedReader(new FileReader(fullPath));
			String line = null;
			line = br.readLine();

			while(line != null) {
				if(isRule) {
					rule.appendDescription(line);
					if(line.startsWith(RTLKeyword.endTGGRule)) {
						rules.add(rule);
						rule = null;
						isRule = false;
					}
				} else if(line.startsWith(RTLKeyword.startTGGRule)) {
					ruleName = line.substring(4).trim();
					rule = new TGGRule(ruleName);
					isRule = true;
				} else if(transformation == null && line.startsWith(RTLKeyword.transformation)) {
					transformation = line.substring(RTLKeyword.transformation.length()).trim();
					if(transformation.length() == 0)
						transformation = null;
				}
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This function read the input file and return all classess
	 */
	public void getSourceUSEClasses() {
		String fullPath = sourceMM;
		USEClass c = null;
		boolean isClass = false;
		try {
			BufferedReader br = new BufferedReader(new FileReader(fullPath));
			String line = null;
			line = br.readLine();

			while(line != null) {
				if(isClass) {
					c.appendDescription(line);
					if(line.startsWith(RTLKeyword.endClass)) {
						useClasses.add(c);
						c = null;
						isClass = false;
					}
				} else if(line.startsWith(RTLKeyword.startClass)) {
					c = new USEClass();
					c.appendDescription(line);
					isClass = true;
				} else if(model == null && line.startsWith(RTLKeyword.model)) {
					model = line.substring(RTLKeyword.model.length()).trim();
					if(model.length() == 0)
						model = null;
				}
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void getTargetUSEClasses() {
		String fullPath = targetMM;
		USEClass c = null;
		boolean isClass = false;
		try {
			BufferedReader br = new BufferedReader(new FileReader(fullPath));
			String line = null;
			line = br.readLine();

			while(line != null) {
				if(isClass) {
					c.appendDescription(line);
					if(line.startsWith(RTLKeyword.endClass)) {
						useClasses.add(c);
						c = null;
						isClass = false;
					}
				} else if(line.startsWith(RTLKeyword.startClass)) {
					c = new USEClass();
					c.appendDescription(line);
					isClass = true;
				} else if(line.startsWith(RTLKeyword.model)) {
					model += line.substring(RTLKeyword.model.length()).trim();
				}
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	/**
	 * This function reads the input file and return all associations.
	 */
	public void getUSEAssociations() {
		String fullPath = sourceMM;
		USEAssociation as = null;
		boolean isAssociation = false;
		try {
			BufferedReader br = new BufferedReader(new FileReader(fullPath));
			String line = null;
			line = br.readLine();

			while(line != null) {
				if(isAssociation) {
					as.appendDescription(line);
					if(line.startsWith(RTLKeyword.endAssociation)) {
						associations.add(as);
						as = null;
						isAssociation = false;
					}
				} else if(line.startsWith(RTLKeyword.startAssociation)) {
					as = new USEAssociation();
					as.appendDescription(line.trim());
					isAssociation = true;
				}
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This function reads all constraints from the input file
	 */
	public void getModelConstraints() {
		String[] mms = {sourceMM, targetMM};
		boolean isConstraint = false;
		for (String fullPath : mms) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(fullPath));
				String line = null;
				line = br.readLine();
	
				while(line != null) {
					if(isConstraint) {
						if(line.startsWith(RTLKeyword.endConstraint))
							isConstraint = false;
						else if(line.trim().length() > 0) {
							if(line.startsWith("--"))
								modelConstraint += "\n" + line + "\n";
							else 
								modelConstraint += line + "\n";
						}
					} else if(line.startsWith(RTLKeyword.startConstraint)) {
						isConstraint = true;
					}
					line = br.readLine();
				}
				br.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	/**
	 * This function writes TGGRules to file
	 */
	public void writeTGGFile() {
		if(rules.size() == 0)
			return;
		String fullPath = pathDir + "/" + transformation + "Rule.tgg";
		this.nameOfTGGsRuleFile = fullPath;
		PrintWriter out;
		try {
			out = new PrintWriter(new FileWriter(fullPath));
			out.print(generateTGGs);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This function writes file model.use
	 */
	public void writeUSEFile() {
		String fTGGsRule = model + ".use";
		String fullPath = pathDir + "/" + fTGGsRule;
		this.nameOfUseFile = fullPath;
		try {
			PrintWriter out = new PrintWriter(new FileWriter(fullPath));
			out.print(generateUSE);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function writes all command file (Rewrite TGGRules)
	 */
	public void writeCommandFiles() {
		int l = rules.size();
		if(l == 0)
			return;
		TGGRule rule;
		String ruleName;
		String command;
		String fullpath;
		String filename;
		for(int i = 0; i < l; i++) {
			rule = rules.get(i);
			ruleName = rule.getName();
			try {
				command = rule.commandForCoEvolution(transformation);
				filename = transformation + "_" + ruleName + RTLKeyword.coEvolution + ".cmd";
				fullpath = pathDir + "/" + filename;
				PrintWriter out = new PrintWriter(new FileWriter(fullpath));
				out.println(command);
				out.close();

				command = rule.commandForForwardTransform(transformation);
				filename = transformation + "_" + ruleName + RTLKeyword.forwardTransform + ".cmd";
				fullpath = pathDir + "/" + filename;
				out = new PrintWriter(new FileWriter(fullpath));
				out.println(command);
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * We need to add some classes that are generated by TGGRules.
	 */
	public void generateUSEClassFromTGGRules() {
		ArrayList<USEClass> list = new ArrayList<USEClass>();
		ArrayList<USEClass> cs;
		USEClass c;
		for(int i = 0; i < rules.size(); i++ ) {
			cs = rules.get(i).generateUSEClasses();
			if(cs != null)
				list.addAll(cs);
		}
		for(int i = 0; i < list.size(); i++) {
			c = list.get(i);
			if(!checkClassExists(c.getName()))
				useClasses.add(c);
		}
	}

	/**
	 * We need to add some associations that are generated by TGGRules.
	 */
	public void generateAssociationsFromTGGRules() {
		ArrayList<USEAssociation> list = new ArrayList<USEAssociation>();
		ArrayList<USEAssociation> gen;
		USEAssociation a;
		for(int i = 0; i < rules.size(); i++ ) {
			gen = rules.get(i).generateUSEAssociations();
			if(gen != null)
				list.addAll(gen);
		}
		for(int i = 0; i < list.size(); i++) {
			a = list.get(i);
			if(!checkAssociationExists(a.getName()))
				associations.add(a);
		}
	}

//	public static void main(String []args) {
//		if(args.length == 0) {
//			System.out.println("Choose an input file.");
//		} else {
//			if(args.length >= 2 && args[1].trim().length() > 0 ) {
//				(new File(args[1])).mkdirs();
//				(new TGGParser(args[1].trim(), args[0].trim(), null)).parserFileInput();
//			} else {
//				String dir = "generate";
//				(new File(dir)).mkdirs();
//				(new TGGParser(dir, args[0].trim(), null)).parserFileInput();
//			}
//		}
//	}

	/**
	 * Start parser the input file and generate all files.
	 */
	public void parserFileInput() {
		fLogWriter.println("Compile the RTL specification " + sourceMM + " ...");
		//	Read the input and parser the input to 3 center part:	TGGRules - USEClassess - USEAssociations
		getTGGRules();
		getSourceUSEClasses();
		getTargetUSEClasses();
		getUSEAssociations();
		getModelConstraints();

		String defaultS = "s"; //	Default for Source role
		String defaultT = "t"; //	Default	for Target role
		if(transformation != null) {
			String regex = "^([a-zA-Z]+)2([a-zA-Z]+)$";
			if(transformation.matches(regex)) {
				String []regions = transformation.split("2");
				defaultS = regions[0];
				defaultT = regions[1];
			}
		}
		//	For each item in one of 3 center part, we need to parser
		for(int i = 0; i < rules.size(); i++) {
			rules.get(i).setTransform(defaultS, defaultT);
			rules.get(i).parser();
		}
		for(int i = 0; i < useClasses.size(); i++)
			useClasses.get(i).parser();
		for(int i = 0; i < associations.size(); i++)
			associations.get(i).parser();

		//	Add some class or association from TGGRules
		generateUSEClassFromTGGRules();
		generateAssociationsFromTGGRules();

		//	Add all invariants
		for(int i = 0; i < rules.size(); i++) {
			ArrayList<TGGInvariant> invs = rules.get(i).getInvariants();
			TGGInvariant inv;
			for(int j = 0; j < invs.size(); j++) {
				inv = invs.get(j);
				if(!checkInvariantExists(inv))
					invariants.add(inv);
			}
		}

		//	Generate Input: TGGsRule and USE Description
		//generateTGGsRule();
		//generateUSEDescription();

		//	Write output to file
//		fLogWriter.println("Compile USE model specification");
//		writeUSEFile();
//		fLogWriter.println("Compile TGGs incorporating OCL rules");
//		writeTGGFile();
		fLogWriter.println("Rewrite TGGs incorporating OCL rules");
		writeCommandFiles();
		fLogWriter.println("Compile success");
	}

	public void parserFileInput(String pathDir, String fileName) {
		this.pathDir = pathDir;
		this.sourceMM = fileName;
		this.parserFileInput();
	}

	public static ArrayList<TGGInvariant> USEInvariants() {
		return invariants;
	}

	public static ArrayList<TGGRule> TGGRules() {
		return rules;
	}

	public static ArrayList<USEClass> USEClasses() {
		return useClasses;
	}

	public static ArrayList<USEAssociation> USEAssociations() {
		return associations;
	}

	public static USEClass searchUSEClass(String name) {
		USEClass c;
		for(int i = 0; i < useClasses.size(); i++) {
			c = useClasses.get(i);
			if(c.getName().equals(name))
				return c;
		}
		return null;
	}

	public static USEAssociation searchUSEAssociation(String name) {
		USEAssociation a;
		for(int i = 0; i < associations.size(); i++) {
			a = associations.get(i);
			if(a.getName().equals(name))
				return a;
		}
		return null;
	}

	public String getUSEDescription() {
		return generateUSE;
	}

	public String getTGGsDescription() {
		return generateTGGs;
	}

	public String getNameOfInputFile() {
		return sourceMM;
	}

	public String getNameOfUseFile() {
		return nameOfUseFile;
	}

	public String getNameOfTGGsRuleFile() {
		return nameOfTGGsRuleFile;
	}

	/**
	 * This function writes TGGRules to file
	 */
	public void generateTGGsRule() {
		if(rules.size() == 0)
			return;
		if(transformation == null || transformation.length() == 0)
			transformation = "transform";
		String fullPath = pathDir + "/" + transformation + "Rule.tgg";
		PrintWriter out;
		String genTGGsRule = "";

		genTGGsRule += RTLKeyword.transformation + RTLRegex.indent(1) + transformation + "\n";
		TGGRule rule;
		for(int i = 0; i < rules.size(); i++) {
			rule = rules.get(i);
			genTGGsRule += RTLRegex.comment(50, '-') + "\n";
			genTGGsRule += RTLKeyword.startTGGRule + RTLRegex.indent(1) + rule.getName() + "\n";
			genTGGsRule += rule.getDescription();
		}
		try {
			out = new PrintWriter(new FileWriter(fullPath));
			out.print(genTGGsRule);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		generateTGGs = genTGGsRule;
	}

	/**
	 * This function writes file model.use
	 */
	public void generateUSEDescription() {
		String genUSE = "";
		String modelDescription = RTLKeyword.model + RTLRegex.indent(1) + model + "\n\n";
		for(int i = 0; i < useClasses.size(); i++)
			modelDescription += useClasses.get(i).toString() + "\n";
		for(int i = 0; i < associations.size(); i++)
			modelDescription += associations.get(i).toString();
		TGGRule tggRule;
		genUSE += modelDescription + "\n";
		genUSE += "constraints\n";
		genUSE += modelConstraint + "\n";
		/**
		 * For case: there aren't any TGGsRule in the specification file
		 */
		if(rules.size() == 0) {
			generateUSE = genUSE;
			return;
		}
		genUSE += "\nclass RuleCollection\noperations\n";
		genUSE += RTLRegex.comment(70, '-') + "CoEvolution\n";
		for(int i = 0; i < rules.size(); i++)
			genUSE += rules.get(i).coEvolutionTuple(2) + "\n";
		genUSE += RTLRegex.comment(70, '-') + "ForwardTransform\n";
		for(int i = 0; i < rules.size(); i++)
			genUSE += rules.get(i).forwardTraformTuple(2) + "\n";
		genUSE += "\nend\n";
		//	For constraints
		genUSE += "constraints\n";
		genUSE += "--correspond class invariants\n";
		for(int i = 0; i < invariants.size(); i++)
			genUSE += invariants.get(i).toString() + "\n";
		genUSE += "\n";
		for(int i = 0; i < rules.size(); i++) {
			tggRule = rules.get(i);
			genUSE += tggRule.contextCoEvolution("RuleCollection", 0);
		}
		for(int i = 0; i < rules.size(); i++) {
			tggRule = rules.get(i);
			genUSE += tggRule.contextForwardTransform("RuleCollection", 0);
		}
		generateUSE = genUSE;
	}
} 
