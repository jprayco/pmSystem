package com.pms.pmSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pms.pmSystem.model.Users;
import com.pms.pmSystem.projection.UserLoginProjection;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {

    Users findByUsername(String username);

    List<UserLoginProjection> findAllProjectedBy();

    UserLoginProjection findProjectedByUsername(String username);
}
