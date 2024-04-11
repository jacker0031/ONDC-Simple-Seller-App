package com.nomosinsight.inventoryservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockCheckRequest {
	private String skuCode;

	private Integer quantity;
}
