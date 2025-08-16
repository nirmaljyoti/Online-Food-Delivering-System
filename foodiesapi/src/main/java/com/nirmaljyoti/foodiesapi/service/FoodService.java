package com.nirmaljyoti.foodiesapi.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nirmaljyoti.foodiesapi.io.FoodRequest;
import com.nirmaljyoti.foodiesapi.io.FoodResponse;

public interface FoodService {
	
	String uploadFile(MultipartFile file);
	
	FoodResponse addFood(FoodRequest foodRequest, MultipartFile file);
	List<FoodResponse> readFoods();
	FoodResponse readFood(String id);
	boolean deleteFile(String filename);
	void deleteFood(String id);
}
