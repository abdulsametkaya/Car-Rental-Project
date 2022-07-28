package com.carrental.repository;

import com.carrental.domain.Role;
import com.carrental.domain.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> 	{
	 Optional<Role> findByName(RoleType name);
}
