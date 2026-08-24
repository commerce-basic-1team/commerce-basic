package _team.commerce.domain.order.dto;

import jakarta.validation.constraints.NotBlank;

public record OrderCancelRequest(

        @NotBlank(message = "취소 사유는 필수입니다.")
        String reason

) {
}