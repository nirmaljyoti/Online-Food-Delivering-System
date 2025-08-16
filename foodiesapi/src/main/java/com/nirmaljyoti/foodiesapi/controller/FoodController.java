package com.nirmaljyoti.foodiesapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nirmaljyoti.foodiesapi.io.FoodRequest;
import com.nirmaljyoti.foodiesapi.io.FoodResponse;
import com.nirmaljyoti.foodiesapi.service.FoodService;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

	@Autowired
	private FoodService foodService;

	@PostMapping()
	public FoodResponse addFood(@RequestPart("food") String foodString, @RequestPart("file") MultipartFile file) {
		ObjectMapper objectMapper = new ObjectMapper();
		FoodRequest foodRequest = null;
		try {
			foodRequest = objectMapper.readValue(foodString, FoodRequest.class);
		} catch (JsonProcessingException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON Format");
		}

		FoodResponse foodResponse = foodService.addFood(foodRequest, file);
		return foodResponse;
	}
	@GetMapping
    public List<FoodResponse> readFoods() {
        return this.foodService.readFoods();
    }

	@GetMapping("/{id}")
    public FoodResponse readFood(@PathVariable String id) {
        return this.foodService.readFood(id);
    }

	@DeleteMapping("/{id}")
	public void deleteFood(@PathVariable String id){
		this.foodService.deleteFood(id);
	}
}