package sample.cafekiosk.unit.beverages;

public class Latte implements Beverage {
    @Override
    public String getName() {
        return "라때";
    }

    @Override
    public int getPrice() {
        return 4500;
    }
}
