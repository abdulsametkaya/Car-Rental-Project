package com.carrental.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageSavedResponse extends GRResponse {
	
	private String imageId;
	public ImageSavedResponse(String imageId, String message,boolean success) {
		super(success,message);
		this.imageId=imageId;
	}

}
