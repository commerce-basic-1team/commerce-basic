package _team.commerce.domain.product.config;

import _team.commerce.domain.product.entity.Product;
import _team.commerce.domain.product.entity.ProductCategory;
import _team.commerce.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductDataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) {
            return;
        }

        List<Product> products = List.of(
                Product.of("사과", 3000L, 50, "신선한 국내산 사과", ProductCategory.FOOD),
                Product.of("바나나", 4500L, 40, "달콤한 바나나 한 송이", ProductCategory.FOOD),
                Product.of("닭가슴살", 8900L, 30, "간편하게 섭취할 수 있는 닭가슴살", ProductCategory.FOOD),
                Product.of("원두커피", 15000L, 25, "고소한 향의 원두커피", ProductCategory.FOOD),
                Product.of("올리브오일", 22000L, 15, "요리에 활용하기 좋은 올리브오일", ProductCategory.FOOD),
                Product.of("견과류 세트", 12000L, 35, "여러 종류의 견과류 세트", ProductCategory.FOOD),
                Product.of("후드티", 35000L, 20, "편하게 입을 수 있는 기본 후드티", ProductCategory.CLOTHING),
                Product.of("청바지", 49000L, 18, "기본핏 데님 청바지", ProductCategory.CLOTHING),
                Product.of("셔츠", 29000L, 22, "깔끔한 기본 셔츠", ProductCategory.CLOTHING),
                Product.of("패딩", 99000L, 10, "겨울용 경량 패딩", ProductCategory.CLOTHING),
                Product.of("운동복", 42000L, 16, "가볍고 편한 운동복 세트", ProductCategory.CLOTHING),
                Product.of("양말 세트", 9000L, 60, "데일리 양말 세트", ProductCategory.CLOTHING),
                Product.of("무선 마우스", 39000L, 25, "사무용 무선 마우스", ProductCategory.ELECTRONICS),
                Product.of("기계식 키보드", 89000L, 14, "기계식 스위치를 사용한 키보드", ProductCategory.ELECTRONICS),
                Product.of("USB 허브", 24000L, 28, "다양한 장치를 연결할 수 있는 USB 허브", ProductCategory.ELECTRONICS),
                Product.of("블루투스 스피커", 55000L, 12, "휴대용 블루투스 스피커", ProductCategory.ELECTRONICS),
                Product.of("웹캠", 65000L, 8, "온라인 회의용 FHD 웹캠", ProductCategory.ELECTRONICS),
                Product.of("보조배터리", 32000L, 32, "대용량 휴대용 보조배터리", ProductCategory.ELECTRONICS),
                Product.of("모니터암", 78000L, 9, "높이 조절이 가능한 모니터암", ProductCategory.ELECTRONICS),
                Product.of("외장 SSD", 129000L, 7, "휴대용 고속 외장 SSD", ProductCategory.ELECTRONICS),
                Product.of("프로틴바", 2500L, 45, "간편하게 먹는 단백질바", ProductCategory.FOOD),
                Product.of("맨투맨", 31000L, 24, "데일리 기본 맨투맨", ProductCategory.CLOTHING),
                Product.of("이어폰", 27000L, 0, "유선 인이어 이어폰", ProductCategory.ELECTRONICS),
                Product.of("그래놀라", 7500L, 0, "아침 식사용 그래놀라", ProductCategory.FOOD)
        );

        productRepository.saveAll(products);
    }
}