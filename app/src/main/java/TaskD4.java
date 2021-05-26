import server.db.util.Util;
import java.sql.Connection;
import java.sql.Statement;
import lombok.Cleanup;
import lombok.SneakyThrows;

public class TaskD4 {

  @SneakyThrows
  public static void main(String[] args) {
    @Cleanup Connection connection = Util.getConnection();
    @Cleanup Statement statement = connection.createStatement();

    statement.execute("""  
      DROP TABLE IF EXISTS flight_price;
      CREATE TABLE flight_price AS (
      SELECT departure_airport, arrival_airport, fare_conditions, AVG(amount) amount
        FROM ticket_flights tf
        JOIN flights fl
        ON tf.flight_id = fl.flight_id
        GROUP BY fare_conditions, departure_airport, arrival_airport
		   );
       """
    );
  }
}
