package _team.commerce.domain.payment.entity;

import _team.commerce.global.common.BaseEntity;
import _team.commerce.global.exception.CustomException;
import _team.commerce.global.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO(주문 도메인 머지 후): @ManyToOne(fetch = LAZY) Order order 필드 추가

    @Column(nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    private LocalDateTime completedAt;

    private LocalDateTime canceledAt;

    private Payment(Long amount) {
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
    }

    /**
     * 주문 생성 시점의 결제 사전 기록. 상태는 항상 대기로 시작한다.
     */
    public static Payment createPending(Long amount) {
        return new Payment(amount);
    }

    /**
     * 모의 결제 승인. 대기 상태에서만 가능하다.
     */
    public void approve() {
        validateStatus(PaymentStatus.PENDING);
        this.status = PaymentStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    /**
     * 모의 결제 거절 또는 결제 전 취소. 대기 상태에서만 가능하다.
     */
    public void fail() {
        validateStatus(PaymentStatus.PENDING);
        this.status = PaymentStatus.FAILED;
    }

    /**
     * 결제 완료 후 취소. 완료 상태에서만 가능하다.
     */
    public void cancel() {
        validateStatus(PaymentStatus.COMPLETED);
        this.status = PaymentStatus.CANCELED;
        this.canceledAt = LocalDateTime.now();
    }

    public boolean isPending() {
        return this.status == PaymentStatus.PENDING;
    }

    public boolean isCompleted() {
        return this.status == PaymentStatus.COMPLETED;
    }

    private void validateStatus(PaymentStatus required) {
        if (this.status != required) {
            throw new CustomException(ErrorCode.INVALID_PAYMENT_STATUS);
        }
    }
}