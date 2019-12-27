package queries;

import java.util.Map;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;

import neo4j.MainWindow;

public class Query2 implements MyQuery {

	private MainWindow mw;

	public Query2(MainWindow mw) {
		this.mw = mw;
	}

	@Override
	public String getQuery() {
		return "MATCH (n:City) " + "WHERE n.inhabitants > 10000 " + "RETURN n.name "+"ORDER BY n.inhabitants DESC";
	}

	@Override
	public void printResult(Result result) {
		String str = "";
		while (result.hasNext()) {

			Record r = result.next();
			Map<String, Object> map = r.asMap();
			for (String key : map.keySet()) {
				str += "-> " +  map.get(key) + "\n";
			}
		}
		mw.setResult(str);

	}

	@Override
	public String getDescripcion() {
		return "Get all cities that have over 10000 inhabitants ordered by number of inhabitants";
	}

}
