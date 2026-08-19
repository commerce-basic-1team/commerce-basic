package _team.commerce.domain.cart.controller;

import _team.commerce.domain.cart.dto.request.CartItemQuantityUpdateRequest;
import _team.commerce.domain.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    /**
     * 장바구니 상품 수량 변경
     */
    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<Void> changeQuantity(
            @PathVariable Long cartItemId,
            @RequestBody @Valid CartItemQuantityUpdateRequest request
    ) {
        cartService.changeQuantity(cartItemId, request.quantity());

        return ResponseEntity.noContent().build();
    }

    /**
     * 장바구니 상품 개별 삭제
     */
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long cartItemId
    ) {
        cartService.deleteItem(cartItemId);

        return ResponseEntity.noContent().build();
    }

    // TODO(Member 도메인 구현 후)
    // GET /api/cart
    // 현재 로그인한 회원의 장바구니 조회

    // TODO(Member/Product 도메인 구현 후)
    // POST /api/cart/items
    // 상품 담기

    // TODO(Member 도메인 구현 후)
    // DELETE /api/cart
    // 현재 로그인한 회원의 장바구니 전체 비우기
}