package com.nomosinsight.orderservice.controller;

import com.nomosinsight.orderservice.model.dto.OrderRequest;
import com.nomosinsight.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

  final private OrderService orderService;

  @PostMapping
  public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest) {
    orderService.placeOrder(orderRequest);
    return ResponseEntity.ok("Order Placed SuccessFully");
  }

}
