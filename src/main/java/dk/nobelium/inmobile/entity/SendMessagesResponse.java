package dk.nobelium.inmobile.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

@XmlRootElement(name = "reply")
@XmlAccessorType(XmlAccessType.FIELD)
public class SendMessagesResponse {

    @XmlElement(name = "recipient")
    private List<MsisdnAndMessageId> messageIds;

    public SendMessagesResponse() {
    }

    public SendMessagesResponse(List<MsisdnAndMessageId> messageIds) {
        this.messageIds = messageIds;
    }

    public List<MsisdnAndMessageId> getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(List<MsisdnAndMessageId> messageIds) {
        this.messageIds = messageIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendMessagesResponse that = (SendMessagesResponse) o;
        return Objects.equals(messageIds, that.messageIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageIds);
    }

    @Override
    public String toString() {
        return "SendMessagesResponse{" +
               "messageIds=" + messageIds +
               '}';
    }
}
