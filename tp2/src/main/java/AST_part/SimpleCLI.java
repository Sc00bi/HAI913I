package AST_part;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;

import models.ICluster;
import models.WeightedGraph;

public class SimpleCLI {
	public final static DecimalFormat df = new DecimalFormat("0.00"); // import java.text.DecimalFormat;
	public static String projectPath = "C:\\Users\\Scooby\\Documents\\GitHub\\HAI912I\\tp1";
	public static String projectSourcePath = projectPath + "\\src";
	public static String jrePath = "C:\\Program Files\\Java\\jre1.8.0_291\\lib\\rt.jar";

	public static void main(String[] args) throws IOException, InterruptedException {
		
		Scanner scanner = new Scanner(System.in);
		boolean flagExit = false;
		int option;
		Parser parser = new Parser(projectPath, projectSourcePath, jrePath);

		// print introduction and start menu
		printIntroduction();

		while (!flagExit) {
			// a déplacer ...
			printQuestionsMenu();
			option = scanner.nextInt();
			parser.parse();
			switch (option) {
			case 0:
				flagExit = true;
				break;
			case 1:
				printQuestion1();
				break;
			case 2:
				printQuestion2();
				break;
			case 3:
				printQuestion3();
				break;
			case 4:
				printQuestion4();
				break;
			default:
				System.out.println(option);
				break;
			}

			System.out.println("Passer à la suite ? (Entier quelconque)");
			scanner.nextInt();
		}

	}

	// print menu options
	public static void printQuestionsMenu() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("=== EXERCICE 1 ===\n");
		stringBuilder.append("1  - Couplage de A et B\n");
		stringBuilder.append("2  - Graphe de couplage pondéré\n");
		stringBuilder.append("=== EXERCICE 2 ===\n");
		stringBuilder.append("3  - Affichage du regroupement hiérarchique \n");
		stringBuilder.append("4  - Identification des groupes de classes couplées \n");
		stringBuilder.append("=== OPTIONS ===\n");
		stringBuilder.append("0  - Quitter.\n");

		System.out.println(stringBuilder.toString());
	}

	// print introduction
	public static void printIntroduction() {
		System.out.println(
				"Cette CLI permet d'analyser statiquement avec JDT un projet dans le cadre de l'UE : \n        HAI913I - \"Evolution et restructuration logicielle\".\n");
		System.out.println("Ce TP a été réalisé par Tom BROS, M2 GL.\n");

		System.out.println("Dans un premier temps, veuiller choisir un projet à analyser : ");
		System.out.println("    1 - Analyser le projet par défaut fournit dans les ressources du projet.");
		System.out.println("    2 - Analyser un autre projet.");

		Scanner scanner = new Scanner(System.in);
		int option = scanner.nextInt();

		switch (option) {
		case 1:
			break;
		case 2:
			System.out.println("Entrez le projectPath de votre projet :");
			projectPath = scanner.nextLine();
			System.out.println("Entrez la position de la source au sein de ce projet :");
			projectSourcePath = projectPath + scanner.nextLine();
			System.out.println("Entrez le jrePath");
			jrePath = scanner.nextLine();
			break;
		}
	}
	
	// prints cli to answer question 1.1
	public static void printQuestion1()
	{
		Scanner scanner = new Scanner(System.in);
		
		System.out.println(StaticAnalysis.getCoupledGraph().printVertex());

		System.out.println("Veuillez choisir deux classes pour calculer le couplage :");
		System.out.println("Une première :");
		String className1 = scanner.nextLine();
		System.out.println("Une deuxième :");
		String className2 = scanner.nextLine();
		
		System.out.println("Le couplage entre ces deux classes vaut : \n" + StaticAnalysis.getCouplingMetric(className1, className2));
	}
	
	// print question 1.2 answer
	public static void printQuestion2()
	{
		System.out.println("Notre graphe de couplage est : ");
		System.out.println(StaticAnalysis.getCoupledGraph().toString());
	}
	
	// print question 2.1 answer
	public static void printQuestion3()
	{
		System.out.println(Clustering.clustering(StaticAnalysis.getCoupledGraph()));
	}
	
	// print question 2.2 answer
	public static void printQuestion4()
	{
		System.out.println("Les différents foyers de classes couplées sont :");
		WeightedGraph graph = StaticAnalysis.getCoupledGraph();
		for (ICluster cluster : Clustering.filterWithCp(Clustering.clustering(graph), 0, graph))
		{
			System.out.println("Groupe de classes couplées : ");
			System.out.println(cluster);
		}
	}

}
