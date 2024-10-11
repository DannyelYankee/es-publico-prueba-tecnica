package com.esPublico.app.transfers.dtos;

import lombok.Data;

@Data
public class SummaryDTO {
	private String value;
	private Long total;

	public SummaryDTO(String value, Long total) {
		this.value = value;
		this.total = total;
	}
}
