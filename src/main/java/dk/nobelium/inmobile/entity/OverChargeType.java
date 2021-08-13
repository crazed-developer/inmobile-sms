package dk.nobelium.inmobile.entity;

public enum OverChargeType {
    SERVICE(1),
    DONATION(2),
    MOBILE_PAYMENT(3);

    public final int value;

    OverChargeType(int value) {
        this.value = value;
    }
}
