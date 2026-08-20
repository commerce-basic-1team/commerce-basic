package _team.commerce.domain.cart.controller;

import _team.commerce.domain.cart.dto.request.CartItemCreateRequest;
import _team.commerce.domain.cart.dto.request.CartItemQuantityUpdateRequest;
import _team.commerce.domain.cart.dto.response.CartResponse;
import _team.commerce.domain.cart.service.CartService;
import _team.commerce.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    /**
     * 장바구니 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart(
            @AuthenticationPrincipal Long memberId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(cartService.getCart(memberId))
        );
    }

    /**
     * 상품 담기
     */
    @PostMapping("/items")
    public ResponseEntity<ApiResponse<Void>> addItem(
            @AuthenticationPrincipal Long memberId,
            @RequestBody @Valid CartItemCreateRequest request
    ) {
        cartService.addItem(memberId, request);

        return ResponseEntity.ok(ApiResponse.ok());
    }

    /**
     * 장바구니 상품 수량 변경
     */
    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<Void>> updateQuantity(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long cartItemId,
            @RequestBody @Valid CartItemQuantityUpdateRequest request
    ) {
        cartService.updateQuantity(memberId, cartItemId, request);

        return ResponseEntity.ok(ApiResponse.ok());
    }

    /**
     * 장바구니 상품 개별 삭제
     */
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long cartItemId
    ) {
        cartService.deleteItem(memberId, cartItemId);

        return ResponseEntity.ok(ApiResponse.ok());
    }

    /**
     * 장바구니 전체 비우기
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> clearCart(
            @AuthenticationPrincipal Long memberId
    ) {
        cartService.clearCart(memberId);

        return ResponseEntity.ok(ApiResponse.ok());
    }
}