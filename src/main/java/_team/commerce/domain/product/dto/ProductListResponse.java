package _team.commerce.domain.product.dto;

import _team.commerce.domain.product.entity.Product;
import _team.commerce.domain.product.entity.ProductCategory;

public record ProductListResponse(
        Long productId,
        String productName,
        Long price,
        Integer stock,
        ProductCategory category
) {

    public static ProductListResponse from(Product product) {
        return new ProductListResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getCategory()
        );
    }
}