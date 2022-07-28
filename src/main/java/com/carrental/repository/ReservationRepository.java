package com.carrental.repository;


import com.carrental.domain.Car;
import com.carrental.domain.Reservation;
import com.carrental.domain.User;
import com.carrental.domain.enums.ReservationStatus;
import com.carrental.dto.ReservationDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	
	boolean existsByCarId(Car car);
	
	boolean existsByUserId(User user);
	
	
	List<ReservationDTO> findAllBy();
	
	Optional<ReservationDTO> findDTOById(Long id);
	
	List<ReservationDTO> findAllByUserId(User userId);
	
	Optional<ReservationDTO> findByIdAndUserId(Long id,User user);
	
	
	
	@Query("SELECT r FROM Reservation r "
			+ "JOIN FETCH Car cd on r.carId= cd.id WHERE "
			+ "cd.id=:carId and (r.status not in :status) and :pickUpTime BETWEEN r.pickUpTime and r. dropOffTime "
			+ "or "
			+ "cd.id=:carId and (r.status not in :status) and :dropOffTime BETWEEN r.pickUpTime and r. dropOffTime "
			+ "or "
			+ "cd.id=:carId and (r.status not in :status) and (r.pickUpTime BETWEEN :pickUpTime and :dropOffTime)")
	
	List<Reservation> checkCarStatus(@Param("carId") Long carId, @Param("pickUpTime") LocalDateTime pickUpTime, 
			@Param("dropOffTime") LocalDateTime dropOffTime, @Param("status") ReservationStatus[] status);
	

}
