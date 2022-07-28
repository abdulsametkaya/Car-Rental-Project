package com.carrental.dto.mapper;

import com.carrental.domain.Reservation;
import com.carrental.dto.request.ReservationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface ReservationMapper {
	Reservation reservationRequestToReservation(ReservationRequest reservationRequest);
}
