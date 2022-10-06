package part2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import visitors.AttributeVisitor;
import visitors.LinesVisitor;
import visitors.MethodDeclarationVisitor;
import visitors.MethodInvocationVisitor;
import visitors.PackageVisitor;
import visitors.TypeDeclarationVisitor;
import visitors.VariableDeclarationFragmentVisitor;

public class StaticAnalysis {
	private static int numberOfClassesCounter, numberOfMethodsCounter, numberOfAttributesCounter;
	private static Map<String, Integer> classesNumberOfMethodsCollector = new HashMap<>();
	private static Map<String, Integer> classesNumberOfAttributesCollector = new HashMap<>();
	private static Set<String> packagesNamesDeclaration = new HashSet<>();

	public static void analyse(CompilationUnit parse) {
		numberOfClassesCounter += numberOfClasses(parse);
		numberOfMethodsCounter += numberOfMethods(parse);
		numberOfAttributesCounter += numberOfAttributes(parse);
		
		classesNumberOfAttributesCollector.putAll(classesNumberOfAttributes(parse));
		classesNumberOfMethodsCollector.putAll(classesNumberOfMethods(parse));
		
		packagesNamesDeclaration.addAll(packagesDeclarations(parse));
	}

	// collects the number of Classes of a .java file (Q 1.1.1)
	public static int numberOfClasses(CompilationUnit parse) {

		TypeDeclarationVisitor typeVisitor = new TypeDeclarationVisitor();
		parse.accept(typeVisitor);
		int res = 0;

		for (TypeDeclaration typeDeclaration : typeVisitor.getTypeDeclarationList()) {
			if (!typeDeclaration.isInterface()) {
				res++;
			}
		}
		return res;
	}

	// returns the number of lines of a .java file (Q 1.1.2)
	public static int numberOfLines(CompilationUnit parse) {
		LinesVisitor linesVisitor = new LinesVisitor();
		parse.accept(linesVisitor);

		return linesVisitor.getLinesCounter();
	}

	// returns the number of methods of the .java file (Q 1.1.3)
	public static int numberOfMethods(CompilationUnit parse) {
		TypeDeclarationVisitor typeVisitor = new TypeDeclarationVisitor();
		parse.accept(typeVisitor);

		int res = 0;

		for (TypeDeclaration typeDeclaration : typeVisitor.getTypeDeclarationList()) {
			if (!typeDeclaration.isInterface())
				res += typeDeclaration.getMethods().length;
		}

		return res;
	}

	// returns a set of declarations from the .java file ( Q1.1.4)
	public static HashSet<String> packagesDeclarations(CompilationUnit parse) {
		PackageVisitor packageVisitor = new PackageVisitor();
		parse.accept(packageVisitor);

		return packageVisitor.getPackageDeclarations();
	}

	// returns the number of lines contained in a method of the .java file (Q1.1.6)
	public static int linesInMethod(CompilationUnit parse) {
		TypeDeclarationVisitor typeDeclarationVisitor = new TypeDeclarationVisitor();
		parse.accept(typeDeclarationVisitor);

		int cptLines = 0;
		for (TypeDeclaration typeDeclaration : typeDeclarationVisitor.getTypeDeclarationList()) {
			for (MethodDeclaration md : typeDeclaration.getMethods()) {
				cptLines += parse.getLineNumber(md.getLength()) - parse.getLineNumber(md.getRoot().getStartPosition());
			}
		}

		return cptLines;
	}

	// returns the number of attributes declared in the .java file (Q1.1.7)
	public static int numberOfAttributes(CompilationUnit parse) {
		/*
		 * AttributeVisitor attributeVisitor = new AttributeVisitor();
		 * parse.accept(attributeVisitor);
		 * 
		 * return attributeVisitor.getFields().size();
		 */
		TypeDeclarationVisitor typeDeclarationVisitor = new TypeDeclarationVisitor();
		parse.accept(typeDeclarationVisitor);

		int res = 0;
		for (TypeDeclaration typeDeclaration : typeDeclarationVisitor.getTypeDeclarationList()) {
			res += typeDeclaration.getFields().length;
		}
		return res;
	}

	// returns a map of classes names associated to the number of classes they
	// contain (Q1.1.8)
	public static Map<String, Integer> classesNumberOfMethods(CompilationUnit parse) {
		TypeDeclarationVisitor typeDeclarationVisitor = new TypeDeclarationVisitor();
		parse.accept(typeDeclarationVisitor);

		HashMap<String, Integer> res = new HashMap<String, Integer>();

		for (TypeDeclaration typeDeclaration : typeDeclarationVisitor.getTypeDeclarationList()) {
			if (!typeDeclaration.isInterface())
				res.put(typeDeclaration.getName().toString(), typeDeclaration.getMethods().length);
		}
		return res;
	}

	// returns a map of classes names associated to the number of classes they
	// contain (Q1.1.9)
	public static Map<String, Integer> classesNumberOfAttributes(CompilationUnit parse) {
		TypeDeclarationVisitor typeDeclarationVisitor = new TypeDeclarationVisitor();
		parse.accept(typeDeclarationVisitor);

		HashMap<String, Integer> res = new HashMap<>();
		for (TypeDeclaration typeDeclaration : typeDeclarationVisitor.getTypeDeclarationList()) {
			res.put(typeDeclaration.getName().toString(), typeDeclaration.getFields().length);
		}

		return res;
	}

	// returns a map of the 10% classes associated to the higher value (Q1.1.8,
	// 1.1.9, 1.1.12)
	public static Map<String, Integer> getTenPourcentsClasses(Map<String, Integer> map, int numberOfClasses) {
		long limit = (long) ((numberOfClasses) * 0.1);
		limit = 0. == limit ? 1 : limit;
		return map.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(limit)
				.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

	}

	// empties the StaticAnalysis attributes
	public static void empty() {
		numberOfClassesCounter = 0;
		classesNumberOfMethodsCollector.clear();

	}

	// navigate variables inside method
	public static void printVariableInfo(CompilationUnit parse) {

		MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
		parse.accept(visitor1);
		for (MethodDeclaration method : visitor1.getMethodDeclarationList()) {

			VariableDeclarationFragmentVisitor visitor2 = new VariableDeclarationFragmentVisitor();
			method.accept(visitor2);

			for (VariableDeclarationFragment variableDeclarationFragment : visitor2.getVariables()) {
				System.out.println("variable name: " + variableDeclarationFragment.getName() + " variable Initializer: "
						+ variableDeclarationFragment.getInitializer());
			}
		}
	}

	// navigate method invocations inside method
	public static void printMethodInvocationInfo(CompilationUnit parse) {

		MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
		parse.accept(visitor1);
		for (MethodDeclaration method : visitor1.getMethodDeclarationList()) {

			MethodInvocationVisitor visitor2 = new MethodInvocationVisitor();
			method.accept(visitor2);

			for (MethodInvocation methodInvocation : visitor2.getMethods()) {
				System.out.println("method " + method.getName() + " invoc method " + methodInvocation.getName());
			}
		}
	}

	// navigate method information
	public static void printMethodInfo(CompilationUnit parse) {
		MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
		parse.accept(visitor);

		for (MethodDeclaration method : visitor.getMethodDeclarationList()) {
			System.out.println("Method name: " + method.getName() + " Return type: " + method.getReturnType2());
		}

	}

	// answers Q1
	public static int getNumberOfClassesCounter() {
		return numberOfClassesCounter;
	}

	// answers Q3
	public static int getTotalNumberOfMethods() {
		int res = 0;
		for (String className : classesNumberOfMethodsCollector.keySet()) {
			res += classesNumberOfMethodsCollector.get(className);
		}

		return res;
	}

	// answers Q3 bis
	public static int getNumberOfMethodsCounter() {
		return numberOfMethodsCounter;
	}

	// answers Q4
	public static int getNumberOfPackages() {
		return packagesNamesDeclaration.size();
	}

	// answers Q5
	public static double averageNumberOfMethods() {
		return ((double) numberOfMethodsCounter) / ((double) numberOfClassesCounter);
	}

	// answers Q7
	public static double averageAttributesNumber() {
		return ((double) numberOfAttributesCounter) / ((double) numberOfClassesCounter);
	}

	// answers Q8
	public static Map<String, Integer> top10PourcentClassesByMethods() {
		return getTenPourcentsClasses(classesNumberOfMethodsCollector, numberOfClassesCounter);
	}

	// answers Q9
	public static Map<String, Integer> top10PourcentClassesByAttributes() {
		return getTenPourcentsClasses(classesNumberOfAttributesCollector, numberOfClassesCounter);
	}
	
	// answers Q10
	public static Map<String, String> top10PourcentAttributesAndMethodsIntersection()
	{
		Map<String, String> res = new HashMap<>();
		
		for (String key : classesNumberOfMethodsCollector.keySet())
		{
			if (classesNumberOfAttributesCollector.containsKey(key))
			{
				res.put(key, "Méthodes : " + classesNumberOfMethodsCollector.get(key) + "; Attributs : " + classesNumberOfAttributesCollector.get(key));
			}
		}
		return res;
				
	}
	
	// answers Q11
	public static Map<String, Integer> classesXMethods(int x)
	{
		HashMap<String, Integer> res = new HashMap<String, Integer>();
		for (String key : classesNumberOfMethodsCollector.keySet())
		{
			if (classesNumberOfMethodsCollector.get(key) >= x)
			{
				res.put(key, classesNumberOfMethodsCollector.get(key));
			}
		}
		return res;
	}
}