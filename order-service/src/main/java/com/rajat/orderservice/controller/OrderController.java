package com.rajat.orderservice.controller;

import com.rajat.orderservice.dto.OrderReq;
import com.rajat.orderservice.dto.OrderRes;
import com.rajat.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderRes placeOrder(@RequestBody OrderReq orderReq) {
        return orderService.createOrder(orderReq);
    }
}
