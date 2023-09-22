package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class OrderRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("오늘 주문한 주문 내역을 가져온다.")
    @Test
    void findOrdersBy(){
        //given
        Product product1 = createProduct(ProductType.BOTTLE,"001",1000);
        Product product2 = createProduct(ProductType.BAKERY,"002",3000);
        Product product3 = createProduct(ProductType.HANDMADE,"003",5000);
        List<Product> products = List.of(product1,product2,product3);
        productRepository.saveAll(products);

        LocalDate startDay = LocalDate.of(2023,9,15);
        LocalDateTime registeredDateTime = LocalDateTime.of(2023,9,15,10,0);

        Order orders1 = createPaymentCompletedOrder(products,LocalDateTime.of(2023,9,14,23,59,59));
        Order orders2 = createPaymentCompletedOrder(products, LocalDateTime.of(2023,9,15,23,59,59));
        Order orders3 = createPaymentCompletedOrder(products, registeredDateTime);
        Order orders4 = createPaymentCompletedOrder(products, LocalDateTime.of(2023,9,16,0,0,0));

        orderRepository.save(orders1);
        orderRepository.save(orders2);
        orderRepository.save(orders3);
        orderRepository.save(orders4);

        //when
        List<Order> orders =orderRepository.findOrdersBy(startDay.atStartOfDay(),
                startDay.plusDays(1).atStartOfDay(),
                OrderStatus.PAYMENT_COMPLETED);

        //then
        assertThat(orders).hasSize(2);
    }
    private Order createPaymentCompletedOrder(List<Product> products, LocalDateTime registeredDateTime) {
        Order order1 = Order.builder()
                .products(products)
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .registeredDateTime(registeredDateTime)
                .build();
        return orderRepository.save(order1);
    }

    private Product createProduct(ProductType type, String productNumber, int price) {
        return Product.builder()
                .type(type)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("메뉴 이름")
                .build();
    }

}