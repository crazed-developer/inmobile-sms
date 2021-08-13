package dk.nobelium.inmobile.entity;

import java.util.Objects;

public class RefundMessage implements ISmsMessage {
    private String messageIdToRefund;
    private String messageText;
    private String messageId;

    /**
     *
     * @param messageIdToRefund The id of the overcharged message which should be refunded.
     * @param messageText The text sent to the mobile user with a confirmation of the refund
     * @param messageId The id of this message (Optional). If not specified, the remote api will generate an id.
     */
    public RefundMessage(String messageIdToRefund, String messageText, String messageId) {

        if (messageIdToRefund == null || messageIdToRefund.isBlank()) {
            throw new IllegalArgumentException("MessageIdToRefundId needs to be specified.");
        }

        if (messageText == null || messageText.isBlank()) {
            throw new IllegalArgumentException("messageText needs to be specified.");
        }

        this.messageIdToRefund = messageIdToRefund;
        this.messageText = messageText;
        this.messageId = messageId;
    }

    public String getMessageIdToRefund() {
        return messageIdToRefund;
    }

    public void setMessageIdToRefund(String messageIdToRefund) {
        this.messageIdToRefund = messageIdToRefund;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
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
        RefundMessage that = (RefundMessage) o;
        return Objects.equals(messageIdToRefund, that.messageIdToRefund) && Objects.equals(messageText, that.messageText) && Objects.equals(messageId, that.messageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageIdToRefund, messageText, messageId);
    }

    @Override
    public String toString() {
        return "RefundMessage{" +
               "messageIdToRefund='" + messageIdToRefund + '\'' +
               ", messageText='" + messageText + '\'' +
               ", messageId='" + messageId + '\'' +
               '}';
    }
}
