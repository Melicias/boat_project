package com.example.demo.services;

import com.example.demo.dto.BoatResponse;
import com.example.demo.jpaRepositories.BoatRepository;
import com.example.demo.models.Boat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoatService {

    private final BoatRepository boatRepository;

    @Autowired
    public BoatService(BoatRepository boatRepository){
        this.boatRepository = boatRepository;
    }
    public List<BoatResponse> getBoats(){
        return toDTOs(boatRepository.findAll());
    }

    public BoatResponse addBoat(Boat boat) {
        if( boat.getName().isBlank()){
            throw new IllegalArgumentException("No name provided");
        }

        this.boatRepository.save(boat);
        return toDTO(boat);
    }

    //ADMIN request -> no need to worry about if this boats bellongs to a user or not
    public BoatResponse getBoat(Long boatId) {
        Optional<Boat> boat = boatRepository.findById(boatId);
        if(boat.isEmpty()){
            throw new IllegalArgumentException();
        }

        return toDTO(boat.get());
    }

    public void deleteBoat(Long boatId) {
        //missing verification to check if its from the user
        boatRepository.deleteById(boatId);
    }

    public BoatResponse updateBoat(Long boatId, Boat newBoat) {
        Optional<Boat> boatOpt = boatRepository.findById(boatId);
        if(boatOpt.isEmpty()){
            throw new IllegalArgumentException();
        }
        Boat boat = boatOpt.get();
        boat.setName(newBoat.getName());
        boat.setDescription(newBoat.getDescription());
        boatRepository.save(boat);

        return toDTO(boat);
    }





    public BoatResponse toDTO(Boat boat) {
        BoatResponse boatDTO = new BoatResponse(
                boat.getId(),
                boat.getName(),
                boat.getDescription(),
                boat.getUser().getEmail()
        );
        return boatDTO;
    }

    private List<BoatResponse> toDTOs(List<Boat> boats) {
        return boats.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
