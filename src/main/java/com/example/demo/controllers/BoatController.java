package com.example.demo.controllers;

import com.example.demo.models.Boat;
import com.example.demo.services.BoatService;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.List;

@RestController
@RequestMapping(path= "/boat")
public class BoatController {

    private final BoatService boatService;

    public BoatController(BoatService boatService){
        this.boatService = boatService;
    }

    @GetMapping
    public List<Boat> getBoats(){
        return boatService.getBoats();
    }

    @PostMapping
    public void newBoat(@RequestBody Boat boat){
        boatService.addBoat(boat);
        
    }

}
