package com.carrental.greenrent.controller;


import com.carrental.controller.UserController;
import com.carrental.dto.UserDTO;
import com.carrental.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

	@Mock
	UserService userService;
	
	@InjectMocks
	UserController underTest;
	
	
	@Test
	void getUserByIdTest() {
		UserDTO userDTO=new UserDTO();
		
		userDTO.setId(1L);
		userDTO.setEmail("a@email.com");
		
		HttpServletRequest request=mock(HttpServletRequest.class);
		
		when(userService.findById(1L)).thenReturn(userDTO);
		
		when(request.getAttribute("id")).thenReturn(1L);
		
		
		ResponseEntity<UserDTO> userResponse=underTest.getUserById(request);
		UserDTO actual=userResponse.getBody();
		
		assertAll(
				()->assertNotNull(actual),
				()->assertEquals(HttpStatus.OK, userResponse.getStatusCode()),
				()->assertEquals(1L, actual.getId())
				);		
	}
	
	
}
