package db.repositories;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import lombok.Cleanup;
import lombok.SneakyThrows;
import db.entities.Airport;
import db.util.Util;
import org.postgresql.geometric.PGpoint;

public class AirportRepositoryImpl implements AirportRepository {

  @Override
  @SneakyThrows
  public List<Airport> getAirports() {
    @Cleanup Connection connection = Util.getConnection();
    @Cleanup Statement statement = connection.createStatement();

    @Cleanup var resultSet = statement.executeQuery("""
        SELECT * FROM airports_data;
        """);
    List<Airport> list = new ArrayList<>();

    while (resultSet.next()) {
      Airport airport = new Airport();
      airport.setCity(resultSet.getString("city"));
      airport.setCode(resultSet.getString("airport_code"));
      airport.setName(resultSet.getString("airport_name"));
      airport.setTimezone(resultSet.getString("timezone"));
      airport.setCoordinates(((PGpoint) resultSet.getObject("coordinates")));
      list.add(airport);
    }

    return list;
  }

  @SneakyThrows
  @Override
  public List<Airport> getAirports(String city) {
    @Cleanup Connection connection = Util.getConnection();
    @Cleanup Statement statement = connection.createStatement();

    @Cleanup var resultSet = statement.executeQuery(String.format("""
        SELECT * FROM airports_data
        WHERE city ->> 'en' = '%s';
        """, city));
    List<Airport> list = new ArrayList<>();

    while (resultSet.next()) {
      Airport airport = new Airport();
      airport.setCity(resultSet.getString("city"));
      airport.setCode(resultSet.getString("airport_code"));
      airport.setName(resultSet.getString("airport_name"));
      airport.setTimezone(resultSet.getString("timezone"));
      airport.setCoordinates(((PGpoint) resultSet.getObject("coordinates")));
      list.add(airport);
    }

    return list;
  }
}
