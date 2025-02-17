package com.springboot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "city_master")
public class CityEntity {
	
	@Id
	private Integer cityId;
	private String cityName;
	
	@ManyToOne //Multiple cities belongs to single state
	@JoinColumn(name = "state_id")
	private StateEntity state;

}
