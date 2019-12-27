package queries;

import java.util.Map;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;

import neo4j.MainWindow;

public class Query1 implements MyQuery {

	private MainWindow mw;
	
	public Query1(MainWindow mw) {
		this.mw=mw;
	}

	@Override
	public String getQuery() {
		return "MATCH shortestPath((a)-[c:BUSLINE|:RAILWAY|:BOAT*]-(b)) " + 
				"WHERE a.name='Pola de Lena' and b.name='Gijón' " + 
				"RETURN reduce (var=0,v1 in c | var+v1.avg_ride_time) as Total_Time, reduce(var = 0, v1 in c | var + v1.price) as Total_Price, extract(x in c|type(x)) as Types,extract(x in c|startnode(x).name) as InitialNodes,extract(x in c|endnode(x).name) as EndNodes " + 
				"LIMIT 25";
	}

	@Override
	public void printResult(Result result) {
		String str="";
		while (result.hasNext()) {
			
			
			Record r = result.next();
			Map<String, Object> map = r.asMap();
			
		}
		mw.setResult(str);
	}

	@Override
	public String getDescripcion() {
		return "Return the shortest path (including each of the nodes) in between Gijón and Pola de Lena, along with the total trip time and the amount paid";
	}

}
