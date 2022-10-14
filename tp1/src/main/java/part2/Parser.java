package part2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

/*
 * 13 nombre maximal de paramètres par rapport à toutes les méthodes de l'application
 */
public class Parser {
	public static String projectPath = "C:\\Users\\Scooby\\Documents\\Master\\913\\ProjetTest";
	public static String projectSourcePath = projectPath + "\\src";
	public static String jrePath = "C:\\Program Files\\Java\\jre1.8.0_291\\lib\\rt.jar";

	public Parser(String projectPath, String projetSourcePath, String jrePath) {
		this.projectPath = projectPath;
		this.projectSourcePath = projetSourcePath;
		this.jrePath = jrePath;
	}

	public void parse() throws IOException {
		final File folder = new File(projectSourcePath);
		ArrayList<File> javaFiles = listJavaFilesForFolder(folder);

		// file reader
		for (File fileEntry : javaFiles) {
			String content = FileUtils.readFileToString(fileEntry);
			// System.out.println(content);

			CompilationUnit parse = parse(content.toCharArray());

			// collecting data on the file parsed
			StaticAnalysis.analyze(parse);
		}

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
		map.entrySet().stream().forEach(
				x -> System.out.println("( METHOD ) : " + x.getKey().getName() + "\t\t" + "( " + x.getValue() + " )"));
	}
}
