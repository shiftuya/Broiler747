package db.repositories;

import db.entities.Booking;

public interface BookingRepository {

  String saveBooking(Booking booking);
}
