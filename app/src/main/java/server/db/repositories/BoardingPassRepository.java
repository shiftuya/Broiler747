package server.db.repositories;

import server.db.entities.BoardingPass;

public interface BoardingPassRepository {
  BoardingPass generateBoardingPass(String ticketNo, int flightId);
}
