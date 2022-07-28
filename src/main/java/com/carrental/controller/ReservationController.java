package com.carrental.controller;

import com.carrental.dto.ReservationDTO;
import com.carrental.dto.request.ReservationRequest;
import com.carrental.dto.request.ReservationUpdateRequest;
import com.carrental.dto.response.CarAvailabilityResponse;
import com.carrental.dto.response.GRResponse;
import com.carrental.dto.response.ResponseMessage;
import com.carrental.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/reservations")
public class ReservationController {

	private ReservationService reservationService;
	
	
	/*
	 * {
    "pickUpTime":"07/16/2022 20:00:00",
    "dropOffTime":"07/16/2022 23:00:00",
    "pickUpLocation":"Ankara",
    "dropOffLocation":"Ankara"
}
	 */
	
	//http://localhost:8080/reservations/add?carId=1
	
	@PostMapping("/add")
	@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	public ResponseEntity<GRResponse> makeReservation(HttpServletRequest request, @RequestParam(value="carId") Long carId,
													  @Valid @RequestBody ReservationRequest reservationRequest){
		Long userId= (Long) request.getAttribute("id");
		reservationService.createReservation(reservationRequest, userId, carId);
		
		GRResponse response=new GRResponse();
		response.setMessage(ResponseMessage.RESERVATION_SAVED_RESPONSE_MESSAGE);
		response.setSuccess(true);
		
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	
	
	@PostMapping("/add/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<GRResponse> addReservation(@RequestParam(value="userId") Long userId,
													 @RequestParam(value="carId") Long carId,
													 @Valid @RequestBody ReservationRequest reservationRequest){
		
		reservationService.createReservation(reservationRequest, userId, carId);
		
		GRResponse response=new GRResponse();
		response.setMessage(ResponseMessage.RESERVATION_SAVED_RESPONSE_MESSAGE);
		response.setSuccess(true);
		
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	
	
	@PutMapping("/admin/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<GRResponse> updateReservation(@RequestParam(value="carId") Long carId,
														@RequestParam(value="reservationId") Long reservationId,
														@Valid @RequestBody ReservationUpdateRequest reservationUpdateRequest){
	
		reservationService.updateReservation(reservationId,carId,reservationUpdateRequest);
		
		GRResponse response=new GRResponse();
		response.setMessage(ResponseMessage.RESERVATION_UPDATE_RESPONSE_MESSAGE);
		response.setSuccess(true);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	//http://localhost:8080/reservations/auth?carId=1&pickUpDateTime=07/19/2022 11:00:00&dropOffDateTime=07/19/2022 14:00:00
	@GetMapping("/auth")
	@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	public ResponseEntity<GRResponse> checkCarIsAvailable(@RequestParam(value="carId") Long carId,
														@RequestParam(value="pickUpDateTime")
														@DateTimeFormat(pattern="MM/dd/yyyy HH:mm:ss") LocalDateTime pickUpTime,
														@RequestParam(value="dropOffDateTime")
															  @DateTimeFormat(pattern="MM/dd/yyyy HH:mm:ss") LocalDateTime dropOffTime
			){
		
		boolean isNotAvailable= reservationService.checkCarAvailability(carId, pickUpTime, dropOffTime);
		Double totalPrice = reservationService.getTotalPrice(carId, pickUpTime, dropOffTime);
		
		CarAvailabilityResponse response=new CarAvailabilityResponse(!isNotAvailable,totalPrice,
				ResponseMessage.CAR_AVAILABLE_MESSAGE, true);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	//http://localhost:8080/reservations/admin/5/auth
	@DeleteMapping("/admin/{id}/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<GRResponse> deleteReservation(@PathVariable Long id){
		reservationService.removeById(id);
		
		GRResponse response=new GRResponse();
		response.setMessage(ResponseMessage.RESERVATION_DELETED_RESPONSE_MESSAGE);
		response.setSuccess(true);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	
	//Admin bütün reservasyon bilgilerini getirmek için kullanıyor
	@GetMapping("/admin/all")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<ReservationDTO>> getAllReservations(){
		List<ReservationDTO> reservations= reservationService.getAllReservations();
		return ResponseEntity.ok(reservations);
	}
	
	
	//Admin bir reservasyon id ile reservasyon bilgisini döndürmek için kullanıyor.
	//http://localhost:8080/reservations/1/admin
	@GetMapping("/{id}/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id){
		ReservationDTO reservationDTO= reservationService.findById(id);
		return ResponseEntity.ok(reservationDTO);
	}
	
	
	//Bir userId ile gelerek user'a ait olan tüm reservasyonları döndürüyor
	//http://localhost:8080/reservations/admin/auth/all?userId=1
	@GetMapping("/admin/auth/all")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<ReservationDTO>> getAllUserReservations(@RequestParam(value="userId") Long userId){
		List<ReservationDTO> reservations= reservationService.findAllByUserId(userId);
		return ResponseEntity.ok(reservations);
	}
	
	
	//Customer yada admin rolüne sahip bir kullanıcı kendisine ait olan bir reservasyon bilgisini getiriyor.
	@GetMapping("/{id}/auth")
	@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	public ResponseEntity<ReservationDTO> getUserReservationById(HttpServletRequest request, @PathVariable Long id){
		Long userId= (Long) request.getAttribute("id");
		ReservationDTO reservationDTO = reservationService.findByIdAndUserId(id, userId);
		return ResponseEntity.ok(reservationDTO);
	}
	
	//Customer yada admin rolüne sahip bir kullanıcı kendisine ait olan bütün reservasyon bilgilerini getiriyor.
	//http://localhost:8080/reservations/auth/all
	@GetMapping("/auth/all")
	@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	public ResponseEntity<List<ReservationDTO>> getUserReservationsByUserId(HttpServletRequest request){
		Long userId= (Long) request.getAttribute("id");
		List<ReservationDTO> reservations = reservationService.findAllByUserId(userId);
		return ResponseEntity.ok(reservations);
	}
	
	

	
}
