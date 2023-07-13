package com.example.demo.controllers;

import com.example.demo.exceptions.ApiException;
import com.example.demo.requests.AuthenticationRequest;
import com.example.demo.dto.AuthenticationResponse;
import com.example.demo.requests.RegisterRequest;
import com.example.demo.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(
            summary = "Create an account",
            description = "Create an account and get the access token")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content( schema = @Schema(implementation = AuthenticationResponse.class), mediaType = "application/json"),
                    description = "Token"),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }, description = "Forbidden access"),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = ApiException.class), mediaType = "application/json") }, description = "User with this email already exists!") })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @Operation(
            summary = "Login into an account",
            description = "Login to get the access token")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content( schema = @Schema(implementation = AuthenticationResponse.class), mediaType = "application/json"),
                    description = "Token"),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }, description = "Forbidden access")})
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
