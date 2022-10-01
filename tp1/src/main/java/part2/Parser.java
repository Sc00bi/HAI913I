package part2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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
	public static final String projectPath = "C:\\Users\\Scooby\\Documents\\Master\\913\\ProjetTest";
	public static final String projectSourcePath = projectPath + "\\src";
	public static final String jrePath = "C:\\Program Files\\Java\\jre1.8.0_291\\lib\\rt.jar";

	public static void main(String[] args) throws IOException {

		// read java files
		final File folder = new File(projectSourcePath);
		ArrayList<File> javaFiles = listJavaFilesForFolder(folder);

		// counters
		int numberOfClasses = 0; // number of classes of the application
		int numberOfLines = 0; // number of Lines of the application
		int numberOfMethods = 0; // number of methods of the application
		
		// sets
		HashSet<String> packageDeclarations = new HashSet<>();

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
			numberOfClasses += StaticAnalysis.numberOfClasses(parse);

			// number of lines (Q1.1.2)
			numberOfLines += StaticAnalysis.numberOfLines(parse);
			
			// number of methods (Q1.1.3)
			numberOfMethods += StaticAnalysis.numberOfMethods(parse);
			
			// adding the packages declarations (Q1.1.4)
			packageDeclarations.addAll(StaticAnalysis.packagesDeclarations(parse));

		}
		
		// print the results
		System.out.println("Le nombre de lignes de code total est : " + numberOfLines);
		System.out.println("Le nombre de classes de l'application : " + numberOfClasses);
		System.out.println("Le nombre de méthodes de l'application : " + numberOfMethods);
		System.out.println("Le nombre de déclaration de package : " + packageDeclarations.size());
		System.out.println("Le nombre moyen de méthodes par classe : " + numberOfMethods/numberOfClasses);
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
}
