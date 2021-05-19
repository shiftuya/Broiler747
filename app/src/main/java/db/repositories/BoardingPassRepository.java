package db.repositories;

import db.entities.BoardingPass;

public interface BoardingPassRepository {
  BoardingPass generateBoardingPass(String ticketNo, int flightId);
}
