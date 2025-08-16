package com.nirmaljyoti.foodiesapi.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.nirmaljyoti.foodiesapi.entity.FoodEntity;
import com.nirmaljyoti.foodiesapi.io.FoodRequest;
import com.nirmaljyoti.foodiesapi.io.FoodResponse;
import com.nirmaljyoti.foodiesapi.repository.FoodRepository;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
public class FoodServiceImpl implements FoodService {

	@Autowired
	private S3Client s3Client;

	@Autowired
	private FoodRepository foodRepository;

	@Value("${aws.s3.bucketname}")
	private String bucketName;

	@Override
	public String uploadFile(MultipartFile file) {
		String fileNameExtension = file.getOriginalFilename()
				.substring(file.getOriginalFilename().lastIndexOf("." + 1));
		String key = UUID.randomUUID().toString() + "." + fileNameExtension;
		try {
			PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(key)
					.acl("public-read").contentType(file.getContentType()).build();
			PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest,
					RequestBody.fromBytes(file.getBytes()));

			if (putObjectResponse.sdkHttpResponse().isSuccessful()) {
				return "https://" + bucketName + ".s3.amazonaws.com/" + key;
			} else
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload error");

		} catch (IOException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"An error occured while uploading the file");
		}
	}

	@Override
	public FoodResponse addFood(FoodRequest foodRequest, MultipartFile file) {
		FoodEntity foodEntity = convertToEntity(foodRequest);
		foodEntity.setImageUrl(uploadFile(file));
		FoodEntity savedFoodEntity = foodRepository.save(foodEntity);
		return convertToResponse(savedFoodEntity);
	}

	private FoodEntity convertToEntity(FoodRequest foodRequest) {
		FoodEntity foodEntity = FoodEntity.builder().name(foodRequest.getName())
				.description(foodRequest.getDescription()).price(foodRequest.getPrice())
				.category(foodRequest.getCategory()).build();

		return foodEntity;
	}

	private FoodResponse convertToResponse(FoodEntity savedFoodEntity) {
		return FoodResponse.builder().id(savedFoodEntity.getId()).name(savedFoodEntity.getName()).category(savedFoodEntity.getCategory())
				.description(savedFoodEntity.getDescription()).imageUrl(savedFoodEntity.getImageUrl())
				.price(savedFoodEntity.getPrice()).build();
	}

	@Override
	public List<FoodResponse> readFoods() {
		List<FoodEntity> all = this.foodRepository.findAll();
		return all.stream().map(object->convertToResponse(object)).collect(Collectors.toList());
	}

	public FoodResponse readFood(String id){
		FoodEntity entity = this.foodRepository.findById(id).orElseThrow(()-> new RuntimeException("food not found with id : "+ id));
		return convertToResponse(entity);
	}

	public boolean deleteFile(String filename){
		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
		.bucket(bucketName)
		.key(filename)
		.build();
		s3Client.deleteObject(deleteObjectRequest);
		return true;
	}

	public void deleteFood(String id)
	{
		FoodResponse food = readFood(id);
		String imageUrl = food.getImageUrl();
		String filename = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
		boolean isFileDeleted = deleteFile(filename);
		if(isFileDeleted){
			foodRepository.deleteById(food.getId());
		}
	}

}
