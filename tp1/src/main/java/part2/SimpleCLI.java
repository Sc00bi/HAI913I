package part2;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class SimpleCLI {
	public final static DecimalFormat df = new DecimalFormat("0.00"); // import java.text.DecimalFormat;
	public static String projectPath = "C:\\Users\\Scooby\\Documents\\Master\\913\\ProjetTest";
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
				printQuestion3FromVisitor();
				printQuestion3FromMap();
				break;
			case 4:
				printQuestion4();
				break;
			case 5:
				printQuestion5();
				break;
			case 6:
				printQuestion6();
				break;
			case 7:
				printQuestion7();
				break;
			case 8:
				printQuestion8();
				break;
			case 9:
				printQuestion9();
				break;
			case 10:
				printQuestion10();
				break;
			case 11:
				printQuestion11();
				break;
			case 12:
				printQuestion12();
				break;
			case 13:
				printQuestion13();
				break;
			case 14:
				printQuestion14();
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
		stringBuilder.append("1  - Le nombre de classes de l'application.\n");
		stringBuilder.append("2  - Le nombre de lignes de code de l'application.\n");
		stringBuilder.append("3  - Le nombre de méthodes de l'application.\n");
		stringBuilder.append("4  - Le nombre total de packages de l'application.\n");
		stringBuilder.append("5  - Le nombre moyen de méthodes par classes.\n");
		stringBuilder.append("6  - Le nombre moyen de ligne de code par méthode.\n");
		stringBuilder.append("7  - Le nombre moyen d'attributs par classe.\n");
		stringBuilder.append("8  - Les 10% des classes qui possèdent le plus de méthode.\n");
		stringBuilder.append("9  - Les 10% des classes qui possèdent le plus d'attributs.\n");
		stringBuilder.append("10 - Les classes qui font partie en même temps des deux catégories précédentes.\n");
		stringBuilder.append("11 - Les classes qui possèdent plus de X méthodes.\n");
		stringBuilder.append(
				"12 - Les 10% des méthodes qui possèdent le plus grand nombre de lignes de code pour chaque classe.\n");
		stringBuilder
				.append("13 - Le nombre maximal de paramètres par rapport à toutes les méthodes de l'application.\n");
		stringBuilder.append("=== EXERCICE 2 ===\n");
		stringBuilder.append("14 - Affichage du graphe d'appel de l'application en chaîne de caractères.\n");
		//stringBuilder.append("15 - Où trouver mon graphe d'appel en .png ?");
		stringBuilder.append("=== OPTIONS ===\n");
		stringBuilder.append("0  - Quitter.\n");

		System.out.println(stringBuilder.toString());
	}

	// print introduction
	public static void printIntroduction() {
		System.out.println(
				"Cette CLI permet d'analyser statiquement un projet dans le cadre de l'UE : \n        HAI913I - \"Evolution et restructuration logicielle\".\n");
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

	// print question 1 answer
	public static void printQuestion1() {
		System.out.println("Le nombre de classes de l'application est : " + StaticAnalysis.getNumberOfClassesCounter());
	}

	// print question 2 answer
	public static void printQuestion2() {
		System.out.println("En utilisant le \"LinesVistor\", le nombre de lignes de l'application est : "
				+ StaticAnalysis.getTotalNumberOfLinesCounter());
	}

	// print question 3 answer
	public static void printQuestion3FromMap() {
		System.out.println("Le nombre total de méthodes de l'application en parcourant notre Map est : "
				+ StaticAnalysis.getTotalNumberOfMethods());
	}

	// print question 3 answer (bis)
	public static void printQuestion3FromVisitor() {
		System.out.println("Le nombre total de méthodes de l'application en utilisant TypeVisitor est : "
				+ StaticAnalysis.getNumberOfMethodsCounter());
	}

	// print question 4 answer
	public static void printQuestion4() {
		System.out.println("Le nombre total de packages de l'application est :" + StaticAnalysis.getNumberOfPackages());
	}

	// print question 5 answer
	public static void printQuestion5() {
		System.out.println(
				"Le nombre moyen de méthodes par classe est : " + df.format(StaticAnalysis.averageNumberOfMethods()));
	}

	// print question 6 answer
	public static void printQuestion6() {
		System.out.println(
				"Le nombre moyen de lignes par méthodes est : " + df.format(StaticAnalysis.averageAttributesNumber()));
	}

	// print question 7 answer
	public static void printQuestion7() {
		System.out.println(
				"Le nombre moyen d'attributs par classe est : " + df.format(StaticAnalysis.averageAttributesNumber()));
	}

	// print question 8 answer
	public static void printQuestion8() {
		System.out.println("Les 10% des classes contenant le plus de méthodes sont : ");
		HashMap<TypeDeclaration, Integer> top10 = (HashMap<TypeDeclaration, Integer>) StaticAnalysis
				.top10PourcentClassesByMethods();
		for (TypeDeclaration key : top10.keySet()) {
			System.out.println("    Classe [" + key.getName() + "] : " + top10.get(key) + " méthodes");
		}
	}

	// print question 9 answer
	public static void printQuestion9() {
		HashMap<TypeDeclaration, Integer> top10 = (HashMap<TypeDeclaration, Integer>) StaticAnalysis
				.top10PourcentClassesByAttributes();
		System.out.println("Les 10% des classes contenant le plus d'attributs sont : ");
		for (TypeDeclaration key : top10.keySet()) {
			System.out.println("    Classe [" + key.getName() + "] : " + top10.get(key) + " attributs");
		}
	}

	// print question 10 answer
	public static void printQuestion10() {
		ArrayList<TypeDeclaration> top10 = StaticAnalysis.top10PourcentAttributesAndMethodsIntersection();
		if (top10.isEmpty()) {
			System.out.println("Aucune classe ne fait partie de cette intersection.");
		} else {
			System.out.println(
					"Les classes faisant partie de l'intersection des 10% par nombre d'attributs et par nombre de méthodes sont : ");
			for (TypeDeclaration key : top10) {
				System.out.println("    Classe [" + key.getName() + "] : "
						+ StaticAnalysis.top10PourcentClassesByAttributes().get(key) + " attributs et "
						+ StaticAnalysis.top10PourcentClassesByMethods().get(key) + " méthodes");
			}
		}
	}

	// print question 11 answer
	public static void printQuestion11() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Entrez un nombre : ");
		int x = scanner.nextInt();
		HashMap<TypeDeclaration, Integer> mapX = (HashMap<TypeDeclaration, Integer>) StaticAnalysis.classesXMethods(x);
		if (mapX.isEmpty()) {
			System.out.println("Aucune classe de ce projet n'a plus de " + x + " méthodes.");
		} else {
			for (TypeDeclaration key : mapX.keySet()) {
				System.out.println("Classe : [" + key.getName() + "] : " + mapX.get(key) + " méthodes");
			}
		}
	}

	// print question 12 answer
	public static void printQuestion12() {
		Map<TypeDeclaration, Map<MethodDeclaration, Integer>> map = StaticAnalysis.methodsTopTen();
		System.out.println("Voyons pour chaque classe quelles sont les méthodes contenant le plus de ligne : ");
		for (TypeDeclaration className : map.keySet()) {
			System.out.println("    [" + className.getName() + "] :");
			for (MethodDeclaration md : map.get(className).keySet()) {
				System.out.println("        " + md.getName() + " : " + map.get(className).get(md) + " lignes");
			}
		}
	}

	// print question 13 answer
	public static void printQuestion13() {
		System.out.println(StaticAnalysis.maximumNumberOfParameters());
	}
	
	// print question 14 answer
	public static void printQuestion14() {
		System.out.println(StaticAnalysis.getCallGraph());
	}
}
