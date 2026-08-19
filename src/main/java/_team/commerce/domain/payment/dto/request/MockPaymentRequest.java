package _team.commerce.domain.payment.dto.request;

import _team.commerce.domain.payment.entity.PaymentResult;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MockPaymentRequest(

        @NotNull(message = "주문 ID는 필수입니다.")
        Long orderId,

        @NotNull(message = "결제 결과는 필수입니다.")
        PaymentResult result,

        @NotNull(message = "결제 금액은 필수입니다.")
        @Min(value = 1, message = "결제 금액은 1원 이상이어야 합니다.")
        Long amount
) {
}