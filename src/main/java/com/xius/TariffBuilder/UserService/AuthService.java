package com.xius.TariffBuilder.UserService;

import java.security.MessageDigest;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xius.TariffBuilder.Entity.User;
import com.xius.TariffBuilder.UserRepository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public boolean loginUser(String username, String password, String networkName) {

        String encryptedPassword = encryptPassword(password);

        return userRepository.findByLoginIdAndPasswordAndNetworkDisplay(username, encryptedPassword, networkName)
                .isPresent();
    }

    // CHECK: Network exists
    public boolean isValidNetwork(String networkName) {
        return userRepository.existsByNetwork_NetworkDisplay(networkName);
    }

    // CHECK: Username exists under network
    public boolean isValidUsername(String username, String networkName) {
        return userRepository.existsByLoginIdAndNetwork_NetworkDisplay(username, networkName);
    }

    // Password correct for that user
    public boolean isValidPassword(String username, String password, String networkName) {

        String encryptedPassword = encryptPassword(password);

        Optional<User> userOpt = userRepository.findByLoginIdAndNetwork_NetworkDisplay(username, networkName);

        if (userOpt.isPresent()) {
            return userOpt.get().getPassword().equals(encryptedPassword);
        }

        return false;
    }

    // PASSWORD ENCRYPTION (SHA-1)
    public String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] result = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                sb.append(String.format("%02X", b));
            }

            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // added
    public User getUser(String username, String password, String network) {

        String encryptedPassword = encryptPassword(password);

        return userRepository
                .findByLoginIdAndPasswordAndNetworkDisplay(username, encryptedPassword, network)
                .orElse(null);
    }
}