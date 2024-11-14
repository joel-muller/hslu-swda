package ch.hslu.swda.entities;

public class ModifyContext {
    public static void modify(Order order, Modifiable modifiable) {
        modifiable.modify(order);
    }
}
