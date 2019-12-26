package neo4j;

import java.util.HashMap;
import java.util.Map;

public class Program {

	private Map<Integer, MyQuery> queries = new HashMap<>();
	private Connection c;

	public Program(String user, String password) {
		c = new Connection("bolt://localhost:7687", user, password);
		queries.put(1, new Query1());
	}

	private void runQuery(MyQuery query) {
		c.runQuery(query);
		try {
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void printQueries() {
		for (Integer intg : queries.keySet()) {
			System.out.println(intg + " -> " + queries.get(intg).getDescripcion());
		}

	}

	public void processOption(String option) {
		int number = -1;
		try {
			number = Integer.parseInt(option);
		} catch (NumberFormatException e) {
			System.out.println("Not a valid number");
			return;
		}
		if (queries.containsKey(number)) {
			runQuery(queries.get(number));
		}
		System.out.println("Not a valid number");
		return;
	}

}
