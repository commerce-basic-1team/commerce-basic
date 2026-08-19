package _team.commerce.domain.product.dto;

import _team.commerce.domain.product.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public record ProductPageResponse(
        List<ProductListResponse> products,
        long totalElements,
        int currentPage,
        int pageSize
) {

    public static ProductPageResponse from(Page<Product> productPage) {
        List<ProductListResponse> products = productPage.getContent()
                .stream()
                .map(ProductListResponse::from)
                .toList();

        return new ProductPageResponse(
                products,
                productPage.getTotalElements(),
                productPage.getNumber(),
                productPage.getSize()
        );
    }
}