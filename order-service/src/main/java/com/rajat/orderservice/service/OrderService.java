package com.rajat.orderservice.service;

import com.rajat.orderservice.dto.InventoryResponse;
import com.rajat.orderservice.dto.OrderLineItemsDto;
import com.rajat.orderservice.dto.OrderReq;
import com.rajat.orderservice.dto.OrderRes;
import com.rajat.orderservice.model.Order;
import com.rajat.orderservice.model.OrderLineItems;
import com.rajat.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public OrderService(OrderRepository orderRepository,WebClient.Builder webClientBuilder) {
        this.orderRepository = orderRepository;
        this.webClientBuilder = webClientBuilder;
    }

    public OrderRes createOrder(OrderReq orderReq) {
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItems(orderReq.getOrderLineItemsDtoList().stream().map(this::getOrderLineItems).toList())
                .build();
        // TODO: call inventory service and place order if items are in stock
        List<String> skuCodes = order.getOrderLineItems().stream().map(OrderLineItems::getSkuCode).toList();
        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        if(inventoryResponses!=null) {
            boolean allProductInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::getIsInStock);
            if(allProductInStock)  {
                Order savedOrder = orderRepository.save(order);
                return getOrderRes(savedOrder);
            }
            throw new IllegalArgumentException("Product out of stock");
        }
        throw new IllegalStateException("Null inventory responses");
    }
    private OrderLineItems getOrderLineItems(OrderLineItemsDto orderLineItemsDto) {
        return OrderLineItems.builder()
                .skuCode(orderLineItemsDto.getSkuCode())
                .price(orderLineItemsDto.getPrice())
                .quantity(orderLineItemsDto.getQuantity())
                .build();
    }
    private OrderLineItemsDto getOrderLineItemsDto(OrderLineItems orderLineItems) {
        return OrderLineItemsDto.builder()
                .skuCode(orderLineItems.getSkuCode())
                .price(orderLineItems.getPrice())
                .quantity(orderLineItems.getQuantity())
                .id(orderLineItems.getId())
                .build();
    }
    private OrderRes getOrderRes(Order order) {
        return OrderRes.builder()
                .orderItems(order.getOrderLineItems().stream().map(this::getOrderLineItemsDto).toList())
                .orderNumber(order.getOrderNumber())
                .build();
    }
}
