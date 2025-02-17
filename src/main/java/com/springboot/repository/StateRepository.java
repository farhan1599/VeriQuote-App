package com.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.springboot.entity.StateEntity;


public interface StateRepository extends JpaRepository<StateEntity, Integer> {

	public List<StateEntity> findByCountryCountryId(Integer countryId);

}
