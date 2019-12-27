package queries;

import java.util.Map;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;

import neo4j.MainWindow;

public class Query5 implements MyQuery{
	private MainWindow mw;

	public Query5(MainWindow mw) {
		this.mw = mw;
	}

	@Override
	public String getQuery() {
		return "MATCH (n)-[r1:RAILWAY]-(m) " + 
				"WITH r1.line as line " + 
				"MATCH (a)-[r:RAILWAY]-(b) " + 
				"WHERE r.line = line " + 
				"WITH avg(r.price) as average, line as line " + 
				"MATCH (a)-[r:RAILWAY]-(b) " + 
				"WHERE (r.price) > average AND r.line = line " + 
				"RETURN  a.name,r.avg_ride_time,r.line,r.price,b.name,average " + 
				"LIMIT 25";
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
					str += "line: " +  map.get(key)+", ";
					break;
				case 3:
					str += "price: " +  map.get(key)+"€ ]-";
					break;
				case 4:
					str += "(" +  map.get(key)+") =>";
					break;
				case 5:
					str+=" Line Average: "+ String.format("%.2f",map.get(key))+" € \n";
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
		return "Return the paths between two adjacent nodes of the same lime whose price is higher than the average " + 
				"price of their line.";
	}
}
