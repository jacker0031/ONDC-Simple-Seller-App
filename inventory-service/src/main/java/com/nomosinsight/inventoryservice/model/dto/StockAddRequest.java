package com.nomosinsight.inventoryservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockAddRequest {
	private String skuCode;

	private Integer quantity;
}
