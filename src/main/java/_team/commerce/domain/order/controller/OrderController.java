package _team.commerce.domain.order.controller;

import _team.commerce.domain.order.service.OrderService;
import _team.commerce.domain.order.dto.*;
import _team.commerce.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    // 주문 생성
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


    // 주문 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<OrderPageResponse>> getOrderList(

            @AuthenticationPrincipal Long memberId,

            @PageableDefault(size = 10)
            Pageable pageable
    ) {

        OrderPageResponse response =
                orderService.getOrderList(memberId, pageable);

        return ResponseEntity.ok(
                ApiResponse.success(response)
        );
    }


    // 주문 상세 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDetailResponse>> getOrderDetail(

            @AuthenticationPrincipal Long memberId,
            @PathVariable Long orderId
    ) {

        OrderDetailResponse response =
                orderService.getOrderDetail(memberId, orderId);

        return ResponseEntity.ok(
                ApiResponse.success(response)
        );
    }


    // 주문 취소
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(

            @AuthenticationPrincipal Long memberId,
            @PathVariable Long orderId,
            @Valid @RequestBody OrderCancelRequest request
    ) {

        orderService.cancelOrder(memberId, orderId, request);

        return ResponseEntity.ok(
                ApiResponse.success(null)
        );
    }
    // 주문서 미리보기
    @PostMapping("/preview")
    public ResponseEntity<ApiResponse<OrderPreviewResponse>> previewOrder(

            @Valid @RequestBody OrderCreateRequest request
    ) {

        OrderPreviewResponse response =
                orderService.previewOrder(request);

        return ResponseEntity.ok(
                ApiResponse.success(response)
        );
    }
}