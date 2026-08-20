package _team.commerce.domain.order.controller.dto;

import _team.commerce.domain.order.entity.OrderStatus;

public record OrderCreateResponse(
        Long orderId,
        String orderNumber,
        Long totalAmount,
        OrderStatus status
) {
}