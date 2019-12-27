package queries;

import java.util.Collection;
import java.util.Map;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.types.Node;

import neo4j.MainWindow;

public class Query6 implements MyQuery {
	private MainWindow mw;

	public Query6(MainWindow mw) {
		this.mw = mw;
	}

	@Override
	public String getQuery() {
		return "MATCH path = (n)-[r1:RAILWAY]-(l)-[r2*1..3]-(t:Valley) " + 
				"WHERE l.name='Langreo' AND n.name =~ \"[aeiouAEIOU].*\" " + 
				"RETURN n, nodes(path), relationships(path) " + 
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
					Node n = (Node)map.get(key);
					String name = n.get("name").toString().replace("\"", "");
					str += "-> (" +  name+")-";
					break;
				case 1:
					Collection<Node> c = (Collection<Node>) map.get(key);
					
					for (Node node : c) {
						str+="("+node.get("name").toString().replace("\"", "")+")-";
					}
					
					break;
				case 2:
					str += "line: " +  map.get(key)+"\n";
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
		return "From every possible path between any node that is adjacent to Langreo by train " + 
				"and a valley return the departure node, the nodes in the path and the relationships traversed " + 
				"except those that start with a vowel";
	}
}
