package neo4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import queries.MyQuery;
import queries.Query1;
import queries.Query2;
import queries.Query3;
import queries.Query4;
import queries.Query5;
import queries.Query6;

public class Program {

	private Map<Integer, MyQuery> queries = new HashMap<>();
	private Connection c;
	

	public Program(String user, String password,MainWindow mw) {
		
		c = new Connection("bolt://localhost:7687", user, password);
		queries.put(5, new Query1(mw));
		queries.put(1, new Query2(mw));
		queries.put(2, new Query3(mw));
		queries.put(3, new Query4(mw));
		queries.put(4, new Query5(mw));
		queries.put(6, new Query6(mw));
	}

	private void runQuery(MyQuery query) {
		c.runQuery(query);
		
	}

	public void processOption(String option) {
		int number = -1;
		try {
			number = Integer.parseInt(option);
		} catch (NumberFormatException e) {
			throw new RuntimeException("Option given is not a number");
		}
		if (queries.containsKey(number)) {
			runQuery(queries.get(number));
		}
		else {
			throw new RuntimeException("Option given is not a number");
		}
		
	}

	/**
	 * Returns a matrix containing for each row a query that can be performed.
	 * Each row contains the id and the description of the query.
	 * @return - A String[n][2]
	 */
	public String[][] getQueries() {
		List<String[]> list = new ArrayList<>();
		for (Integer intg : queries.keySet()) {
			String[] row = new String[2];
			row[0]=intg.toString();
			row[1]=queries.get(intg).getDescripcion();
			list.add(row);
		}
		list.sort(new Comparator<String[]>() {

			@Override
			public int compare(String[] o1, String[] o2) {
				int id1 = Integer.parseInt(o1[0]);
				int id2 = Integer.parseInt(o2[0]);
				return id1-id2;
			}
			
		});
		
		String[][] result = new String[list.size()][2];
		
		for (int i = 0; i < result.length; i++) {
			result[i]=list.get(i);
		}
		
		return result;
	}

}
