package sample.cafekiosk.unit.order;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sample.cafekiosk.unit.beverages.Beverage;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Order {
    private LocalDateTime orderDateTime;
    private List<Beverage> beverages;

    public Order(LocalDateTime now, List<Beverage> beverages) {
    }
}
