package com.ccamargo.reserve.repository;

import com.ccamargo.reserve.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByEmail(String email);

    @Override
    Optional<UserEntity> findById(Integer integer);
}

