package com.phx.userauthenticationservice.services;

import com.phx.userauthenticationservice.exceptions.InvalidPasswordException;
import com.phx.userauthenticationservice.exceptions.UserAlreadyExistException;
import com.phx.userauthenticationservice.models.Session;
import com.phx.userauthenticationservice.models.State;
import com.phx.userauthenticationservice.models.User;
import com.phx.userauthenticationservice.repos.SessionRepo;
import com.phx.userauthenticationservice.repos.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    SessionRepo sessionRepo;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private SecretKey secretKey;

    @Override
    public User signup(String email, String password) throws UserAlreadyExistException {
        Optional<User> optionalUser = userRepo.findUserByEmail(email);
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistException("Email already registered!!");
        }

        User user = new User();
        user.setEmail(email);
        user.setState(State.ACTIVE);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepo.save(user);

        return user;
    }

    @Override
    public Pair<User,MultiValueMap<String,String>> login(String email, String password) throws InvalidPasswordException {
        Optional<User> optionalUser = userRepo.findUserByEmail(email);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
                throw  new InvalidPasswordException("Password is invalid");
            }
            // Generate plain token
//            String message = "{\n" +
//                "   \"email\": \"anurag@scaler.com\",\n" +
//                "   \"roles\": [\n" +
//                "      \"instructor\",\n" +
//                "      \"buddy\"\n" +
//                "   ],\n" +
//                "   \"expirationDate\": \"25thSept2024\"\n" +
//                "}";
//            byte[] contents = message.getBytes(StandardCharsets.UTF_8);

            Map<String,Object> claims =new HashMap<>();
            claims.put("user_id__",user.getId());
            claims.put("user_email",user.getEmail());
            claims.put("roles",user.getRoles());
            long timeInMillis = System.currentTimeMillis();
            claims.put("iat",timeInMillis);
            claims.put("exp",timeInMillis+86400000);
            claims.put("iss","salman");
            // Creating token with signature using HS256 algorithms

            //MacAlgorithm algorithm = Jwts.SIG.HS256; // algorithm name
            //SecretKey secretKey = algorithm.key().build(); // Generating secret key at runtime
            //String token = Jwts.builder().content(contents).signWith(secretKey).compact(); // Generating token with payload and secret
            String token = Jwts.builder().claims(claims).signWith(secretKey).compact();

            MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
            multiValueMap.add(HttpHeaders.SET_COOKIE, token);

            Pair<User,MultiValueMap<String,String>> pair = new Pair<>(user,multiValueMap);

            // Storing session in db
            Session session = new Session();
            session.setToken(token);
            session.setUser(user);
            session.setState(State.ACTIVE);
            sessionRepo.save(session);

            return pair;
        }

        return null;
    }

    @Override
    public Boolean validateToken(Long userId,String token) {
        Optional<Session> optionalSession = sessionRepo.findByTokenAndUser_Id(token,userId);
        if(!optionalSession.isPresent()){
            return false;
        }

        // Build the parser to decode the token
        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = jwtParser.parseSignedClaims(token).getBody();

        Long expiry = claims.get("exp", Long.class);
        Long currentTime = System.currentTimeMillis();

        if(currentTime > expiry){
            System.out.println("Token is expired");
            return false;
        }
        return true;
    }

    @Override
    public User lgout(String email) {
        return null;
    }
}
