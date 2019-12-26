package neo4j;

import java.util.Map;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;

public class Query1 implements MyQuery {

	@Override
	public String getQuery() {
		return "match shortestPath((a)-[c*]-(b)) " + "where a.name='Pola de Lena' and b.name='Gijón' "
				+ "return reduce (var=0,v1 in c | var+v1.avg_ride_time) as tiempo, extract(x in c|type(x)) as tipo ";
	}

	@Override
	public void printResult(Result result) {
		while (result.hasNext()) {
			Record r = result.next();
			Map<String, Object> map = r.asMap();
			for (String key : map.keySet()) {
				System.out.println("-> " + key + " = " + map.get(key));
			}
		}

	}

	@Override
	public String getDescripcion() {
		return "Shortest Path between Pola de Lena and Gijón";
	}

}
