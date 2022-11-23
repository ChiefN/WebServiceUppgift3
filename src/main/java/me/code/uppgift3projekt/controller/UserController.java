package me.code.uppgift3projekt.controller;

import me.code.uppgift3projekt.dto.LoginRequestModel;
import me.code.uppgift3projekt.exception.UserAlreadyExistsException;
import me.code.uppgift3projekt.service.PostService;
import me.code.uppgift3projekt.service.TokenService;
import me.code.uppgift3projekt.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {

    private final TokenService tokenService;
    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    public UserController(TokenService tokenService, UserService userService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestModel userLogin) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.username(), userLogin.password()));
        return tokenService.generateToken(authentication);
    }

    @PostMapping ("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, Object> credentials){
        String username;
        String password;

        try{
            username = credentials.get("username").toString();
            password = credentials.get("password").toString();
        }catch (Exception e){
            return new ResponseEntity<String>("Invalid JsonBody. Format as: {username: ?????, password: ?????}", HttpStatus.BAD_REQUEST);
        }

        if(username.isBlank() || password.isBlank()){
            return new ResponseEntity<String>("Username and password can not be blank", HttpStatus.FORBIDDEN);
        }

        try {
            userService.register(username, password);
            return new ResponseEntity<String>("User successfully created", HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<String>("Username already exists", HttpStatus.FORBIDDEN);
        }
    }

}
