package _team.commerce.domain.payment.service;

import _team.commerce.domain.cart.service.CartService;
import _team.commerce.domain.order.entity.Order;
import _team.commerce.domain.payment.dto.request.MockPaymentRequest;
import _team.commerce.domain.payment.dto.request.PaymentCancelRequest;
import _team.commerce.domain.payment.dto.response.PaymentResponse;
import _team.commerce.domain.payment.entity.Payment;
import _team.commerce.domain.payment.entity.PaymentResult;
import _team.commerce.domain.payment.repository.PaymentRepository;
import _team.commerce.global.exception.CustomException;
import _team.commerce.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CartService cartService;

    /**
     * 모의 결제 승인 요청.
     * 선검증 3종(소유권·상태·금액) 통과 시 결과에 따라 승인 또는 거절 처리한다.
     */
    @Transactional
    public PaymentResponse processMockPayment(Long memberId, MockPaymentRequest request) {
        Payment payment = getPaymentByOrderId(request.orderId());

        validateOwner(payment, memberId);
        validatePayable(payment);
        validateAmount(payment, request.amount());

        if (request.result() == PaymentResult.SUCCESS) {
            approvePayment(payment, memberId);
        } else {
            failPayment(payment);
        }

        return PaymentResponse.from(payment);
    }

    /**
     * 결제 단건 조회.
     */
    public PaymentResponse getPayment(Long memberId, Long paymentId) {
        Payment payment = getPaymentById(paymentId);
        validateOwner(payment, memberId);

        return PaymentResponse.from(payment);
    }

    /**
     * 결제 취소(전액).
     * 결제 완료 상태에서만 가능하며, 결제 전 취소는 주문 도메인에서 처리한다.
     */
    @Transactional
    public PaymentResponse cancelPayment(
            Long memberId,
            Long paymentId,
            PaymentCancelRequest request
    ) {
        Payment payment = getPaymentById(paymentId);
        validateOwner(payment, memberId);

        payment.cancel();

        Order order = payment.getOrder();
        order.cancel(request.reason());
        restoreStock(order);

        return PaymentResponse.from(payment);
    }
    /**
     * 승인 시: 결제 완료 → 주문 완료 → 장바구니 비우기.
     * 재고는 주문 생성 시점에 이미 차감되었으므로 건드리지 않는다.
     */
    private void approvePayment(Payment payment, Long memberId) {
        payment.approve();
        payment.getOrder().complete();
        cartService.clearCart(memberId);
    }

    /**
     * 거절 시: 결제 실패 → 주문 취소 → 선차감 재고 복구.
     * 장바구니는 재시도를 위해 유지한다.
     */
    private void failPayment(Payment payment) {
        payment.fail();

        Order order = payment.getOrder();
        order.cancel("결제 실패");
        restoreStock(order);
    }

    private void restoreStock(Order order) {
        order.getOrderItems().forEach(orderItem ->
                orderItem.getProduct().increaseStock(orderItem.getQuantity())
        );
    }

    private Payment getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
    }

    private Payment getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
    }

    private void validateOwner(Payment payment, Long memberId) {
        if (!payment.isOwnedBy(memberId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }
    }

    private void validatePayable(Payment payment) {
        if (!payment.isPending()) {
            throw new CustomException(ErrorCode.INVALID_PAYMENT_STATUS);
        }
    }

    private void validateAmount(Payment payment, Long requestAmount) {
        if (!payment.getAmount().equals(requestAmount)) {
            throw new CustomException(ErrorCode.AMOUNT_MISMATCH);
        }
    }
}