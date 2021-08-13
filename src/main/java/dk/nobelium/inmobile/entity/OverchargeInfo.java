package dk.nobelium.inmobile.entity;

import java.util.Objects;

public class OverchargeInfo {
    private int price;
    private String countryCode;
    private String shortCodeNumber;
    private OverChargeType type;
    private String invoiceDescription;

    /**
     * @param price              The price in danish øre, e.g. for 2 DKK, specify 200.
     * @param countryCode        The country code for the short code number, e.g. "45" if the short number is "1245"
     * @param shortCodeNumber    The shortcode number, e.g. "1245"
     * @param type               The type of overcharge.
     * @param invoiceDescription The text to be displayed on the end users phone bill
     */
    public OverchargeInfo(int price, String countryCode, String shortCodeNumber, OverChargeType type, String invoiceDescription) {

        if (price <= 0)
            throw new IllegalArgumentException("overchargePrice must be positive");
        if (countryCode == null)
            throw new NullPointerException("countryCode must not be null or empty");
        if (shortCodeNumber == null)
            throw new NullPointerException("shortCodeNumber must not be null or empty");

        this.price = price;
        this.countryCode = countryCode;
        this.shortCodeNumber = shortCodeNumber;
        this.type = type;

        if (invoiceDescription != null) {
            this.invoiceDescription = invoiceDescription;
        } else {
            this.invoiceDescription = "";
        }
    }

    public int getPrice() {
        return price;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getShortCodeNumber() {
        return shortCodeNumber;
    }

    public OverChargeType getType() {
        return type;
    }

    public String getInvoiceDescription() {
        return invoiceDescription;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setShortCodeNumber(String shortCodeNumber) {
        this.shortCodeNumber = shortCodeNumber;
    }

    public void setType(OverChargeType type) {
        this.type = type;
    }

    public void setInvoiceDescription(String invoiceDescription) {
        this.invoiceDescription = invoiceDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OverchargeInfo that = (OverchargeInfo) o;
        return price == that.price && Objects.equals(countryCode, that.countryCode) && Objects.equals(shortCodeNumber, that.shortCodeNumber) && type == that.type && Objects.equals(invoiceDescription, that.invoiceDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, countryCode, shortCodeNumber, type, invoiceDescription);
    }

    @Override
    public String toString() {
        return "OverchargeInfo{" +
               "price=" + price +
               ", countryCode='" + countryCode + '\'' +
               ", shortCodeNumber='" + shortCodeNumber + '\'' +
               ", type=" + type +
               ", invoiceDescription='" + invoiceDescription + '\'' +
               '}';
    }
}
