package neo4j;

import java.util.logging.LogManager;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.exceptions.ServiceUnavailableException;

import queries.MyQuery;

public class Connection implements AutoCloseable {

	private final Driver driver;

	public Connection(String uri, String user, String password) {
		LogManager.getLogManager().reset();
		driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
		try {
			driver.verifyConnectivity();
		} catch (ServiceUnavailableException e) {
			throw new RuntimeException("Couldn't connect");
		}
	}

	@Override
	public void close() throws Exception {
		driver.close();
	}

	/**
	 * Given a MyQuery executes it and prints the results according to the given
	 * ones.
	 * 
	 * @param query
	 * @return
	 */
	public void runQuery(MyQuery query) {

		try (Session session = driver.session()) {

			session.writeTransaction(new TransactionWork<Void>() {
				@Override
				public Void execute(Transaction tx) {
					Result result = tx.run(query.getQuery());
					query.printResult(result);
					return null;
				}
			});
		}
	
	}

}
