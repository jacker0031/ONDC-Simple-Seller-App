package com.nomosinsight.inventoryservice.service;

import com.nomosinsight.inventoryservice.model.dto.StockAddRequest;
import com.nomosinsight.inventoryservice.model.dto.StockCheckRequest;
import com.nomosinsight.inventoryservice.model.dto.StockCheckResponse;
import com.nomosinsight.inventoryservice.model.entity.Inventory;
import com.nomosinsight.inventoryservice.repository.InventoryRepository;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

  private final InventoryRepository inventoryRepository;

  public List<StockCheckResponse> isInStock(List<StockCheckRequest> stockCheckRequestList) {

    List<StockCheckResponse> stockCheckResponses=new ArrayList<>();
    stockCheckRequestList.forEach(stockCheckRequest -> {
      Optional<Inventory> bySkuCode = inventoryRepository.findBySkuCode(stockCheckRequest.getSkuCode());
      if(bySkuCode.isPresent())
      {
        if(bySkuCode.get().getQuantity()>=stockCheckRequest.getQuantity()){
          stockCheckResponses.add(StockCheckResponse.builder().available(true).skuCode(stockCheckRequest.getSkuCode()).build());
        }
        else {
          stockCheckResponses.add(StockCheckResponse.builder().available(false).skuCode(stockCheckRequest.getSkuCode()).build());
        }
      }
      else{
        stockCheckResponses.add(StockCheckResponse.builder().available(false).skuCode(stockCheckRequest.getSkuCode()).build());
      }
    });
    return stockCheckResponses;
  }

  @Transactional
  public void addStock(List<StockAddRequest> stockAddRequests) {
    stockAddRequests.forEach(
        stockAddRequest -> {
          Optional<Inventory> bySkuCode =
              inventoryRepository.findBySkuCode(stockAddRequest.getSkuCode());
          if (bySkuCode.isPresent()) {
            Inventory currentStock = bySkuCode.get();
            Integer updateQuantity = currentStock.getQuantity() + stockAddRequest.getQuantity();
            currentStock.setQuantity(updateQuantity);
            inventoryRepository.save(currentStock);
          } else {
            inventoryRepository.save(
                Inventory.builder()
                    .quantity(stockAddRequest.getQuantity())
                    .skuCode(stockAddRequest.getSkuCode())
                    .build());
          }
        });
  }
}
