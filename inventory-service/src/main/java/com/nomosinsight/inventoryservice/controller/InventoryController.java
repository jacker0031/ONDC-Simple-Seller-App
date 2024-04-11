package com.nomosinsight.inventoryservice.controller;

import com.nomosinsight.inventoryservice.model.dto.StockAddRequest;
import com.nomosinsight.inventoryservice.model.dto.StockCheckRequest;
import com.nomosinsight.inventoryservice.model.dto.StockCheckResponse;
import com.nomosinsight.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

	final private InventoryService inventoryService;

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public List<StockCheckResponse> isInStock(@RequestBody List<StockCheckRequest> stockCheckRequestList){
		return inventoryService.isInStock(stockCheckRequestList);
	}

	@PostMapping("/add")
	public void addStock(@RequestBody List<StockAddRequest> stockAddRequest){
		inventoryService.addStock(stockAddRequest);
	}
}
