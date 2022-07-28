package com.carrental.repository;


import com.carrental.domain.Car;
import com.carrental.dto.CarDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
	
	@Query("SELECT new com.carrental.dto.CarDTO(car)  FROM Car car")
	Page<CarDTO> findAllCarWithPage(Pageable pageable);

}
