package com.carrental.controller;


import com.carrental.domain.ImageFile;
import com.carrental.dto.ImageFileDTO;
import com.carrental.dto.response.ImageSavedResponse;
import com.carrental.dto.response.ResponseMessage;
import com.carrental.service.ImageFileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/files")
@AllArgsConstructor
public class ImageFileController {
	
	private ImageFileService imageFileService;
	
	@PostMapping("/upload")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ImageSavedResponse> uploadFile(@RequestParam("file") MultipartFile file ){
		String imageId= imageFileService.saveImage(file);
		
		ImageSavedResponse response=new ImageSavedResponse();
		
		response.setImageId(imageId);
		response.setMessage(ResponseMessage.IMAGE_SAVED_RESPONSE_MESSAGE);
		response.setSuccess(true);
		
		return ResponseEntity.ok(response);
		
	}
	
	
	@GetMapping("/download/{id}")
	public ResponseEntity<byte []> getImageFile(@PathVariable String id){
		ImageFile imageFile= imageFileService.getImageById(id);
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+
		imageFile.getName()).body(imageFile.getData());
	}
	
	
	@GetMapping("/display/{id}")
	public ResponseEntity<byte []> displayFile(@PathVariable String id){
		ImageFile imageFile= imageFileService.getImageById(id);
	
		HttpHeaders header=new HttpHeaders();
		header.setContentType(MediaType.IMAGE_PNG);
		
		return new ResponseEntity<>(imageFile.getData(),header,HttpStatus.OK);
	}
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<ImageFileDTO>> getAllImages(){
		List<ImageFileDTO> imageList= imageFileService.getAllImages();
		
		//return ResponseEntity.ok(imageList);
		
		return ResponseEntity.status(HttpStatus.OK).body(imageList);
	}
	
	
	
	

}
