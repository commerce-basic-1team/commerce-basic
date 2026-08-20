package _team.commerce.domain.order.entity;

import _team.commerce.domain.auth.entity.Member;
import _team.commerce.global.common.BaseEntity;
import _team.commerce.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    private String cancelReason;


    private Order(
            Member member,
            String orderNumber,
            BigDecimal totalAmount
    ) {
        this.member = member;
        this.orderNumber = orderNumber;
        this.totalAmount = totalAmount;
        this.status = OrderStatus.PENDING_PAYMENT;
    }


    public static Order create(
            Member member,
            String orderNumber,
            BigDecimal totalAmount
    ) {
        return new Order(member, orderNumber, totalAmount);
    }


    public void complete() {
        this.status = OrderStatus.COMPLETED;
    }


    public void cancel(String reason) {
        if (this.status == OrderStatus.CANCELED) {
            throw new IllegalStateException(
                    ErrorCode.ALREADY_CANCELED.getMessage()
            );
        }

        this.status = OrderStatus.CANCELED;
        this.cancelReason = reason;
    }
}