package com.carrental.controller;

import com.carrental.dto.CarDTO;
import com.carrental.dto.response.GRResponse;
import com.carrental.dto.response.ResponseMessage;
import com.carrental.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/car")
@AllArgsConstructor
public class CarController {
	
	private CarService carService;

	/*
	 * {
    "model": "bmw",
    "doors": 4,
    "seats": 4,
    "luggage": 450,
    "transmission": "automatic",
    "airConditioning": true,
    "age": 4,
    "pricePerHour": 400,
    "fuelType": "diesel"
}
	 */
	
	//http://localhost:8080/car/admin/2ada74bf-2099-45ff-a08a-36ca1712d18f/add
	
	@PostMapping("/admin/{imageId}/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<GRResponse> saveCar(@PathVariable String imageId,
											  @Valid @RequestBody CarDTO carDTO){
		
		carService.saveCar(carDTO, imageId);
		
		GRResponse response=new GRResponse();
		response.setMessage(ResponseMessage.CAR_SAVED_RESPONSE_MESSAGE);
		response.setSuccess(true);
		
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
	
	@GetMapping("/visitors/all")
	public ResponseEntity<List<CarDTO>> getAllCars(){
		List<CarDTO> carDTOs= carService.getAllCars();
		return ResponseEntity.ok(carDTOs);
	}
	
	//http://localhost:8080/car/visitors/2
	@GetMapping("/visitors/{id}")
	public ResponseEntity<CarDTO> getCarById(@PathVariable Long id){
		CarDTO carDTO= carService.findById(id);
		return ResponseEntity.ok(carDTO);
	}
	
	//http://localhost:8080/car/visitors/pages?page=0&size=5&sort=model&direction=ASC
	@GetMapping("/visitors/pages")
	public ResponseEntity<Page<CarDTO>> getAllWithPage(@RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam("sort") String prop,
			@RequestParam("direction") Direction direction){

     Pageable pageable=PageRequest.of(page, size, Sort.by(direction,prop)); 
	 Page<CarDTO> carPage= carService.findAllWithPage(pageable);
	
	 return ResponseEntity.ok(carPage);
	}
	//http://localhost:8080/car/admin/auth?id=1&imageId=2ada74bf-2099-45ff-a08a-36ca1712d18f
	@PutMapping("/admin/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<GRResponse> updateCar(@RequestParam("id") Long id,
			                                    @RequestParam("imageId") String imageId,
			                                    @Valid @RequestBody CarDTO carDTO){
		carService.updateCar(id, imageId, carDTO);
		GRResponse response=new GRResponse();
		response.setMessage(ResponseMessage.CAR_UPDATE_RESPONSE_MESSAGE);
		response.setSuccess(true);
		
		return ResponseEntity.ok(response);
	}
	
	//http://localhost:8080/car/admin/1/auth
	@DeleteMapping("/admin/{id}/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<GRResponse> deleteCar(@PathVariable Long id){
		carService.removeById(id);
		
		GRResponse response=new GRResponse();
		response.setMessage(ResponseMessage.CAR_DELETE_RESPONSE_MESSAGE);
		response.setSuccess(true);
		
		return ResponseEntity.ok(response);
	}
	
}
