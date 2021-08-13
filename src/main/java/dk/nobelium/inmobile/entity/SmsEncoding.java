package dk.nobelium.inmobile.entity;

public enum SmsEncoding {
    GSM_7("gsm7"),
    UTF_8("utf-8"),
    GSM_7_EXTENDED("gsm7extended");

    public final String value;

    SmsEncoding(String value) {
        this.value = value;
    }
}
