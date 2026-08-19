package _team.commerce.domain.cart.entity;

import _team.commerce.global.common.BaseEntity;
import _team.commerce.global.exception.CustomException;
import _team.commerce.global.exception.ErrorCode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "cart_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    // TODO(Product 도메인 구현 후): Product product 연관관계 추가

    private Integer quantity;

    private CartItem(Cart cart, Integer quantity) {
        validateQuantity(quantity);
        this.cart = cart;
        this.quantity = quantity;
    }

    public static CartItem create(Cart cart, Integer quantity) {
        return new CartItem(cart, quantity);
    }

    public void changeQuantity(Integer quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
    }
}