package db.repositories;

import java.util.List;
import db.entities.ScheduleFlight;

public interface FlightRepository {
  List<ScheduleFlight> getArrivingFlights(String airport);
  List<ScheduleFlight> getDepartingFlights(String airport);
}
