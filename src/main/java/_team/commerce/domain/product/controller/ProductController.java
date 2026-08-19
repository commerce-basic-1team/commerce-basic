package _team.commerce.domain.product.controller;

import _team.commerce.domain.product.dto.ProductDetailResponse;
import _team.commerce.domain.product.dto.ProductPageResponse;
import _team.commerce.domain.product.entity.ProductCategory;
import _team.commerce.domain.product.service.ProductService;
import _team.commerce.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<ProductPageResponse>> findProducts(
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ProductPageResponse response =
                productService.findProducts(category, minPrice, maxPrice, page, size);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getProduct(
            @PathVariable Long productId
    ) {
        ProductDetailResponse response = productService.getProduct(productId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}