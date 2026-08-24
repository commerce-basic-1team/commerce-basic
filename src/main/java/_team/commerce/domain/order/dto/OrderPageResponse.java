package _team.commerce.domain.order.dto;

import _team.commerce.domain.order.entity.Order;
import org.springframework.data.domain.Page;

import java.util.List;

public record OrderPageResponse(

        List<OrderResponse> orders,

        long totalElements,

        int currentPage,

        int pageSize

) {

    public static OrderPageResponse from(Page<Order> orderPage) {

        List<OrderResponse> orders =
                orderPage.getContent()
                        .stream()
                        .map(OrderResponse::from)
                        .toList();

        return new OrderPageResponse(
                orders,
                orderPage.getTotalElements(),
                orderPage.getNumber(),
                orderPage.getSize()
        );
    }
}