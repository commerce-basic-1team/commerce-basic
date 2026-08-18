package _team.commerce.domain.payment.dto.response;

import _team.commerce.domain.payment.entity.Payment;
import _team.commerce.domain.payment.entity.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentResponse(
        Long paymentId,
        Long amount,
        PaymentStatus status,
        LocalDateTime completedAt,
        LocalDateTime canceledAt
) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getCompletedAt(),
                payment.getCanceledAt()
        );
    }
}