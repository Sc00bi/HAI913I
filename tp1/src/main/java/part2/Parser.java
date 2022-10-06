package part2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

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

/*
 * 1 nombre de classes
 * 2 nombre de lignes
 * 3 nombres de méthodes total
 * 4 nombre total de paquets
 * 5 nombre moyen de methodes par classes
 * 6  nombre moyen de ligne de code par méthode
 * 7 nombre moyen d'attribut par classe - FAUT PEUT ETRE LES METTRE EN DOUBLE
 * 8 10% des classes qui ont le plus grand nombre de méthodes
 * 9 10% des classes le plus gd nombre d'attributs
 * 10 classes dans les deux catégories précédentes
 * 11classes possèdent plus de X méthodes
 * 12 10% des méthode qui possedent le plus de ligne de code pour chaque classe
 * 13 nombre maximal de paramètres par rapport à toutes les méthodes de l'application
 */
public class Parser {
	public static String projectPath = "C:\\Users\\Scooby\\Documents\\Master\\913\\ProjetTest";
	public static String projectSourcePath = projectPath + "\\src";
	public static String jrePath = "C:\\Program Files\\Java\\jre1.8.0_291\\lib\\rt.jar";
	
	private int numberOfClasses;
	
	public Parser(String projectPath, String projetSourcePath, String jrePath)
	{
		this.projectPath = projectPath;
		this.projectSourcePath = projetSourcePath;
		this.jrePath = jrePath;
	}
	
	public void parse() throws IOException
	{
		final File folder = new File(projectSourcePath);
		ArrayList<File> javaFiles = listJavaFilesForFolder(folder);
		
		// counters
		this.numberOfClasses = 0; // number of classes of the application
		
		//
		for (File fileEntry : javaFiles) {
			String content = FileUtils.readFileToString(fileEntry);
			// System.out.println(content);

			CompilationUnit parse = parse(content.toCharArray());
			this.numberOfClasses += StaticAnalysis.numberOfClasses(parse);
		}
	}

	public int getNumberOfClasses() {
		return numberOfClasses;
	}

	public void setNumberOfClasses(int numberOfClasses) {
		this.numberOfClasses = numberOfClasses;
	}

	public static void main(String[] args) throws IOException {

		// read java files
		final File folder = new File(projectSourcePath);
		ArrayList<File> javaFiles = listJavaFilesForFolder(folder);

		// counters
		int numberOfClasses = 0; // number of classes of the application
		int numberOfLines = 0; // number of Lines of the application
		int numberOfMethods = 0; // number of methods of the application
		int numberOfLinesInMethods = 0; // number of methods of the application written in a method
		int numberOfAttributes = 0; // number of attributes of the application
		
		// sets
		Set<String> packageDeclarations = new HashSet<>();
		
		// 
		Map<String, Integer> classesNumberOfMethods = new HashMap<>();	
		Map<String, Integer> classesNumberOfAttributes = new HashMap<>();	

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
			
			// number of lines for each methods (Q1.1.6)
			numberOfLinesInMethods += StaticAnalysis.linesInMethod(parse);
			
			// number of attributes for each file (Q1.1.7)
			numberOfAttributes += StaticAnalysis.numberOfAttributes(parse);
			
			// number of methods associated to a class name (Q1.1.8)
			classesNumberOfMethods.putAll(StaticAnalysis.classesNumberOfMethods(parse));
			
			// 1.1.9
			classesNumberOfAttributes.putAll(StaticAnalysis.classesNumberOfAttributes(parse));
			
		}
		// 
		classesNumberOfMethods = StaticAnalysis.getTenPourcentsClasses(classesNumberOfMethods, numberOfClasses);
		classesNumberOfAttributes = StaticAnalysis.getTenPourcentsClasses(classesNumberOfAttributes, numberOfClasses);
		
		// print the results
		System.out.println("Le nombre de lignes de code total est : " + numberOfLines);
		System.out.println("Le nombre de classes de l'application : " + numberOfClasses);
		System.out.println("Le nombre de méthodes de l'application : " + numberOfMethods);
		System.out.println("Le nombre de déclaration de package : " + packageDeclarations.size());
		System.out.println("Le nombre moyen de méthodes par classe : " + numberOfMethods/numberOfClasses);
		System.out.println("Le nombre moyen de ligne de code par méthode :"  + numberOfLinesInMethods/numberOfMethods);
		System.out.println("Le nombre moyen d'attributs par classe : " + numberOfAttributes/numberOfClasses);
		System.out.println("Les 10% de classes contenant le plus de méthodes sont : " + classesNumberOfMethods.keySet());
		System.out.println("Les 10% de classes contenant le plus d'attributs sont : " + classesNumberOfAttributes.keySet());
		
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
	
	public static void displayMapMethodInteger(Map<MethodDeclaration, Integer> map) {
        map
                .entrySet()
                .stream()
                .forEach(x -> System.out.println(
                        "( METHOD ) : " + x.getKey().getName() + "\t\t"  +
                                "( " + x.getValue() + " )")
                );
    }
}
