package db.entities;

import lombok.Data;

@Data
public class Booking {

  private Path path;
  private String passengerId;
  private String passengerName;
  private Contact contact;
  private String fareConditions;
}
