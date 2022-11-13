package AST_part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import models.WeightedGraph;
import visitors.MethodDeclarationVisitor;
import visitors.MethodInvocationVisitor;
import visitors.TypeDeclarationVisitor;
import visitors.VariableDeclarationFragmentVisitor;

// Class that provides methods to calculate informations about an OO application
public class StaticAnalysis {
	/* ATTRIBUTES */
	// graph
	private static WeightedGraph coupledGraph = new WeightedGraph();

	// execute the analysis of a .java file and stocks every informations needed
	public static void analyze(CompilationUnit parse) {
		updateGraph(parse);
	}
	
	// returns a list of names of classes from the file
	public static List<String> getApplicationClassesNames(CompilationUnit parse)
	{
		List<String> res = new ArrayList<>();
		TypeDeclarationVisitor typeDeclarationVisitor = new TypeDeclarationVisitor();
		parse.accept(typeDeclarationVisitor);
		for (TypeDeclaration typeDeclaration : typeDeclarationVisitor.getTypeDeclarationList())
		{
			res.add(typeDeclaration.getName().toString());
		} return res;
	}
	
	
	// gets the best name format for a class
	private String getNameClassOfInvokeMethod(MethodInvocation methodInvocation, TypeDeclaration typeDeclaration){
        String classInvokedMethodName = "";

        if (( methodInvocation.getExpression() != null) &&
                (methodInvocation.getExpression().resolveTypeBinding() != null)) {
            classInvokedMethodName+= methodInvocation.getExpression().resolveTypeBinding().getName();
        }
        else {
            classInvokedMethodName+= typeDeclaration.getName().getFullyQualifiedName();
        }
        return classInvokedMethodName;
    }

	// update the graph by adding the class read and updates the value of the edges
	private static void updateGraph(CompilationUnit parse) {
		//List<String> applicationClassesNames = getApplicationClassesNames(parse);
		
		TypeDeclarationVisitor typeDeclarationVisitor = new TypeDeclarationVisitor();
		parse.accept(typeDeclarationVisitor);

		for (TypeDeclaration typeDeclaration : typeDeclarationVisitor.getTypeDeclarationList()) {
			for (MethodDeclaration methodDeclaration : typeDeclaration.getMethods()) {
				MethodInvocationVisitor methodInvocationVisitor = new MethodInvocationVisitor();
				methodDeclaration.accept(methodInvocationVisitor);

				String classCalling = typeDeclaration.getName().toString();
				coupledGraph.updateWeightedEdge(classCalling);

				for (MethodInvocation methodInvocation : methodInvocationVisitor.getMethods()) {

					String classCalled = "";

					// if the expression is explicite
					if (methodInvocation.getExpression() != null) {
						if (methodInvocation.getExpression().resolveTypeBinding() != null) {
							classCalled = methodInvocation.getExpression().resolveTypeBinding().getName().toString();
						}
						// else, if
					} else if (methodInvocation.resolveMethodBinding() != null) {

						classCalled = methodInvocation.resolveMethodBinding().getDeclaringClass().toString();
						// if the called method is static
					} else {
						classCalled = methodDeclaration.getName().toString();
					}
					if (!classCalled.equals("")) {
						coupledGraph.updateWeightedEdge(classCalled);
						coupledGraph.updateWeightedEdge(classCalled, classCalling);
					}
				}

			}
		}
	}

	// calculates the coupling metric between two classes A and B
	public static float getCouplingMetric(String classA, String classB) {
		return coupledGraph.couplingClasses(classA, classB);
	}

	// getter on coupledGraph
	public static WeightedGraph getCoupledGraph() {
		return coupledGraph;
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