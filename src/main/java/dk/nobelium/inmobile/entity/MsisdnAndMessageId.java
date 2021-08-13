package dk.nobelium.inmobile.entity;


import javax.xml.bind.annotation.*;
import java.util.Objects;

@XmlRootElement(name = "recipient")
@XmlAccessorType(XmlAccessType.FIELD)
public class MsisdnAndMessageId {

    @XmlAttribute(name = "msisdn")
    private String msisdn;

    @XmlAttribute(name = "id")
    private String messageId;

    public MsisdnAndMessageId() {
    }

    public MsisdnAndMessageId(String msisdn, String messageId) {
        this.msisdn = msisdn;
        this.messageId = messageId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MsisdnAndMessageId that = (MsisdnAndMessageId) o;
        return Objects.equals(msisdn, that.msisdn) && Objects.equals(messageId, that.messageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(msisdn, messageId);
    }

    @Override
    public String toString() {
        return "MsisdnAndMessageId{" +
               "msisdn='" + msisdn + '\'' +
               ", messageId='" + messageId + '\'' +
               '}';
    }
}
