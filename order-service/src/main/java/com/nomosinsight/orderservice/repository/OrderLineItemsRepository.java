package com.nomosinsight.orderservice.repository;

import com.nomosinsight.orderservice.model.entity.OrderLineItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineItemsRepository extends JpaRepository<OrderLineItems,Long> {}
