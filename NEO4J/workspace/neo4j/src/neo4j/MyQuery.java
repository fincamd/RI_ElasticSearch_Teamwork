package neo4j;

import org.neo4j.driver.Result;

public interface MyQuery {

	public String getQuery();

	public void printResult(Result r);

	public String getDescripcion();

}
