package db.repositories;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import lombok.Cleanup;
import lombok.SneakyThrows;
import db.util.Util;

public class CityRepositoryImpl implements CityRepository {

  @Override
  @SneakyThrows
  public List<String> getCities() {
    @Cleanup Connection connection = Util.getConnection();
    @Cleanup Statement statement = connection.createStatement();

    @Cleanup var resultSet = statement.executeQuery("""
        SELECT DISTINCT city FROM airports_data;
        """);
    List<String> list = new ArrayList<>();

    while (resultSet.next()) {
      list.add(resultSet.getString("city"));
    }

    return list;
  }
}
