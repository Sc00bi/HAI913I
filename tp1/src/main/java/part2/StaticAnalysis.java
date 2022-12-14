package part2;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import visitors.LinesVisitor;
import visitors.MethodDeclarationVisitor;
import visitors.MethodInvocationVisitor;
import visitors.PackageVisitor;
import visitors.TypeDeclarationVisitor;
import visitors.VariableDeclarationFragmentVisitor;

// Class that provides methods to calculate informations about an OO application
public class StaticAnalysis {
	/* ATTRIBUTES */
	// counters
	private static int numberOfClassesCounter, numberOfMethodsCounter, numberOfAttributesCounter, numberOfLinesCounter,
			linesInMethodsCounter;
	// maps
	private static Map<TypeDeclaration, Integer> classesNumberOfMethodsCollector = new HashMap<>();
	private static Map<TypeDeclaration, Integer> classesNumberOfAttributesCollector = new HashMap<>();
	private static Map<TypeDeclaration, Map<MethodDeclaration, Integer>> classesMethodsNumberOfLinesCollector = new HashMap<>();
	private static Map<MethodDeclaration, Integer> methodsNumberOfParameters = new HashMap<>();
	// set
	private static Set<String> packagesNamesDeclaration = new HashSet<>();
	// graph
	private static SimpleDirectedGraph<String, DefaultEdge> callGraph = new SimpleDirectedGraph<>(DefaultEdge.class);

	public StaticAnalysis() {
		this.callGraph = new SimpleDirectedGraph<>(DefaultEdge.class);
	}

	// execute the analysis of a .java file and stocks every informations needed
	public static void analyze(CompilationUnit parse) {
		numberOfClassesCounter += numberOfClasses(parse);
		numberOfMethodsCounter += numberOfMethods(parse);
		numberOfAttributesCounter += numberOfAttributes(parse);
		numberOfLinesCounter += numberOfLines(parse);
		linesInMethodsCounter += linesInMethod(parse);

		classesNumberOfAttributesCollector.putAll(classesNumberOfAttributes(parse));
		classesNumberOfMethodsCollector.putAll(classesNumberOfMethods(parse));
		classesMethodsNumberOfLinesCollector.putAll(getClassesMethodsNumberOfLines(parse));
		methodsNumberOfParameters.putAll(getEveryMethodsNumberOfAttributes(parse));

		packagesNamesDeclaration.addAll(packagesDeclarations(parse));

		updateGraph(parse);

	}

	// empties the StaticAnalysis attributes
	private static void empty() {
		numberOfClassesCounter = 0;
		classesNumberOfMethodsCollector.clear();
	}

	/*
	 * This part of the code focuses on implanting methods to collect informations
	 * about our application based on its code
	 */
	// collects the number of Classes of a .java file (Q 1.1.1)
	private static int numberOfClasses(CompilationUnit parse) {

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
	private static int numberOfLines(CompilationUnit parse) {
		LinesVisitor linesVisitor = new LinesVisitor();
		parse.accept(linesVisitor);

		return linesVisitor.getLinesCounter();
	}

	// returns the number of methods of the .java file (Q 1.1.3)
	private static int numberOfMethods(CompilationUnit parse) {
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
	private static HashSet<String> packagesDeclarations(CompilationUnit parse) {
		PackageVisitor packageVisitor = new PackageVisitor();
		parse.accept(packageVisitor);

		return packageVisitor.getPackageDeclarations();
	}

	// returns the number of lines contained in a method of the .java file (Q1.1.6)
	private static int linesInMethod(CompilationUnit parse) {
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
	private static int numberOfAttributes(CompilationUnit parse) {
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
	private static Map<TypeDeclaration, Integer> classesNumberOfMethods(CompilationUnit parse) {
		TypeDeclarationVisitor typeDeclarationVisitor = new TypeDeclarationVisitor();
		parse.accept(typeDeclarationVisitor);

		HashMap<TypeDeclaration, Integer> res = new HashMap<>();

		for (TypeDeclaration typeDeclaration : typeDeclarationVisitor.getTypeDeclarationList()) {
			if (!typeDeclaration.isInterface())
				res.put(typeDeclaration, typeDeclaration.getMethods().length);
		}
		return res;
	}

	// returns a map of classes names associated to the number of classes they
	// contain (Q1.1.9)
	private static Map<TypeDeclaration, Integer> classesNumberOfAttributes(CompilationUnit parse) {
		TypeDeclarationVisitor typeDeclarationVisitor = new TypeDeclarationVisitor();
		parse.accept(typeDeclarationVisitor);

		HashMap<TypeDeclaration, Integer> res = new HashMap<>();
		for (TypeDeclaration typeDeclaration : typeDeclarationVisitor.getTypeDeclarationList()) {
			res.put(typeDeclaration, typeDeclaration.getFields().length);
		}

		return res;
	}

	// returns a map of the 10% classes associated to the higher value (Q1.1.8,
	// & 1.1.9)
	private static Map<TypeDeclaration, Integer> getTenPourcentsClasses(Map<TypeDeclaration, Integer> map,
			int numberOfClasses) {
		long limit = (long) ((numberOfClasses) * 0.1);
		limit = 0. == limit ? 1 : limit;
		return map.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(limit)
				.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
	}

	// returns a map of methods with their lines of code (Q 1.1.12)
	@SuppressWarnings("unused")
	private static Map<MethodDeclaration, Integer> methodsNumberOfAttributes(CompilationUnit parse) {
		HashMap<MethodDeclaration, Integer> res = new HashMap<>();
		TypeDeclarationVisitor typeDeclarationVisitor = new TypeDeclarationVisitor();
		parse.accept(typeDeclarationVisitor);

		for (TypeDeclaration typeDeclaration : typeDeclarationVisitor.getTypeDeclarationList()) {
			for (MethodDeclaration md : typeDeclaration.getMethods()) {

				res.put(md, parse.getLineNumber(md.getLength()) - parse.getLineNumber(md.getRoot().getStartPosition()));
			}
		}

		return res;
	}

	// returns a map of the 10% methods associated to the higher value (Q1.1.12)
	private static Map<MethodDeclaration, Integer> getTenPourcentsMethods(Map<MethodDeclaration, Integer> map,
			int numberOfClasses) {
		long limit = (long) ((numberOfClasses) * 0.1);
		limit = 0. == limit ? 1 : limit;
		return map.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(limit)
				.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
	}

	// returns a map of a map of methods associated to their number of lines
	// associated to their class (Q 1.1.12)
	private static HashMap<TypeDeclaration, HashMap<MethodDeclaration, Integer>> getClassesMethodsNumberOfLines(
			CompilationUnit parse) {
		HashMap<TypeDeclaration, HashMap<MethodDeclaration, Integer>> res = new HashMap<>();
		TypeDeclarationVisitor typeDeclarationVisitor = new TypeDeclarationVisitor();
		parse.accept(typeDeclarationVisitor);

		for (TypeDeclaration typeDeclaration : typeDeclarationVisitor.getTypeDeclarationList()) {
			HashMap<MethodDeclaration, Integer> tmp = new HashMap<MethodDeclaration, Integer>();
			if (!typeDeclaration.isInterface()) {
				for (MethodDeclaration md : typeDeclaration.getMethods()) {
					tmp.put(md,
							parse.getLineNumber(md.getLength()) - parse.getLineNumber(md.getRoot().getStartPosition()));
				}
				res.put(typeDeclaration, tmp);
			}
		}
		return res;
	}

	// returns a map of the every methods associated to their number of attributes
	// (Q1.1.13)
	private static Map<MethodDeclaration, Integer> getEveryMethodsNumberOfAttributes(CompilationUnit parse) {
		HashMap<MethodDeclaration, Integer> res = new HashMap<>();
		TypeDeclarationVisitor typeDeclarationVisitor = new TypeDeclarationVisitor();
		parse.accept(typeDeclarationVisitor);

		for (TypeDeclaration typeDeclaration : typeDeclarationVisitor.getTypeDeclarationList()) {
			for (MethodDeclaration md : typeDeclaration.getMethods()) {

				res.put(md, md.parameters().size());
			}
		}

		return res;
	}

	// answers Q1
	public static int getNumberOfClassesCounter() {
		return numberOfClassesCounter;
	}

	// answers Q2
	public static int getTotalNumberOfLinesCounter() {
		return numberOfLinesCounter;
	}

	// answers Q3
	public static int getTotalNumberOfMethods() {
		int res = 0;
		for (TypeDeclaration className : classesNumberOfMethodsCollector.keySet()) {
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

	// answers Q6
	public static double averageNumberOfLinesByMethods() {
		return ((double) linesInMethodsCounter) / ((double) numberOfMethodsCounter);
	}

	// answers Q7
	public static double averageAttributesNumber() {
		return ((double) numberOfAttributesCounter) / ((double) numberOfClassesCounter);
	}

	// answers Q8
	public static Map<TypeDeclaration, Integer> top10PourcentClassesByMethods() {
		return getTenPourcentsClasses(classesNumberOfMethodsCollector, numberOfClassesCounter);
	}

	// answers Q9
	public static Map<TypeDeclaration, Integer> top10PourcentClassesByAttributes() {
		return getTenPourcentsClasses(classesNumberOfAttributesCollector, numberOfClassesCounter);
	}

	// answers Q10
	public static ArrayList<TypeDeclaration> top10PourcentAttributesAndMethodsIntersection() {
		ArrayList<TypeDeclaration> res = new ArrayList<>();

		for (TypeDeclaration key : top10PourcentClassesByAttributes().keySet()) {
			if (top10PourcentClassesByMethods().containsKey(key)) {
				res.add(key);
			}
		}
		return res;

	}

	// answers Q11
	public static Map<TypeDeclaration, Integer> classesXMethods(int x) {
		HashMap<TypeDeclaration, Integer> res = new HashMap<TypeDeclaration, Integer>();
		for (TypeDeclaration key : classesNumberOfMethodsCollector.keySet()) {
			if (classesNumberOfMethodsCollector.get(key) >= x) {
				res.put(key, classesNumberOfMethodsCollector.get(key));
			}
		}
		return res;
	}

	// answers Q12
	public static Map<TypeDeclaration, Map<MethodDeclaration, Integer>> methodsTopTen() {
		for (TypeDeclaration key : classesMethodsNumberOfLinesCollector.keySet()) {
			HashMap<MethodDeclaration, Integer> tmpMap = (HashMap<MethodDeclaration, Integer>) getTenPourcentsMethods(
					classesMethodsNumberOfLinesCollector.get(key), numberOfClassesCounter);
			classesMethodsNumberOfLinesCollector.replace(key, tmpMap);
		}

		return classesMethodsNumberOfLinesCollector;
	}

	// answers Q13
	public static String maximumNumberOfParameters() {
		String res = "";
		MethodDeclaration topMethod;
		int max = 0;
		for (MethodDeclaration md : methodsNumberOfParameters.keySet()) {
			if (methodsNumberOfParameters.get(md) > max) {
				max = methodsNumberOfParameters.get(md);
				topMethod = md;
				res = topMethod.getName().toString() + " : " + max + " param?tre(s)";
			}
		}
		return "La m?thode contenant le plus de param?tres est " + res;
	}

	/*
	 * This part of the code focuses on providing methods meant to generate a call
	 * graph based on the static analysis of our application
	 */

	// adds to the graph every method invocation represented as a vertex and links
	// it with the method calling it
	private static void updateGraph(CompilationUnit parse) {
		TypeDeclarationVisitor typeDeclarationVisitor = new TypeDeclarationVisitor();
		parse.accept(typeDeclarationVisitor);

		for (TypeDeclaration typeDeclaration : typeDeclarationVisitor.getTypeDeclarationList()) {
			for (MethodDeclaration methodDeclaration : typeDeclaration.getMethods()) {
				methodDeclaration.resolveBinding();
				MethodInvocationVisitor methodInvocationVisitor = new MethodInvocationVisitor();
				methodDeclaration.accept(methodInvocationVisitor);

				String methodCalling = typeDeclaration.getName().toString() + "::"
						+ methodDeclaration.getName().toString() + "()";

				for (MethodInvocation methodInvocation : methodInvocationVisitor.getMethods()) {

					// verifying the method caller is not yet in the graph, else we add it
					if (!callGraph.containsVertex(methodCalling)) {
						callGraph.addVertex(methodCalling);
					}
					String methodCalled = "";

					// if the expression is explicite
					if (methodInvocation.getExpression() != null) {
						if (methodInvocation.getExpression().resolveTypeBinding() != null) {
							methodCalled = methodInvocation.getExpression().resolveTypeBinding().getName().toString()
									+ "::" + methodInvocation.getName().toString() + "()";

						}
						// else, if
					} else if (methodInvocation.resolveMethodBinding() != null) {

						methodCalled = methodInvocation.resolveMethodBinding().getDeclaringClass().toString() + "::"
								+ methodInvocation.getName().toString() + "()";
						// if the called method is static
					} else {
						methodCalled = methodDeclaration.getName().toString() + "::"
								+ methodInvocation.getName().toString() + "()";
					}
					if (!methodCalled.equals("")) {
						callGraph.addVertex(methodCalled);
						callGraph.addEdge(methodCalling, methodCalled);
					}
				}

			}
		}
	}

	// answers Q2.1
	public static SimpleDirectedGraph<String, DefaultEdge> getCallGraph() {
		return callGraph;
	}

	/*
	 * Methods provided by M.Rima and M.Seriai
	 */
	// navigate variables inside method
	@SuppressWarnings("unused")
	private static void printVariableInfo(CompilationUnit parse) {

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
	@SuppressWarnings("unused")
	private static void printMethodInvocationInfo(CompilationUnit parse) {

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
	@SuppressWarnings("unused")
	private static void printMethodInfo(CompilationUnit parse) {
		MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
		parse.accept(visitor);

		for (MethodDeclaration method : visitor.getMethodDeclarationList()) {
			System.out.println("Method name: " + method.getName() + " Return type: " + method.getReturnType2());
		}

	}
}