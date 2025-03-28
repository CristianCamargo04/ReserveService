package com.ccamargo.reserve.repository;

import com.ccamargo.reserve.model.rol.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<RolEntity, Integer> {

    RolEntity findByAuthority(String authority);
}
