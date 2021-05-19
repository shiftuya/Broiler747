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

    statement.execute("""
        create or replace function generate_id() returns char(6) as $$
        declare
        	done bool;
        	new_id char(6);
        begin
                
            done := false;
            WHILE NOT done LOOP
                new_id := lpad(upper(to_hex(floor(random() * 16777215) ::int)), 6, '0');
                done := NOT exists(SELECT book_ref FROM bookings1 WHERE book_ref=new_id);
            END LOOP;
            RETURN new_id;
                
        end;
        $$ language plpgsql;""");

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
