package _team.commerce.domain.order.controller;

import _team.commerce.domain.order.controller.dto.OrderCancelRequest;
import _team.commerce.domain.order.controller.dto.OrderCreateRequest;
import _team.commerce.domain.order.controller.dto.OrderCreateResponse;
import _team.commerce.domain.order.controller.service.OrderService;
import _team.commerce.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<ApiResponse<OrderCreateResponse>> createOrder(

            @AuthenticationPrincipal Long memberId,

            @Valid @RequestBody OrderCreateRequest request
    ) {

        OrderCreateResponse response =
                orderService.createOrder(memberId, request);


        return ResponseEntity.ok(
                ApiResponse.success(response)
        );
    }
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long orderId,
            @Valid @RequestBody OrderCancelRequest request
    ) {
        orderService.cancelOrder(memberId, orderId, request);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}