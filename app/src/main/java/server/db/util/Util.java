package server.db.util;

import java.sql.Connection;
import java.sql.DriverManager;
import lombok.SneakyThrows;

public class Util {
  static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/demo";
  static final String DB_USER = "postgres";
  static final String DB_PASSWORD = "admin";

  @SneakyThrows
  public static Connection getConnection() {
    return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
  }

}
