import db.util.Util;
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
        CREATE TABLE flight_price (
          id int NOT NULL PRIMARY KEY,
          departure_airport_code char(3) NOT NULL,
          arrival_airport_code char(3) NOT NULL,
          price numeric(10, 2) NOT NULL,
          fare_conditions varchar(10) NOT NULL
        );
        """
    );

    @Cleanup var res = statement.executeQuery("""
        SELECT departure_airport, arrival_airport, fare_conditions, AVG(amount)
        FROM ticket_flights tf
        JOIN flights fl
        ON tf.flight_id = fl.flight_id
        GROUP BY fare_conditions, departure_airport, arrival_airport;
        """);

    while (res.next()) {
      System.out.println(res.getInt("avg"));
    }
  }
}
