package _team.commerce.domain.payment.controller;

import _team.commerce.domain.payment.dto.request.MockPaymentRequest;
import _team.commerce.domain.payment.dto.request.PaymentCancelRequest;
import _team.commerce.domain.payment.dto.response.PaymentResponse;
import _team.commerce.domain.payment.service.PaymentService;
import _team.commerce.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 모의 결제 승인 요청
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> processMockPayment(
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody MockPaymentRequest request
    ) {
        PaymentResponse response = paymentService.processMockPayment(memberId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 결제 단건 조회
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long paymentId
    ) {
        PaymentResponse response = paymentService.getPayment(memberId, paymentId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 결제 취소 (전액)
     */
    @PostMapping("/{paymentId}/cancel")
    public ResponseEntity<ApiResponse<PaymentResponse>> cancelPayment(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long paymentId,
            @Valid @RequestBody PaymentCancelRequest request
    ) {
        PaymentResponse response = paymentService.cancelPayment(memberId, paymentId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}