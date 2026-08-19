package _team.commerce.domain.cart.service;

import _team.commerce.domain.cart.entity.Cart;
import _team.commerce.domain.cart.entity.CartItem;
import _team.commerce.domain.cart.repository.CartItemRepository;
import _team.commerce.domain.cart.repository.CartRepository;
import _team.commerce.global.exception.CustomException;
import _team.commerce.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    /**
     * 장바구니 상품 개별 삭제
     */
    public void deleteItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.CART_ITEM_NOT_FOUND)
                );

        cartItemRepository.delete(cartItem);
    }

    /**
     * 장바구니 전체 비우기
     */
    public void clearCart(Cart cart) {
        List<CartItem> cartItems = cartItemRepository.findAllByCart(cart);

        cartItemRepository.deleteAll(cartItems);
    }

    /**
     * 장바구니 상품 수량 변경
     */
    public void changeQuantity(Long cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.CART_ITEM_NOT_FOUND)
                );

        // TODO(Product 도메인 구현 후)
        // 상품의 현재 재고를 조회하여 변경 수량이 재고를 초과하지 않는지 검증한다.

        cartItem.changeQuantity(quantity);
    }
}