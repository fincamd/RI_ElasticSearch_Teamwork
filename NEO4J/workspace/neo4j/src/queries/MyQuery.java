package queries;

import org.neo4j.driver.Result;

public interface MyQuery {

	/**
	 * Returns the query to be performed
	 * @return
	 */
	public String getQuery();

	/**
	 * Notifies the MainWindow to print the result given
	 * @param r
	 */
	public void printResult(Result r);

	/**
	 * Returns a brief description of what the query does
	 * @return
	 */
	public String getDescripcion();

}
