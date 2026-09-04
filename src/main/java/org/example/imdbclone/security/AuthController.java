package org.example.imdbclone.security;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
//    private final JwtService jwtService;
//
//    @PostMapping("/login")
//    public Map<String,String> login(@RequestBody LoginRequest loginRequest){
//        if("user".equals(loginRequest.username())&&"admin".equals(loginRequest.password())){
//            String token= jwtService.generateToken(loginRequest.username());
//            return Map.of("token", token);
//        } else{
//            throw new RuntimeException("Wrong login or password");
//        }
//    }
}
