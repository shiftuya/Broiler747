package server.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import server.constants.Mappings;
import server.db.entities.Booking;
import server.db.repositories.BookingRepository;

import java.io.InvalidObjectException;

@RestController
@RequestMapping(value = "/", produces = "application/json")
public class BookingRestController {

    @Autowired private BookingRepository bookingRepository;

    @GetMapping(Mappings.BOOKING)
    public String getBooking(@RequestBody String body) {
        Booking booking;
        try {
            booking = new Gson().fromJson(body, Booking.class);
            booking.validate();
        } catch (JsonSyntaxException | InvalidObjectException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return bookingRepository.saveBooking(booking);
    }
}
