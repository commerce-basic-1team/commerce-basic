package _team.commerce.domain.cart.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemCreateRequest(
        @NotNull
        Long productId,

        @NotNull
        @Positive
        Integer quantity
) {
}