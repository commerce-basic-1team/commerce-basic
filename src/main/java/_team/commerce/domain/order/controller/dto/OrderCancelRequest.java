package _team.commerce.domain.order.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record OrderCancelRequest(

        // 주문을 취소하는 이유
        @NotBlank(message = "취소 사유는 필수입니다.")
        String reason

) {
}