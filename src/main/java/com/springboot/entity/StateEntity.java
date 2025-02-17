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
@Table(name = "states_master")
public class StateEntity {
	
	@Id
	private Integer stateId;
	private String stateName;
	
	@ManyToOne //Multiple states belongs to single country
	@JoinColumn(name = "country_id")
	private CountryEntity country; 

}
