package com.app.service;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.Repo.UserRepository;
import com.app.entity.UserInfo;

@Service
public class UserService implements UserDetailsService {

    private final ConcurrentMap<String, OtpRecord> otpCache = new ConcurrentHashMap<>();
    private static final long OTP_EXPIRATION_TIME_MS = TimeUnit.MINUTES.toMillis(5);

    @Autowired
    private UserRepository userRepository;

    public String sendOtp(UserInfo userInfo) {
        String otp = generateOtp();
        String otpMessage = "Your OTP is " + otp;

        System.out.println("Simulating OTP sending: " + otpMessage);

        otpCache.put(userInfo.getPhoneNumber(), new OtpRecord(otp, System.currentTimeMillis()));

        Optional<UserInfo> existingUser = userRepository.findByPhoneNumber(userInfo.getPhoneNumber());
        if (existingUser.isEmpty()) {
            userRepository.save(userInfo);
        }

        return "OTP sent successfully (simulated) to user mobile number.";
    }

    private String generateOtp() {
        int otp = (int) (Math.random() * 1000000);
        return String.format("%06d", otp);
    }

    public String verifyOtp(UserInfo userInfo) {
        OtpRecord record = otpCache.get(userInfo.getPhoneNumber());

        if (record == null) {
            return "No OTP found for this phone number.";
        }

        if (System.currentTimeMillis() - record.getTimestamp() > OTP_EXPIRATION_TIME_MS) {
            otpCache.remove(userInfo.getPhoneNumber());
            return "OTP has expired.";
        }

        if (!record.getOtp().equals(userInfo.getUserOtp())) {
            return "Invalid OTP.";
        }

        Optional<UserInfo> optionalUser = userRepository.findByPhoneNumber(userInfo.getPhoneNumber());

        if (optionalUser.isEmpty()) {
            return handleNewUser(userInfo.getPhoneNumber(), userInfo.getRole());
        } else {
            UserInfo user = optionalUser.get();
            if (!user.getRole().equals(userInfo.getRole())) {
                return "User role mismatch.";
            }
        }

        otpCache.remove(userInfo.getPhoneNumber());
        return "OTP validated successfully.";
    }

    private String handleNewUser(String phoneNumber, String role) {
        UserInfo newUser = new UserInfo();
        newUser.setPhoneNumber(phoneNumber);
        newUser.setRole(role);
        userRepository.save(newUser);
        return "New user registered successfully with phone number: " + phoneNumber;
    }

    private static class OtpRecord {
        private final String otp;
        private final long timestamp;

        public OtpRecord(String otp, long timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }

        public String getOtp() {
            return otp;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        UserInfo user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));

        return (UserDetails) user;
    }
}
