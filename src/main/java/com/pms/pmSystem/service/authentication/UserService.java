package com.pms.pmSystem.service.authentication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pms.pmSystem.model.Users;
import com.pms.pmSystem.projection.UserLoginProjection;
import com.pms.pmSystem.repository.UserRepo;

@Service
public class UserService {
    @Autowired
    UserRepo repo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    public List<UserLoginProjection> getAllUser() {
        return repo.findAllProjectedBy();
    }

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(Users users) {
        users.setPassword(encoder.encode(users.getPassword()));
        return repo.save(users);
    }

    public Users getUserById(int id){
        return repo.findById(id).orElse(new Users());
    }

    public String verify(Users users) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword()));

            if (authentication.isAuthenticated()) {
                UserLoginProjection userProjection = repo.findProjectedByUsername(users.getUsername());
                
                if (userProjection != null) {
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("username", users.getUsername());
                    claims.put("status", userProjection.getStatus());
                    claims.put("email", userProjection.getEmail());
                    claims.put("fname", userProjection.getFname());
                    
                    return jwtService.generateToken(userProjection.getId(), claims);
                }
                
                return jwtService.generateToken(userProjection.getId());
            }
            return "fail";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
