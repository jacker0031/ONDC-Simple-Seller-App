package com.nomosinsight.orderservice.service;

import com.nomosinsight.orderservice.model.dto.OrderRequest;
import com.nomosinsight.orderservice.model.dto.StockCheckRequest;
import com.nomosinsight.orderservice.model.dto.StockCheckResponse;
import com.nomosinsight.orderservice.model.entity.Order;
import com.nomosinsight.orderservice.model.entity.OrderLineItems;
import com.nomosinsight.orderservice.repository.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;

  private final WebClient.Builder webClientBuilder;

  public void placeOrder(OrderRequest orderRequest) {

    List<StockCheckRequest> stockCheckRequests = new ArrayList<>();
    orderRequest
        .getOrderLineItemsDtoList()
        .forEach(
            orderLineItemsDto -> {
              stockCheckRequests.add(
                  StockCheckRequest.builder()
                      .quantity(orderLineItemsDto.getQuantity())
                      .skuCode(orderLineItemsDto.getSkuCode())
                      .build());
            });
    // check order is in stock or not.
    List<StockCheckResponse> isStockPresent =
        webClientBuilder
            .build()
            .post()
            .uri("http://inventory-service/api/inventory")
            .body(BodyInserters.fromValue(stockCheckRequests))
            .retrieve()
            .bodyToFlux(StockCheckResponse.class)
            .collectList()
            .block();
    List<String> notAvailableItem =
        isStockPresent.stream()
            .filter(isStock -> !isStock.getAvailable())
            .map(StockCheckResponse::getSkuCode)
            .toList();
    if (!notAvailableItem.isEmpty()) {
      throw HttpClientErrorException.BadRequest.create(
          HttpStatus.BAD_REQUEST,
          String.join(",", notAvailableItem) + " Not Available",
          null,
          null,
          null);
    }
    Order order =
        Order.builder()
            .orderNumber(UUID.randomUUID().toString())
            .orderLineItemsList(
                orderRequest.getOrderLineItemsDtoList().stream()
                    .map(
                        orderLineItemsDto ->
                            OrderLineItems.builder()
                                .price(orderLineItemsDto.getPrice())
                                .skuCode(orderLineItemsDto.getSkuCode())
                                .quantity(orderLineItemsDto.getQuantity())
                                .build())
                    .collect(Collectors.toList()))
            .build();
    orderRepository.save(order);
  }
}
