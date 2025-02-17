package com.springboot.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {

	private Integer userId;
	private String userName;
	private String email;
	private String pwd;
	private String pwdUpdated;
	private String phno;

	/*
	 * from the UI association mapping objects(country,state,city) will not come,
	 * from the UI countryId,stateId,cityId will come, to store that data we'll take
	 * :
	 */
	private Integer countryId;
	private Integer stateId;
	private Integer cityId;

}
