package com.bridgelabz.fundoonotes.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3Object;
import com.bridgelabz.fundoonotes.model.ProfilePic;

@Component
public interface ProfilePicService {

	ProfilePic storeObjectInS3(MultipartFile file, String originalFilename, String contentType, String token); 

	void deleteProfilePic(String token);

	S3Object getProfilePic(MultipartFile file, String token);

	ProfilePic updateProfilePic(MultipartFile file, String originalFile, String contentType, String token);

}
