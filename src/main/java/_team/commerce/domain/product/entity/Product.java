package _team.commerce.domain.product.entity;

import _team.commerce.global.common.BaseEntity;
import _team.commerce.global.exception.CustomException;
import _team.commerce.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false, length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;

    private Product(
            String name,
            Long price,
            Integer stock,
            String description,
            ProductCategory category
    ) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.category = category;
    }

    public static Product of(
            String name,
            Long price,
            Integer stock,
            String description,
            ProductCategory category
    ) {
        return new Product(name, price, stock, description, category);
    }

    public void decreaseStock(int quantity) {
        validatePositiveQuantity(quantity);

        if (stock < quantity) {
            throw new CustomException(ErrorCode.OUT_OF_STOCK);
        }

        this.stock -= quantity;
    }

    public void increaseStock(int quantity) {
        validatePositiveQuantity(quantity);

        this.stock += quantity;
    }

    private void validatePositiveQuantity(int quantity) {
        if (quantity <= 0) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
    }
}