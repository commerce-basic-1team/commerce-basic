package _team.commerce.domain.order.controller;

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

    // 주문 관련 비즈니스 로직을 담당하는 Service
    private final OrderService orderService;


    // 주문 생성 API
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
}