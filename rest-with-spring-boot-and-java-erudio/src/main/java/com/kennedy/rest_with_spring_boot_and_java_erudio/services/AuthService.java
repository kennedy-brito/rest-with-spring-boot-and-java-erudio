package com.kennedy.rest_with_spring_boot_and_java_erudio.services;

import com.kennedy.rest_with_spring_boot_and_java_erudio.data.vo.v1.security.AccountCredentialsVO;
import com.kennedy.rest_with_spring_boot_and_java_erudio.data.vo.v1.security.TokenVO;
import com.kennedy.rest_with_spring_boot_and_java_erudio.model.User;
import com.kennedy.rest_with_spring_boot_and_java_erudio.repositories.UserRepository;
import com.kennedy.rest_with_spring_boot_and_java_erudio.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity signin(AccountCredentialsVO data){
        try{
            String username = data.getUsername();
            String password = data.getPassword();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            User user = userRepository.findByUsername(username);

            var tokenResponse = new TokenVO();

            if(user != null){
                tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());
            }else {
                throw new UsernameNotFoundException("Username " + username + " not found!");
            }
            return ResponseEntity.ok(tokenResponse);
        }catch (Exception e){
            throw new BadCredentialsException("Invalid username/password supplied!");
        }
    }

    public ResponseEntity refreshToken(String username, String refreshToken){

        User user = userRepository.findByUsername(username);

        var tokenResponse = new TokenVO();

        if(user != null){
            tokenResponse = tokenProvider.refreshToken(refreshToken);
        }else {
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }
        return ResponseEntity.ok(tokenResponse);

    }

}
