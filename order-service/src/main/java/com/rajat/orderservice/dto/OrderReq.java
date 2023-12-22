package com.rajat.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderReq {
    @JsonAlias("order_items_list")
    private List<OrderLineItemsDto> orderLineItemsDtoList;
}
