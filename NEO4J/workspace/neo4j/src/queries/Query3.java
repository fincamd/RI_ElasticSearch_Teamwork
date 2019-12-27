package queries;

import java.util.Map;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;

import neo4j.MainWindow;

public class Query3 implements MyQuery {

	private MainWindow mw;

	public Query3(MainWindow mw) {
		this.mw = mw;
	}

	@Override
	public String getQuery() {
		return "MATCH (n)-[r:RAILWAY]->(o) " + 
				"WHERE r.line =\"C1\" " + 
				"RETURN n.name, r.avg_ride_time,r.price, o.name";
	}

	@Override
	public void printResult(Result result) {
		String str = "";
		while (result.hasNext()) {

			Record r = result.next();
			Map<String, Object> map = r.asMap();
			int ix=0;
			for (String key : map.keySet()) {
				switch(ix) {
				case 0:
					str += "-> (" +  map.get(key)+")-";
					break;
				case 1:
					str += "[avg_ride_time: " +  map.get(key)+", ";
					break;
				case 2:
					str += "price: " +  map.get(key)+"]-";
					break;
				case 3:
					str += "(" +  map.get(key)+")\n";
					ix=-1;
					break;
				}
				ix++;
			}
		}
		mw.setResult(str);

	}

	@Override
	public String getDescripcion() {
		return "Return all nodes that have a Railway with line C1";
	}

}
