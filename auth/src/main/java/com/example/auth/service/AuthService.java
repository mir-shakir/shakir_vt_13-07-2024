package com.example.auth.service;

import com.example.auth.model.Users;
import com.example.auth.repository.UsersRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

@Service
public class AuthService {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.token.validity}")
    private long jwtTokenValidity ;

    @Autowired
    private UsersRepository userRepository;

    public String generateAccessToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);
        long now = System.currentTimeMillis();
        long expiry = now + jwtTokenValidity ;
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(expiry))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public String login(String username, String password) {
        if (Objects.isNull(username) || Objects.isNull(password)) {
            return null;
        }
        Users user = userRepository.findByUsername(username);
        if (Objects.nonNull(user) && password.equals(user.getPass())) {
            return generateAccessToken(user.getName());
        }
        return null;
    }
}
