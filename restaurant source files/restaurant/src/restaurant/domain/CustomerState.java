package restaurant.domain;

public enum CustomerState {
    WAITING,       
    SEATED,        
    ORDERED,       
    SERVED,        
    READY_TO_PAY,  
    PAID,
    LEFT;          

    public boolean losesSatisfaction() {
        return this == WAITING || this == SEATED || this == ORDERED;
    }
}
