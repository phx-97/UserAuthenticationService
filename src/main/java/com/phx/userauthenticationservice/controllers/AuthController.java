package com.phx.userauthenticationservice.controllers;

import com.phx.userauthenticationservice.dtos.LoginRequestDto;
import com.phx.userauthenticationservice.dtos.SignupRequestDto;
import com.phx.userauthenticationservice.dtos.UserDto;
import com.phx.userauthenticationservice.exceptions.InvalidPasswordException;
import com.phx.userauthenticationservice.exceptions.UserAlreadyExistException;
import com.phx.userauthenticationservice.models.User;
import com.phx.userauthenticationservice.repos.UserRepo;
import com.phx.userauthenticationservice.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    IAuthService authService;
    @Autowired
    private UserRepo userRepo;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignupRequestDto signupRequestDto) {
        if(signupRequestDto.getEmail() == null || signupRequestDto.getPassword() == null){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        try{
            User user = authService.signup(signupRequestDto.getEmail(), signupRequestDto.getPassword());
            return new ResponseEntity<>(from(user), HttpStatus.CREATED);
        }catch (UserAlreadyExistException e){
            throw new RuntimeException(e.getMessage());
        }

    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            User user = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
            if(user == null){
                return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(from(user), HttpStatus.OK);
        }catch (InvalidPasswordException exception){
            throw new RuntimeException(exception.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<UserDto> logout(){
        return null;
    }

    public UserDto from(User user){
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());

        return userDto;
    }
}


// RequestMapping -- know as endpoints

