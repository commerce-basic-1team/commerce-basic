package _team.commerce.domain.cart.dto.response;

import _team.commerce.domain.cart.entity.CartItem;

public record CartItemResponse(
        Long cartItemId,
        Long productId,
        Integer quantity
) {

    public static CartItemResponse from(CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getProduct().getId(),
                cartItem.getQuantity()
        );
    }
}