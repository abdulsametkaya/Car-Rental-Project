package com.carrental.controller;

import com.carrental.dto.UserDTO;
import com.carrental.dto.request.AdminUserUpdateRequest;
import com.carrental.dto.request.UpdatePasswordRequest;
import com.carrental.dto.request.UserUpdateRequest;
import com.carrental.dto.response.GRResponse;
import com.carrental.dto.response.ResponseMessage;
import com.carrental.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/auth/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<UserDTO> users=userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    //Sistemdeki kayıtlı herhangi bir kullanıcı kendi bilgilerini getiriyor.
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<UserDTO> getUserById(HttpServletRequest request){
        Long id= (Long) request.getAttribute("id");
        UserDTO userDTO= userService.findById(id);

        return ResponseEntity.ok(userDTO);
    }

    //http://localhost:8080/user/auth/pages?size=1&page=1&sort=firstName&direction=DESC
    @GetMapping("/auth/pages")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDTO>> getAllUserByPage(@RequestParam("page") int page,
                                                          @RequestParam("size") int size,
                                                          @RequestParam("sort") String prop,
                                                          @RequestParam("direction") Sort.Direction direction){

        Pageable pageable= PageRequest.of(page, size, Sort.by(direction,prop));
        Page<UserDTO> userDTOPage=userService.getUserPage(pageable);
        return ResponseEntity.ok(userDTOPage);
    }

    //http://localhost:8080/user/3/auth
    //to get any user in the sytem, admin is able to use this method.
    @GetMapping("/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserByIdAdmin(@PathVariable Long id){
        UserDTO user= userService.findById(id);
        return ResponseEntity.ok(user);
    }

    /*
     * http://localhost:8080/user/auth { "newPassword":"testup",
     * "oldPassword":"test1" }
     */
    @PatchMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<GRResponse> updatePassword(HttpServletRequest httpServletRequest,
                                                     @RequestBody UpdatePasswordRequest passwordRequest ){
        Long id=(Long) httpServletRequest.getAttribute("id");
        userService.updatePassword(id, passwordRequest);

        GRResponse response=new GRResponse();
        response.setMessage(ResponseMessage.PASSWORD_CHANGED_MESSAGE);
        response.setSuccess(true);

        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<GRResponse> updateUser(HttpServletRequest httpServletRequest,
                                                 @Valid @RequestBody UserUpdateRequest userUpdateRequest){
        Long id=(Long) httpServletRequest.getAttribute("id");
        userService.updateUser(id,userUpdateRequest);

        GRResponse response=new GRResponse();
        response.setMessage(ResponseMessage.UPDATE_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return ResponseEntity.ok(response);

    }

    //http://localhost:8080/user/2/auth
    @DeleteMapping("/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GRResponse> deleteUser(@PathVariable Long id){
        userService.removeById(id);

        GRResponse response=new GRResponse();
        response.setMessage(ResponseMessage.DELETE_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return ResponseEntity.ok(response);
    }

    //http://localhost:8080/user/5/auth
    @PutMapping("/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GRResponse> updateUserAuth(@PathVariable Long id,
                                                     @Valid @RequestBody AdminUserUpdateRequest adminUserUpdateRequest){

        userService.updateUserAuth(id,adminUserUpdateRequest);

        GRResponse response=new GRResponse();
        response.setMessage(ResponseMessage.UPDATE_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return ResponseEntity.ok(response);

    }



}
