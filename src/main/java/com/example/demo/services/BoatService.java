package com.example.demo.services;

import com.example.demo.dto.BoatResponse;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.exceptions.IllegalArgumentException;
import com.example.demo.jpaRepositories.BoatRepository;
import com.example.demo.models.Boat;
import com.example.demo.models.ImageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
            throw new EntityNotFoundException("Boat not found");
        }

        return toDTO(boat.get());
    }

    public void deleteBoat(Long boatId) {
        //missing verification to check if its from the user
        Optional<Boat> boatOpt = boatRepository.findById(boatId);
        if(boatOpt.isEmpty()){
            throw new EntityNotFoundException("Boat not found");
        }
        boatRepository.deleteById(boatId);
    }

    public BoatResponse updateBoat(Long boatId, Boat newBoat) {
        Optional<Boat> boatOpt = boatRepository.findById(boatId);
        if(boatOpt.isEmpty()){
            throw new EntityNotFoundException("Boat not found");
        }
        Boat boat = boatOpt.get();
        boat.setName(newBoat.getName());
        boat.setDescription(newBoat.getDescription());
        boatRepository.save(boat);

        return toDTO(boat);
    }

    public String uploadImage(Long boatId, MultipartFile file) {
        Optional<Boat> boatOpt = boatRepository.findById(boatId);
        if(boatOpt.isEmpty()){
            throw new EntityNotFoundException("Boat not found");
        }
        Boat boat = boatOpt.get();

        try{
            ImageData img = new ImageData(file.getOriginalFilename(), file.getContentType(),ImageData.compressImage(file.getBytes()), boat);
            boat.setImage(img);
            boatRepository.save(boat);

            return img.getName();
        }catch(IOException e){

        }

        throw new IllegalArgumentException("Something went wrong with the image upload.");
    }


    public byte[] getImage(Long boatId) {
        Optional<Boat> boatOpt = boatRepository.findById(boatId);
        if(boatOpt.isEmpty()){
            throw new EntityNotFoundException("Boat not found");
        }
        Boat boat = boatOpt.get();
        byte[] image = ImageData.decompressImage(boat.getImage().getImageData());
        return image;
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
