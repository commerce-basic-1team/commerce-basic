package _team.commerce.domain.cart.controller;

import _team.commerce.domain.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    /*
     * TODO(Auth JWT 구현 후)
     *
     * 현재 로그인한 회원의 memberId를 JWT에서 가져온 뒤
     * CartService와 연동하여 아래 API를 구현한다.
     *
     * 1. 장바구니 조회
     * GET /api/cart
     *
     * 2. 상품 담기
     * POST /api/cart/items
     *
     * 3. 상품 수량 변경
     * PATCH /api/cart/items/{cartItemId}
     *
     * 4. 장바구니 상품 개별 삭제
     * DELETE /api/cart/items/{cartItemId}
     *
     * 5. 장바구니 전체 비우기
     * DELETE /api/cart
     */
}