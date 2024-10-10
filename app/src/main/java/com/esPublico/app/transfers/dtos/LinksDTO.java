package com.esPublico.app.transfers.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LinksDTO {
	@JsonProperty("self")
	private String self;
}
