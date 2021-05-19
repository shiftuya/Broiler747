package server.controllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.constants.Mappings;
import server.db.repositories.BoardingPassRepository;

@RestController
@RequestMapping(value = "/", produces = "application/json")
public class BoardingPassRestController {

    @Autowired private BoardingPassRepository boardingPassRepository;

    @GetMapping(Mappings.BOARDING_PASS)
    public String getBoardingPass(@RequestParam String ticketNo, @RequestParam int flightId) {
        return new Gson().toJson(boardingPassRepository.generateBoardingPass(ticketNo, flightId));
    }
}
