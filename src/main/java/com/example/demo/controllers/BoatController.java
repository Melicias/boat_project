package com.example.demo.controllers;

import com.example.demo.models.Boat;
import com.example.demo.services.BoatService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path= "/boat")
@PreAuthorize("hasRole('USER')")
public class BoatController {

    private final BoatService boatService;

    public BoatController(BoatService boatService){
        this.boatService = boatService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<Boat> getBoats(){
        return boatService.getBoats();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public void newBoat(@RequestBody Boat boat){
        boatService.addBoat(boat);
        
    }

}
