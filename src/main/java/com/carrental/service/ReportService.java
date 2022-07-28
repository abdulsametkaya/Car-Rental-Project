package com.carrental.service;

import com.carrental.domain.Car;
import com.carrental.domain.Reservation;
import com.carrental.domain.User;
import com.carrental.helper.ExcelReportHelper;
import com.carrental.repository.CarRepository;
import com.carrental.repository.ReservationRepository;
import com.carrental.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportService {
	
    UserRepository userRepository;
    
    CarRepository carRepository;
    
    ReservationRepository reservationRepository;
	
	
	public ByteArrayInputStream getUserReport() throws IOException {
		List<User> users= userRepository.findAll();
		return ExcelReportHelper.getUsersExcelReport(users);
	}
	
	
	public ByteArrayInputStream getCarReport() throws IOException {
		List<Car> cars= carRepository.findAll();
		return ExcelReportHelper.getCarsExcelReport(cars);
	}
	
	
	public ByteArrayInputStream getReservationReport() throws IOException {
		List<Reservation> reservations= reservationRepository.findAll();
		return ExcelReportHelper.getReservationExcelReport(reservations);
	}

}
