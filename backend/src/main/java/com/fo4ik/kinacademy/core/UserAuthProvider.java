package com.fo4ik.kinacademy.core;

import com.fo4ik.kinacademy.dto.user.UserDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;


//JWT token config
@RequiredArgsConstructor
@Component
public class UserAuthProvider {

    /*@Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    //Encode secret key
    @PostConstruct
    public void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    //Create token for user by User first name, surname and login
    public String createToken(UserDto userDto) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3_600_000); // 1 hour
        return JWT.create()
                .withIssuer(userDto.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("firstName", userDto.getFirstName())
                .withClaim("surname", userDto.getSurname())
                .sign(Algorithm.HMAC256(secretKey)); //Encode token by special algorithm
    }

    //Check token for valid and return Authentication
    public Authentication validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey); //Decode token by special algorithm

            JWTVerifier verifier = JWT.require(algorithm).build(); // Create verifier for token validation

            DecodedJWT jwt = verifier.verify(token);

            UserDto userDto = UserDto.builder()
                    .username(jwt.getIssuer())
                    .firstName(jwt.getClaim("firstName").asString())
                    .surname(jwt.getClaim("surname").asString())
                    .build();

            return new UsernamePasswordAuthenticationToken(userDto, null, Collections.emptyList());
        } catch (Exception e) {
            return null;
        }
    }*/
}
