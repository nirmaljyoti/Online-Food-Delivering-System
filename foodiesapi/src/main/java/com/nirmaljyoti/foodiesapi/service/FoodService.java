package com.nirmaljyoti.foodiesapi.service;

import org.springframework.web.multipart.MultipartFile;

import com.nirmaljyoti.foodiesapi.io.FoodRequest;
import com.nirmaljyoti.foodiesapi.io.FoodResponse;

public interface FoodService {
	
	String uploadFile(MultipartFile file);
	
	FoodResponse addFood(FoodRequest foodRequest, MultipartFile file);
}
