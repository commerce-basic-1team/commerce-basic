package _team.commerce.domain.cart.dto.response;

import _team.commerce.domain.cart.entity.CartItem;

public record CartItemResponse(
        Long cartItemId,
        Integer quantity
) {

    public static CartItemResponse from(CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getQuantity()
        );
    }
}