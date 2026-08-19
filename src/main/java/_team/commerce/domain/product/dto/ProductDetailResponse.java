package _team.commerce.domain.product.dto;

import _team.commerce.domain.product.entity.Product;
import _team.commerce.domain.product.entity.ProductCategory;

public record ProductDetailResponse(
        Long productId,
        String productName,
        String description,
        Long price,
        Integer stock,
        ProductCategory category
) {

    public static ProductDetailResponse from(Product product) {
        return new ProductDetailResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory()
        );
    }
}