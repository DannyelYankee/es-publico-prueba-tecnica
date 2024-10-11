package com.esPublico.app.domain.services;

import java.util.List;

import com.esPublico.app.domain.models.Order;

public interface CsvExportService {

	public byte[] exportOrdersToCsv(List<Order> orders);

}
