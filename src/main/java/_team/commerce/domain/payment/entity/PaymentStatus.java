package _team.commerce.domain.payment.entity;

public enum PaymentStatus {
    PENDING,    // 대기 - 주문 생성 시 사전 기록
    COMPLETED,  // 완료 - 모의 결제 승인
    FAILED,     // 실패 - 결제 거절 / 결제 전 취소
    CANCELED    // 취소 - 결제 완료 후 취소
}