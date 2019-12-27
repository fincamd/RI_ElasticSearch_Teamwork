package queries;

import java.util.Map;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;

import neo4j.MainWindow;

public class Query4 implements MyQuery {

	private MainWindow mw;

	public Query4(MainWindow mw) {
		this.mw = mw;
	}

	@Override
	public String getQuery() {
		return "MATCH (n)-[r:RAILWAY|:BOAT|:BUSLINE]-(v) " + 
				"WHERE r.avg_ride_time > 19 " + 
				"RETURN type(r), r.line, avg(r.price)/avg(r.avg_ride_time) as euros_per_minute_linewise " + 
				"ORDER BY euros_per_minute_linewise DESC "+
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
					str += "-> (Type: " +  map.get(key);
					break;
				case 1:
					if(map.get(key)==null)
						str+=") -> ";
					else
						str += " - Line: " +  map.get(key) +") -> ";
					break;
				case 2:
					str +=   String.format("%.2f",map.get(key))+" € per min\n";
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
		return " Return the ratio between price and time spent on every public transport line ";
	}

}
