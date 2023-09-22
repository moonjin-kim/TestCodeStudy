package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.IntegrationTestSupport;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class ProductRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다")
    @Test
    void findAllBySellingStatusIn(){
        //given
        Product product1 =createProduct("001",ProductType.HANDMADE,ProductSellingStatus.SELLING,"아메리카노",4000);
        Product product2 =createProduct("002",ProductType.HANDMADE,ProductSellingStatus.HOLD,"카페라떼",4500);
        Product product3 =createProduct("003",ProductType.HANDMADE,ProductSellingStatus.STOP_SELLING,"팥빙수",7000);


        productRepository.saveAll(List.of(product1,product2,product3));

        //when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(ProductSellingStatus.SELLING,ProductSellingStatus.HOLD));

        //then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001","아메리카노",ProductSellingStatus.SELLING),
                        tuple("002","카페라떼",ProductSellingStatus.HOLD)
                );
    }

    @DisplayName("상품번호 리스트로 상품번호를 조회한다")
    @Test
    void findAllByProductNumberIn(){
        //given
        Product product1 =createProduct("001",ProductType.HANDMADE,ProductSellingStatus.SELLING,"아메리카노",4000);
        Product product2 =createProduct("002",ProductType.HANDMADE,ProductSellingStatus.HOLD,"카페라떼",4500);
        Product product3 =createProduct("003",ProductType.HANDMADE,ProductSellingStatus.STOP_SELLING,"팥빙수",7000);

        productRepository.saveAll(List.of(product1,product2,product3));

        //when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001","002"));

        //then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001","아메리카노",ProductSellingStatus.SELLING),
                        tuple("002","카페라떼",ProductSellingStatus.HOLD)
                );
    }

    @DisplayName("가장 마지막 상품의 상품번호를 가져온다")
    @Test
    void findLatestProduct(){
        //given
        String targetProductName = "003";

        Product product1 =createProduct("001",ProductType.HANDMADE,ProductSellingStatus.SELLING,"아메리카노",4000);
        Product product2 =createProduct("002",ProductType.HANDMADE,ProductSellingStatus.HOLD,"카페라떼",4500);
        Product product3 =createProduct(targetProductName,ProductType.HANDMADE,ProductSellingStatus.STOP_SELLING,"팥빙수",7000);

        productRepository.saveAll(List.of(product1,product2,product3));

        //when
        String latestProductNumber = productRepository.findLatestProductNumber();

        //then
        assertThat(latestProductNumber).isEqualTo(targetProductName);
    }

    @DisplayName("가장 마지막으로 저장한 상품의 상ㅍㅁ번호를 읽어로 때, 상품이 하나도 업는 경우에는 null을 반환한다.")
    @Test
    void findLatestProductWhenProductIsEmpty(){

        //when
        String latestProductNumber = productRepository.findLatestProductNumber();

        //then
        assertThat(latestProductNumber).isNull();
    }


    private static Product createProduct(String productNumber,ProductType type,ProductSellingStatus sellingStatus,String name, int price){
            return Product.builder()
                    .productNumber(productNumber)
                    .type(type)
                    .sellingStatus(sellingStatus)
                    .name(name)
                    .price(price)
                    .build();
    }



}