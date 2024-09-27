package com.example.jwt_spring_security.controller;


import com.example.jwt_spring_security.model.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class TokenController {


    @PostMapping
    public String generate(@RequestBody final JwtUser jwtUser) {

        Claims claims = Jwts.claims()
                .subject(jwtUser.getUserName())
                .add("userId",String.valueOf(jwtUser.getId()))
                .add("role",jwtUser.getRole())
                .build();



        return Jwts.builder()
                .claims(claims)
                .signWith(SignatureAlgorithm.HS512, "HEARMESUBJECTSOFYMIRMYNAMEISERENJAEGERIAMADDRESSINGMYFELLOWSUBJECTSOFYMIRSPEAKINGTOYOUDIRECTLYTHROUGHTHEPOWEROFTHEFOUNDERALLTHEWALLSONTHEISLANDOFPARADISHAVECRUMBLEDTOTHEGROUNDANDTHELEGIONSOFTITANSBURIEDWITHINHAVEBEGUNTHEIRMARCH")
                .compact();

    }
}