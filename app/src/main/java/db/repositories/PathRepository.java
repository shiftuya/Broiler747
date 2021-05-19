package db.repositories;

import java.util.Date;
import java.util.List;
import db.entities.Path;

public interface PathRepository {
  List<Path> getPaths(String departurePoint, String arrivalPoint, Date departureDate, int connections);
}
