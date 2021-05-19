package server.db.repositories;

import java.util.List;
import server.db.entities.ScheduleFlight;

public interface ScheduleFlightRepository {
  List<ScheduleFlight> getArrivingFlights(String airport);
  List<ScheduleFlight> getDepartingFlights(String airport);
}
