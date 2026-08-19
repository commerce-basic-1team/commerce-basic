package _team.commerce.domain.product.service;

import _team.commerce.domain.product.dto.ProductDetailResponse;
import _team.commerce.domain.product.dto.ProductPageResponse;
import _team.commerce.domain.product.entity.Product;
import _team.commerce.domain.product.entity.ProductCategory;
import _team.commerce.domain.product.repository.ProductRepository;
import _team.commerce.domain.product.specification.ProductSpecification;
import _team.commerce.global.exception.CustomException;
import _team.commerce.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private static final int MAX_PAGE_SIZE = 100;

    private final ProductRepository productRepository;

    public ProductPageResponse findProducts(
            ProductCategory category,
            Long minPrice,
            Long maxPrice,
            int page,
            int size
    ) {
        validateSearchCondition(minPrice, maxPrice, page, size);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Specification<Product> specification =
                ProductSpecification.withFilters(category, minPrice, maxPrice);

        Page<Product> productPage =
                productRepository.findAll(specification, pageable);

        return ProductPageResponse.from(productPage);
    }

    public ProductDetailResponse getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        return ProductDetailResponse.from(product);
    }

    private void validateSearchCondition(
            Long minPrice,
            Long maxPrice,
            int page,
            int size
    ) {
        if (page < 0 || size <= 0 || size > MAX_PAGE_SIZE) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        if (minPrice != null && minPrice < 0) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        if (maxPrice != null && maxPrice < 0) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
    }
}