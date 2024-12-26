package com.app.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.entity.UserInfo;

/**
 * Repository interface for performing CRUD operations on the UserInfo entity.
 * 
 * Extends JpaRepository to provide basic CRUD and JPA-related operations.
 */
@Repository
public interface UserRepository extends JpaRepository<UserInfo, Integer> {

    Optional<UserInfo> findByPhoneNumber(String phoneNumber);

}
