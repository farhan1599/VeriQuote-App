package com.springboot.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuoteResponseDTO {

	private Integer quoteId;
	private String quote;
	private String author;

}
