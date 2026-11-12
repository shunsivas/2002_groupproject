package restaurant.domain;

import java.util.function.BiFunction;

public enum CustomerKind {
    REGULAR(RegularCustomer::new),
    VIP(VIPCustomer::new),
    CRITIC(CriticCustomer::new);

    private final BiFunction<Integer, Integer, Customer> factory;

    CustomerKind(BiFunction<Integer, Integer, Customer> factory) { this.factory = factory; }

    public Customer create(int number, int arrivalTurn) { return factory.apply(number, arrivalTurn); }
}
