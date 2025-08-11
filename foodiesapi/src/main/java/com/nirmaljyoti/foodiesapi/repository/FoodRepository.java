package com.nirmaljyoti.foodiesapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.nirmaljyoti.foodiesapi.entity.FoodEntity;

public interface FoodRepository extends MongoRepository<FoodEntity, String>{

}
