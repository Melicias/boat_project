package com.example.demo.services;

import com.example.demo.jpaRepositories.BoatRepository;
import com.example.demo.models.Boat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoatService {

    private final BoatRepository boatRepository;

    @Autowired
    public BoatService(BoatRepository boatRepository){
        this.boatRepository = boatRepository;
    }
    public List<Boat> getBoats(){
        return boatRepository.findAll();
    }

    public void addBoat(Boat boat) {
        if( boat.getName().isBlank()){
            throw new IllegalArgumentException("No name provided");
        }
        this.boatRepository.save(boat);
    }
}
