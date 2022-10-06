package part2;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;

public class CLIExercice1 {
	public final static DecimalFormat df = new DecimalFormat("0.00"); // import java.text.DecimalFormat;
	public static String projectPath = "C:\\Users\\Scooby\\Documents\\Master\\913\\ProjetTest";
	public static String projectSourcePath = projectPath + "\\src";
	public static String jrePath = "C:\\Program Files\\Java\\jre1.8.0_291\\lib\\rt.jar";

	public static void main(String[] args) throws IOException, InterruptedException {
		String projectPath = "C:\\Users\\Scooby\\Documents\\Master\\913\\ProjetTest";
		String projectSourcePath = projectPath + "\\src";
		String jrePath = "C:\\Program Files\\Java\\jre1.8.0_291\\lib\\rt.jar";

		Scanner scanner = new Scanner(System.in);
		boolean flagExit = false;
		int option;
		Parser parser = new Parser(projectPath, projectSourcePath, jrePath);

		// print introduction and start menu
		printIntroduction();

		while (!flagExit) {
			// a d�placer ...
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
			default:
				System.out.println(option);
				break;
			}
			
			System.out.println("Passer � la suite ? (N'importe quelle entr�e non vide)");
			scanner.nextInt();
		}

	}

	// print menu options
	public static void printQuestionsMenu() {
		System.out.println("1  - Le nombre de classes de l'application.");
		System.out.println("2  - Le nombre de lignes de code de l'application.");
		System.out.println("3  - Le nombre de m�thodes de l'application.");
		System.out.println("4  - Le nombre total de packages de l'application.");
		System.out.println("5  - Le nombre moyen de m�thodes par classes.");
		System.out.println("6  - Le nombre moyen de ligne de code par m�thode.");
		System.out.println("7  - Le nombre moyen d'attributs par classe.");
		System.out.println("8  - Les 10% des classes qui poss�dent le plus de m�thode.");
		System.out.println("9  - Les 10% des classes qui poss�dent le plus d'attributs.");
		System.out.println("10 - Les classes qui font partie en m�me temps des deux cat�gories pr�c�dentes.");
		System.out.println("11 - Les classes qui poss�dent plus de X m�thodes.");
		System.out.println(
				"12 - Les 10% des m�thodes qui poss�dent le plus grand nombre de lignes de code pour chaque classe.");
		System.out.println("13 - Le nombre maximal de param�tres par rapport � toutes les m�thodes de l'application.");
		System.out.println("0  - Autres Options (configuration, exit ...).");
	}

	// print introduction
	public static void printIntroduction() {
		System.out.println(
				"Cette CLI permet d'analyser statiquement un projet dans le cadre de l'UE : \n        HAI913I - \"Evolution et restructuration logicielle\".\n");
		System.out.println("Ce TP a �t� r�alis� par Tom BROS, M2 GL.\n");

		System.out.println("Dans un premier temps, veuiller choisir un projet � analyser : ");
		System.out.println("    1 - Analyser le projet par d�faut fournit dans les ressources du projet.");
		System.out.println("    2 - Analyser r�flexivement ce projet");
		System.out.println("    3 - Analyser un autre projet.");

		Scanner scanner = new Scanner(System.in);
		int option = scanner.nextInt();

		switch (option) {
		case 1:
			projectPath = "C:\\Users\\Scooby\\Documents\\Master\\913\\ProjetTest";
			projectSourcePath = projectPath + "\\src";
			jrePath = "C:\\Program Files\\Java\\jre1.8.0_291\\lib\\rt.jar"; // temporaire : A SUPPRIMER
			break;
		case 2:
			projectPath = "";
			projectSourcePath = projectPath + "\\src";
			break;
		case 3:
			projectPath = "";
			projectSourcePath = projectPath + "\\src";
			break;
		}
	}

	// print question 1 answer
	public static void printQuestion1() {
		System.out.println("Le nombre de classes de l'application est : " + StaticAnalysis.getNumberOfClassesCounter());
	}

	// print question 2 answer
	public static void printQuestion2() {

	}

	// print question 3 answer
	public static void printQuestion3FromMap() {
		System.out.println("Le nombre total de m�thodes de l'application en parcourant notre Map est : "
				+ StaticAnalysis.getTotalNumberOfMethods());
	}

	// print question 3 answer (bis)
	public static void printQuestion3FromVisitor() {
		System.out.println("Le nombre total de m�thodes de l'application en utilisant TypeVisitor est : "
				+ StaticAnalysis.getNumberOfMethodsCounter());
	}

	// print question 4 answer
	public static void printQuestion4() {
		System.out.println("Le nombre total de packages de l'application est :" + StaticAnalysis.getNumberOfPackages());
	}

	// print question 5 answer
	public static void printQuestion5() {
		System.out.println(
				"Le nombre moyen de m�thodes par classe est : " + df.format(StaticAnalysis.averageNumberOfMethods()));
	}

	// print question 6 answer
	public static void printQuestion6() {

	}

	// print question 7 answer
	public static void printQuestion7() {
		System.out.println(
				"Le nombre moyen d'attributs par classe est : " + df.format(StaticAnalysis.averageAttributesNumber()));
	}

	// print question 8 answer
	public static void printQuestion8() {
		System.out.println("Les 10% des classes contenant le plus de m�thodes sont : ");
		HashMap<String, Integer> top10 = (HashMap<String, Integer>) StaticAnalysis.top10PourcentClassesByMethods();
		for (String key : top10.keySet()) {
			System.out.println("    Classe [" + key + "] : " + top10.get(key) + " m�thodes");
		}
	}

	// print question 9 answer
	public static void printQuestion9() {
		HashMap<String, Integer> top10 = (HashMap<String, Integer>) StaticAnalysis.top10PourcentClassesByAttributes();
		System.out.println("Les 10% des classes contenant le plus d'attributs sont : ");
		for (String key : top10.keySet()) {
			System.out.println("    Classe [" + key + "] : " + top10.get(key) + " attributs");
		}
	}

	// print question 10 answer
	public static void printQuestion10() {
		HashMap<String, String> top10 = (HashMap<String, String>) StaticAnalysis
				.top10PourcentAttributesAndMethodsIntersection();
		if (top10.isEmpty()) {
			System.out.println("Aucune classe ne fait partie de cette intersection.");
		} else {
			System.out.println(
					"Les classes faisant partie de l'intersection des 10% par nombre d'attributs et par nombre de m�thodes sont : ");
			for (String key : top10.keySet()) {
				System.out.println("    Classe [" + key + "] : " + top10.get(key) + " attributs");
			}
		}
	}

	// print question 11 answer
	public static void printQuestion11() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Entrez un nombre : ");
		int x = scanner.nextInt();
		HashMap<String, Integer> mapX = (HashMap<String, Integer>) StaticAnalysis.classesXMethods(x);
		if (mapX.isEmpty())
		{
			System.out.println("Aucune classe de ce projet n'a plus de " + x + " m�thodes.");
		}
		else
		{
			for (String key : mapX.keySet())
			{
				System.out.println("Classe : [" + key + "] : " + mapX.get(key) + " m�thodes");
			}
		}
	}

	// print question 12 answer
	public static void printQuestion12() {

	}

	// print question 13 answer
	public static void printQuestion13() {

	}
}
