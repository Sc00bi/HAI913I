package part2;

import java.io.IOException;
import java.util.Scanner;

public class CLIExercice1 {
	public static String projectPath = "C:\\Users\\Scooby\\Documents\\Master\\913\\ProjetTest";
	public static String projectSourcePath = projectPath + "\\src";
	public static String jrePath = "C:\\Program Files\\Java\\jre1.8.0_291\\lib\\rt.jar";

	public static void main(String[] args) throws IOException {
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
			//a déplacer ...
			printQuestionsMenu();
			option = scanner.nextInt();
			parser.parse();
			switch (option) {
			case 0:
				flagExit = true;
				break;
			case 1:
				printQuestion1(parser);
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				break;
			case 9:
				break;
			case 10:
				break;
			case 11:
				System.out.println("erreur");
				break;
			case 12:
				break;
			case 13:
				break;
			default:
				System.out.println(option);
				break;
			}
		}

	}

	// print menu options
	public static void printQuestionsMenu() {
		System.out.println("1  - Le nombre de classes de l'application.");
		System.out.println("2  - Le nombre de lignes de code de l'application.");
		System.out.println("3  - Le nombre de méthodes de l'application.");
		System.out.println("4  - Le nombre total de packages de l'application.");
		System.out.println("5  - Le nombre moyen de méthodes par classes.");
		System.out.println("6  - Le nombre moyen de ligne de code par méthode.");
		System.out.println("7  - Le nombre moyen d'attributs par classe.");
		System.out.println("8  - Les 10% des classes qui possèdent le plus de méthode.");
		System.out.println("9  - Les 10% des classes qui possèdent le plus d'attributs.");
		System.out.println("10 - Les classes qui font partie en même temps des deux catégories précédentes.");
		System.out.println("11 - Les classes qui possèdent plus de X méthodes.");
		System.out.println(
				"12 - Les 10% des méthodes qui possèdent le plus grand nombre de lignes de code pour chaque classe.");
		System.out.println("13 - Le nombre maximal de paramètres par rapport à toutes les méthodes de l'application.");
		System.out.println("0  - Autres Options (configuration, exit ...).");
	}

	// print introduction
	public static void printIntroduction() {
		System.out.println(
				"Cette CLI permet d'analyser statiquement un projet dans le cadre de l'UE : \n        HAI913I - \"Evolution et restructuration logicielle\".\n");
		System.out.println("Ce TP a été réalisé par Tom BROS, M2 GL.\n");

		System.out.println("Dans un premier temps, veuiller choisir un projet à analyser : ");
		System.out.println("    1 - Analyser le projet par défaut fournit dans les ressources du projet.");
		System.out.println("    2 - Analyser réflexivement ce projet");
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
	public static void printQuestion1(Parser parser) {
		System.out.println("Le nombre de classes de l'application est : " + parser.getNumberOfClasses());
	}
}
