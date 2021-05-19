package db.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import lombok.Cleanup;
import lombok.SneakyThrows;
import db.entities.BoardingPass;
import db.util.Util;

public class BoardingPassRepositoryImpl implements BoardingPassRepository {

  @SneakyThrows
  @Override
  public BoardingPass generateBoardingPass(String ticketNo, int flightId) {

    @Cleanup Connection connection = Util.getConnection();
    @Cleanup PreparedStatement statement = connection.prepareStatement(String.format("""
            insert into boarding_passes1 (ticket_no, flight_id, boarding_no, seat_no) values (
            	'%s',
            	%d,
            	(select boarding_no from boarding_passes1
            where flight_id = 1
            order by boarding_no desc
            limit 1
            ) + 1,
            	(select seat_no from seats
            where aircraft_code = (select aircraft_code from flights where flight_id = %d) and fare_conditions = 
            (select fare_conditions from ticket_flights1 tf where tf.ticket_no = %s)
            and not exists (
            select 1 from boarding_passes1 b where seats.seat_no = b.seat_no
            and flight_id = %s) limit 1)
            )""", ticketNo, flightId, flightId, ticketNo, flightId),
        new String[]{"boarding_no", "seat_no"});

    statement.executeUpdate();

    @Cleanup var resultSet = statement.getGeneratedKeys();

    BoardingPass boardingPass = new BoardingPass();
    if (resultSet.next()) {
      boardingPass.setId(resultSet.getInt("boarding_no"));
      boardingPass.setSeat(resultSet.getString("seat_no"));
    } else {
      return null; // exception?
    }

    return boardingPass;
  }
}
