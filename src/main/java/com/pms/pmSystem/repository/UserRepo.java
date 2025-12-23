package com.pms.pmSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pms.pmSystem.model.Users;
import com.pms.pmSystem.projection.UserProjection;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {

    Users findByUsername(String username);

    List<UserProjection> findAllProjectedBy();

    UserProjection findProjectedByUsername(String username);
}
