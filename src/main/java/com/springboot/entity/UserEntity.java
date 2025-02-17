package com.springboot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "user_table")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	private String userName;
	private String email;
	private String pwd;
	private String pwdUpdated;
	private String phno;

	@ManyToOne // Multiple users belongs to same country
	@JoinColumn(name = "country_id")
	private CountryEntity country;

	@ManyToOne // Multiple users belongs to same state
	@JoinColumn(name = "state_id")
	private StateEntity state;

	@ManyToOne // Multiple users belongs to same city
	@JoinColumn(name = "city_id")
	private CityEntity city;

}
