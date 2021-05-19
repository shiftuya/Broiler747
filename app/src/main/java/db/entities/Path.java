package db.entities;

import java.sql.Timestamp;
import java.util.List;
import lombok.Data;

@Data
public class Path {
  List<Integer> flights;
  Timestamp departure;
  Timestamp arrival;
  List<String> departureAirports;
  List<String> arrivalAirports;
}
