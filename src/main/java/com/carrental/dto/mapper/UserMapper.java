package com.carrental.dto.mapper;

import com.carrental.domain.User;
import com.carrental.dto.UserDTO;
import com.carrental.dto.request.AdminUserUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
	
	UserDTO userToUserDTO(User user);
	List<UserDTO> map(List<User> user);
	
	
	@Mapping(target="id", ignore=true)
	@Mapping(target="roles",ignore=true)
	User adminUserUpdateRequestToUser(AdminUserUpdateRequest request);

}
