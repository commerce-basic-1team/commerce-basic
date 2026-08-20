package _team.commerce.domain.payment.dto.response;

import _team.commerce.domain.order.entity.OrderStatus;
import _team.commerce.domain.payment.entity.Payment;
import _team.commerce.domain.payment.entity.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentResponse(
        Long paymentId,
        Long orderId,
        String orderNumber,
        Long amount,
        PaymentStatus paymentStatus,
        OrderStatus orderStatus,
        LocalDateTime completedAt,
        LocalDateTime canceledAt
) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getOrder().getOrderNumber(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getOrder().getStatus(),
                payment.getCompletedAt(),
                payment.getCanceledAt()
        );
    }
}