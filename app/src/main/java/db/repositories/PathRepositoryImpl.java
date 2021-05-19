package db.repositories;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import lombok.Cleanup;
import lombok.SneakyThrows;
import db.entities.Path;
import db.util.Util;

public class PathRepositoryImpl implements PathRepository {

  @Override
  @SneakyThrows
  public List<Path> getPaths(String departurePoint, String arrivalPoint, Date departureDate,
      int connections) {
    @Cleanup Connection connection = Util.getConnection();
    @Cleanup Statement statement = connection.createStatement();

    @Cleanup var resultSet = statement.executeQuery(String.format("""
            with recursive r (
            	departure_airport,
            	arrival_airport,
            	flight_path,
            	scheduled_departure,
            	scheduled_arrival,
            	flight_numbers,
              departures,
              arrivals)
            as (
            	select departure_airport,
            	arrival_airport,
            	array[ad.city],
            	scheduled_departure,
            	scheduled_arrival,
            	array[flight_id] :: int[],
              array[departure_airport]::char(3)[],
             	array[arrival_airport]::char(3)[]
            	from flights
            	join airports_data ad
            	on departure_airport = ad.airport_code
            	where
            	(departure_airport = '%s' or ad.city ->> 'en' = '%s')
            	and scheduled_departure::date = to_date('%s', 'YYYY-MM-DD')
                    
            	union all
                    
            	select rec.departure_airport,
            	f.arrival_airport,
            	(rec.flight_path || ad.city),
            	rec.scheduled_departure,
            	f.scheduled_arrival,
            	(rec.flight_numbers || f.flight_id) :: int[],
            	(rec.departures || f.departure_airport)::char(3)[],
             	(rec.arrivals || f.arrival_airport)::char(3)[]
            	from flights f
            	join r rec on f.departure_airport = rec.arrival_airport
            	join airports_data ad on f.arrival_airport = ad.airport_code
            	where
            	not ad.city = any(rec.flight_path)
            	and f.scheduled_departure > rec.scheduled_arrival
            	and f.scheduled_departure - rec.scheduled_arrival < interval '1 day'
            	and cardinality(rec.flight_path) <= %d
            ) select * from r
            join airports_data ad
            on arrival_airport = ad.airport_code
            where arrival_airport = '%s' or ad.city ->> 'en' = '%s';
            """, departurePoint, departurePoint,
        new SimpleDateFormat("yyyy-MM-dd").format(departureDate),
        connections, arrivalPoint, arrivalPoint));

    List<Path> res = new ArrayList<>();

    while (resultSet.next()) {
      Path path = new Path();
      path.setDeparture(((Timestamp) resultSet.getObject("scheduled_departure")));
      path.setArrival(((Timestamp) resultSet.getObject("scheduled_arrival")));
      // var fnos = (String[]) resultSet.getArray("flight_numbers").getArray();
      path.setFlights(Arrays.asList((Integer[]) resultSet.getArray("flight_numbers").getArray()));

      path.setDepartureAirports(
          Arrays.asList((String[]) resultSet.getArray("departures").getArray()));

      path.setDepartureAirports(
          Arrays.asList((String[]) resultSet.getArray("arrivals").getArray()));
      res.add(path);
    }

    return res;
  }
}
