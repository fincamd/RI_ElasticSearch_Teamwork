package neo4j;

import java.util.Scanner;

public class Main {

	static Scanner in = new Scanner(System.in);

	public static void main(String[] args) {
		String user = null;
		while (user == null) {
			System.out.println("Introduce the neo4j username");
			user = askForOption();
		}
		String password = null;
		while (password == null) {
			System.out.println("Introduce the neo4j password");
			password = askForOption();
		}

		Program p = new Program(user, password);

		boolean exit = false;
		while (!exit) {
			System.out.println("Select a Query number: ");
			p.printQueries();
			String option = askForOption();
			if (option != null)
				p.processOption(option);
			System.out.println("Press a key to continue");
			askForOption();
		}

	}

	private static String askForOption() {
		String line = null;
		if (in.hasNext()) {
			line = in.nextLine();
		}
		return line;
	}

}
