package com.pms.pmSystem.service.authentication;

import com.pms.pmSystem.repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pms.pmSystem.model.UserPrincipal;
import com.pms.pmSystem.model.Users;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = repo.findByUsername(username);

        if (users == null){
            System.out.println("User not found in database");
            throw new UsernameNotFoundException("User not found");
        }
        return new UserPrincipal(users);
    }
}
