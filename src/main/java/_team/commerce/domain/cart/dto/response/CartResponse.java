package _team.commerce.domain.cart.dto.response;

import java.util.List;

public record CartResponse(
        Long cartId,
        List<CartItemResponse> items,
        Long totalAmount
) {
}