package com.example.demo.controllers;

import com.example.demo.dto.BoatResponse;
import com.example.demo.models.Boat;
import com.example.demo.models.User;
import com.example.demo.services.BoatService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/all")
    public List<BoatResponse> getAllBoats(){
        return boatService.getBoats();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/all/{boat_id}")
    public BoatResponse getAllBoat(@PathVariable Long boat_id){
        return boatService.getBoat(boat_id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<BoatResponse> getBoats(){
        return boatService.getBoats();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public BoatResponse newBoat(@RequestBody Boat boat){
        //to get the auth user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //add user to the boat - .getPrincipal() returns an Object of the type User
        boat.setUser((User)authentication.getPrincipal());

        BoatResponse br = boatService.addBoat(boat);
        return br;
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping(path = "{boat_id}")
    public void deleteBoat(@PathVariable Long boat_id){
        boatService.deleteBoat(boat_id);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping(path = "{boat_id}")
    public BoatResponse updateBoat(@PathVariable Long boat_id, @RequestBody Boat boat){
        return boatService.updateBoat(boat_id,boat);
    }

}
