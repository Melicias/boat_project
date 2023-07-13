package com.example.demo.controllers;

import com.example.demo.dto.BoatResponse;
import com.example.demo.dto.ImageUploadResponse;
import com.example.demo.exceptions.ApiException;
import com.example.demo.models.Boat;
import com.example.demo.models.User;
import com.example.demo.requests.BoatRequest;
import com.example.demo.services.BoatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "Boat management CRUD", description = "Simple CRUD for boats and add/get an image of the boat.")
@RestController
@RequestMapping(path= "/boat")
@PreAuthorize("hasRole('USER')")
@SecurityRequirement(name = "token")
public class BoatController {

    private final BoatService boatService;

    public BoatController(BoatService boatService){
        this.boatService = boatService;
    }

    @Operation(
            summary = "Get all Boats (ADMIN)",
            description = "get all Boats from every user. Only ADMINS can perform this operation",
            tags = { "Boat management CRUD" })
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BoatResponse.class)), mediaType = "application/json"),
                    description = "Successfully retrieved boats"),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema(), mediaType = "application/json") }, description = "Forbidden access") })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/all")
    public List<BoatResponse> getAllBoats(){
        return boatService.getBoats();
    }

    @Operation(
            summary = "Get Boat by Id (ADMIN)",
            description = "get a Boat by Id from any user. Only ADMINS can perform this operation",
            tags = { "Boat management CRUD" })
    @Parameter(name = "id", description = "Boat id", example = "1")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content( schema = @Schema(implementation = BoatResponse.class), mediaType = "application/json"),
                    description = "Successfully retrieved a boat"),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema(), mediaType = "application/json") }, description = "Forbidden access"),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ApiException.class), mediaType = "application/json") }, description = "Boat not found") })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/all/{id}")
    public BoatResponse getAllBoat(@PathVariable Long id){
        return boatService.getBoat(id);
    }


    @Operation(
            summary = "Get all Boats owned by the user",
            description = "get all Boats owned by the user",
            tags = { "Boat management CRUD" })
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BoatResponse.class)), mediaType = "application/json"),
                    description = "Successfully retrieved boats"),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema(), mediaType = "application/json") }, description = "Forbidden access") })
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<BoatResponse> getBoats(){
        //to get the auth user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return boatService.getBoatsUser((User)authentication.getPrincipal());
    }


    @Operation(
            summary = "Get Boat by Id",
            description = "get a Boat by Id if the user owns it",
            tags = { "Boat management CRUD" })
    @Parameter(name = "id", description = "Boat id", example = "1")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content( schema = @Schema(implementation = BoatResponse.class), mediaType = "application/json"),
                    description = "Successfully retrieved a boat"),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema(), mediaType = "application/json") }, description = "Forbidden access"),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ApiException.class), mediaType = "application/json") }, description = "Boat not found") })
    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/{id}")
    public BoatResponse getBoat(@PathVariable Long id){
        //to get the auth user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return boatService.getBoatUser((User)authentication.getPrincipal(), id);
    }

    @Operation(
            summary = "Create Boat",
            description = "Create a Boat",
            tags = { "Boat management CRUD" })
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content( schema = @Schema(implementation = BoatResponse.class), mediaType = "application/json"),
                    description = "Successfully created a boat"),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema(), mediaType = "application/json") }, description = "Forbidden access"),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ApiException.class), mediaType = "application/json") }, description = "No name provided") })
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public BoatResponse newBoat(@RequestBody BoatRequest boatRequest){
        //to get the auth user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //add user to the boat - .getPrincipal() returns an Object of the type User
        Boat boat = new Boat(boatRequest.getName(),boatRequest.getDescription(),(User)authentication.getPrincipal());

        return boatService.addBoat(boat);
    }

    @Operation(
            summary = "Create Boat",
            description = "Create a Boat",
            tags = { "Boat management CRUD" })
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content( schema = @Schema(implementation = BoatResponse.class), mediaType = "application/json"),
                    description = "Successfully deleted a boat"),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema(), mediaType = "application/json") }, description = "Forbidden access"),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ApiException.class), mediaType = "application/json") }, description = "No name provided") })
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping(path = "{id}")
    public void deleteBoat(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boatService.deleteBoat(id, (User)authentication.getPrincipal());
    }

    @Operation(
            summary = "Update Boat",
            description = "Update a Boat name and description",
            tags = { "Boat management CRUD" })
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content( schema = @Schema(implementation = BoatResponse.class), mediaType = "application/json"),
                    description = "Successfully retrieved a boat"),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema(), mediaType = "application/json") }, description = "Forbidden access"),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ApiException.class), mediaType = "application/json") }, description = "Boat not found OR No name provided") })
    @PreAuthorize("hasRole('USER')")
    @PutMapping(path = "{id}")
    public BoatResponse updateBoat(@PathVariable Long id, @RequestBody BoatRequest boatrequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return boatService.updateBoat(id, boatrequest, (User)authentication.getPrincipal());
    }

    @Operation(
            summary = "Add an image to a Boat",
            description = "Add an image to a boat that the user owns",
            tags = { "Boat management CRUD" })
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content( schema = @Schema(implementation = ImageUploadResponse.class), mediaType = "application/json"),
                    description = "Image upload with success, the file name is: ..."),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }, description = "Forbidden access"),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ApiException.class), mediaType = "application/json") }, description = "Boat not found OR Something went wrong with the image upload.") })
    @PreAuthorize("hasRole('USER')")
    @PostMapping(path = "/image/{id}")
    public ImageUploadResponse uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ImageUploadResponse("Image upload with success, the file name is: " + boatService.uploadImage(id, file, (User)authentication.getPrincipal()));
    }

    @Operation(
            summary = "Get the Boat image",
            description = "Get the boat  image that the user owns",
            tags = { "Boat management CRUD" })
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content( schema = @Schema(), mediaType = "image/png"),
                    description = "Image"),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }, description = "Forbidden access"),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ApiException.class), mediaType = "application/json") }, description = "Boat not found") })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/image/{id}")
    public ResponseEntity<?>  getImageByName(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        byte[] image = boatService.getImage(id, (User)authentication.getPrincipal());

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(image);
    }

}
