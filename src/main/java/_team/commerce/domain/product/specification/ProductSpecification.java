package _team.commerce.domain.product.specification;

import _team.commerce.domain.product.entity.Product;
import _team.commerce.domain.product.entity.ProductCategory;
import org.springframework.data.jpa.domain.Specification;

public final class ProductSpecification {

    private ProductSpecification() {
    }

    public static Specification<Product> withFilters(
            ProductCategory category,
            Long minPrice,
            Long maxPrice
    ) {
        Specification<Product> specification = Specification.unrestricted();

        if (category != null) {
            specification = specification.and(hasCategory(category));
        }

        if (minPrice != null) {
            specification = specification.and(priceGreaterThanOrEqualTo(minPrice));
        }

        if (maxPrice != null) {
            specification = specification.and(priceLessThanOrEqualTo(maxPrice));
        }

        return specification;
    }

    private static Specification<Product> hasCategory(ProductCategory category) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category"), category);
    }

    private static Specification<Product> priceGreaterThanOrEqualTo(Long minPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    private static Specification<Product> priceLessThanOrEqualTo(Long maxPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }
}
