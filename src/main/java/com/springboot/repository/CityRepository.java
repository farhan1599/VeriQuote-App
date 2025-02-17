package com.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.entity.CityEntity;

public interface CityRepository extends JpaRepository<CityEntity, Integer> {

	public List<CityEntity> findByStateStateId(Integer stateId);
}
