package com.app.controller;

import com.app.dto.AuthResponse;
import com.app.entity.User;
import com.app.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.entity.UserInfo;
import com.app.service.UserService;

/**
 * Controller class that handles HTTP requests related to user operations such as sending and verifying OTPs.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;


    @GetMapping("/")
    public String test() {
        return "This is a welcome Message";
    }


    @PostMapping("/SendOtp")
    public String sendOtp(@RequestBody UserInfo userInfo) {
        return userService.sendOtp(userInfo);
    }

    @PostMapping("/VerifyOtp")
    public ResponseEntity<?> verifyOtp(@RequestBody UserInfo userInfo) {
        String verificationResult = userService.verifyOtp(userInfo);
        if (verificationResult.equals("OTP validated successfully.")) {
              return ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userInfo.getPhoneNumber(),userInfo.getRole())));
        } else {
            return ResponseEntity.status(401).body(verificationResult);
        }
    }
}


