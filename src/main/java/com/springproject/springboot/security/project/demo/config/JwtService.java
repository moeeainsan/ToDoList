/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.springproject.springboot.security.project.demo.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.springproject.springboot.security.project.demo.repository.TokenRepsitory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
 public class JwtService {

    private static final String SECRETE_KEY="73467997C23A1F546C85557448B4B22D63C5116DEC0AC90CE3E4F1C255278399";
    private final TokenRepsitory tokenRepsitory;


    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails) ;
    }
    public String generateToken(
        Map<String,Object> extraClaims,
        UserDetails userDetails
    ){
        return Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
        .signWith(getSignInKey(),SignatureAlgorithm.HS256)
        .compact();
    }


    public boolean isTokenValid(String token,UserDetails userDetails){

        final String username=extractUserName(token);
         
        //logout atawtvalidtokenphit ma phit sit
        boolean isValidToken = tokenRepsitory.findByToken(token)
                                .map(t->!t.isLoggedOut()).orElse(false);

        return (username.equals(userDetails.getUsername())&& !TokenExpired(token) && isValidToken );

         }

    private boolean TokenExpired(String token) {
    return ((Date) extractExpiration(token)).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


public  String extractUserName(String token) { 
      return extractClaim(token, Claims::getSubject);
    }

     //T want to return type which is claim resolver or claims
     public <T> T extractClaim(String token,Function<Claims,T> claimsResolver){
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){

        return Jwts.parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes=Decoders.BASE64.decode(SECRETE_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
