package part2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.internal.utils.FileUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.internal.core.search.matching.TypeDeclarationLocator;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import visitors.MethodDeclarationVisitor;
import visitors.MethodInvocationVisitor;
import visitors.TypeDeclarationVisitor;
import visitors.VariableDeclarationFragmentVisitor;

public class Parser {
	public static final String projectPath = "C:\\Users\\Scooby\\Documents\\Master\\913\\hotelServiceIbis\\hotelServiceIbis";
	public static final String projectSourcePath = projectPath + "\\src";
	public static final String jrePath = "C:\\Program Files\\Java\\jre1.8.0_291\\lib\\rt.jar";

	public static void main(String[] args) throws IOException {

		// read java files
		final File folder = new File(projectSourcePath);
		ArrayList<File> javaFiles = listJavaFilesForFolder(folder);

		// counters
		int numberOfClasses = 0; // number of classes of the application

		//
		for (File fileEntry : javaFiles) {
			String content = FileUtils.readFileToString(fileEntry);
			// System.out.println(content);

			CompilationUnit parse = parse(content.toCharArray());

			// print methods info
			// printMethodInfo(parse);

			// print variables info
			// printVariableInfo(parse);

			// print method invocations
			// printMethodInvocationInfo(parse);

			// adding the number of classes in the file to the number of classes of the
			// application (Q1.1.1)
			numberOfClasses += numberOfClasses(parse);

			// number of lines
			numberOfLines(parse);

		}

		// print application number of classes (TD : Q.1.1.1)
		System.out.println("Le nombre de classes de l'application : " + numberOfClasses);
	}

	// read all java files from specific folder
	public static ArrayList<File> listJavaFilesForFolder(final File folder) {
		ArrayList<File> javaFiles = new ArrayList<File>();
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				javaFiles.addAll(listJavaFilesForFolder(fileEntry));
			} else if (fileEntry.getName().contains(".java")) {
				// System.out.println(fileEntry.getName());
				javaFiles.add(fileEntry);
			}
		}

		return javaFiles;
	}

	// create AST
	private static CompilationUnit parse(char[] classSource) {
		ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.6
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		parser.setBindingsRecovery(true);

		Map options = JavaCore.getOptions();
		parser.setCompilerOptions(options);

		parser.setUnitName("");

		String[] sources = { projectSourcePath };
		String[] classpath = { jrePath };

		parser.setEnvironment(classpath, sources, new String[] { "UTF-8" }, true);
		parser.setSource(classSource);

		return (CompilationUnit) parser.createAST(null); // create and parse
	}

	// navigate method information
	public static void printMethodInfo(CompilationUnit parse) {
		MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
		parse.accept(visitor);

		for (MethodDeclaration method : visitor.getMethodDeclarationList()) {
			System.out.println("Method name: " + method.getName() + " Return type: " + method.getReturnType2());
		}

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

	// returns the number of Classes of d'un fichier .java (Q 1.1.1)
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

	// Eclipse JDT : get start position pour le point de début d'une classe,
	// et getLineNumber() à partir de la start position pour obtenir le nombre de
	// lignes,
	// sur chaque éléments : on peut obtenir le nb de lignes de chq méthode

	// returns the number of lines of the application (Q 1.1.2)
	public static int numberOfLines(CompilationUnit parse) {
		TypeDeclarationVisitor typeVisitor = new TypeDeclarationVisitor();
		parse.accept(typeVisitor);
		int numberOfLines = 0;

		for (TypeDeclaration typeDeclaration : typeVisitor.getTypeDeclarationList()) {
			System.out.println(typeDeclaration.getName() + ", Lenght : " + typeDeclaration.getLength()
					+ ", StartPosition : " + typeDeclaration.getStartPosition() + ", NodeType : "
					+ typeDeclaration.getStartPosition());
			System.out.println("Parent.startPosition : " + typeDeclaration.getParent().getStartPosition()
					+ ", Parent.getLength : " + typeDeclaration.getParent().getLength() + ", NodeType : "
					+ typeDeclaration.getStartPosition());
			System.out.println("Root.startPosition : " + typeDeclaration.getRoot().getStartPosition()
					+ ", Root.getLength : " + typeDeclaration.getRoot().getLength() + ", NodeType : "
					+ typeDeclaration.getStartPosition());
			// System.out.println( typeDeclaration.getLength() +
			// typeDeclaration.getRoot().getStartPosition());
			// System.out.println(typeDeclaration.getLength() + " " +
			// typeDeclaration.getStartPosition());
			for (MethodDeclaration md : typeDeclaration.getMethods()) {
				System.out.println(md.getLength());
				System.out.println(md.getStartPosition());
			}
			
			// typeVisitor.
		}
		return 0;
	}
}
