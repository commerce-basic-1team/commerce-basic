package _team.commerce.domain.cart.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemQuantityUpdateRequest(
        @NotNull
        @Positive
        Integer quantity
) {
}