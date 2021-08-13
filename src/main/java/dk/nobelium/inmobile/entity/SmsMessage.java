package dk.nobelium.inmobile.entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SmsMessage implements ISmsMessage {
    private String messageId;
    private boolean flash;
    private String msisdn;
    private String text;
    private String senderName;
    private SmsEncoding encoding;
    private OverchargeInfo overChargeInfo;
    private LocalDateTime sendTime;
    private Duration expireIn;
    private boolean respectBlacklist;

    /**
     * @param messageId        The message id used to identify the message.
     *                         If NULL or empty string, a message id will be generated on the API server side and returned in the response.
     * @param flash            If true, message is a flash message
     * @param msisdn           The phone number to send to, including country code, e.g. 4512345678
     * @param text             The text message in the SMS
     * @param senderName       The sendername, keep this between 3 and 11 chars.
     * @param encoding         The encoding to use.
     * @param overChargeInfo   If NULL, the message is not overcharged.
     * @param sendTime         The time for the message to be sent. use NULL to send immediately.
     * @param expireIn         The amount of time the message is valid for. If the time passes before the message is sent, the message will not be sent.
     *                         If null, validity period for the message, is not set.
     * @param respectBlacklist Whether or not blacklisting should be respected when sending messages.
     */
    public SmsMessage(String messageId, boolean flash, String msisdn, String text, String senderName, SmsEncoding encoding, OverchargeInfo overChargeInfo, LocalDateTime sendTime, Duration expireIn, boolean respectBlacklist) {
        this.messageId = messageId;
        this.flash = flash;
        this.msisdn = msisdn;
        this.text = text;
        this.senderName = senderName;
        this.encoding = encoding;
        this.overChargeInfo = overChargeInfo;
        this.sendTime = sendTime;
        this.expireIn = expireIn;
        this.respectBlacklist = respectBlacklist;
    }

    public String getMessageId() {
        return messageId;
    }

    public boolean isFlash() {
        return flash;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public String getText() {
        return text;
    }

    public String getSenderName() {
        return senderName;
    }

    public SmsEncoding getEncoding() {
        return encoding;
    }

    public OverchargeInfo getOverChargeInfo() {
        return overChargeInfo;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public Duration getExpireIn() {
        return expireIn;
    }

    public boolean isRespectBlacklist() {
        return respectBlacklist;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setOverChargeInfo(OverchargeInfo overChargeInfo) {
        this.overChargeInfo = overChargeInfo;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public void setExpireIn(Duration expireIn) {
        this.expireIn = expireIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmsMessage that = (SmsMessage) o;
        return flash == that.flash && respectBlacklist == that.respectBlacklist && Objects.equals(messageId, that.messageId) && Objects.equals(msisdn, that.msisdn) && Objects.equals(text, that.text) && Objects.equals(senderName, that.senderName) && encoding == that.encoding && Objects.equals(overChargeInfo, that.overChargeInfo) && Objects.equals(sendTime, that.sendTime) && Objects.equals(expireIn, that.expireIn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, flash, msisdn, text, senderName, encoding, overChargeInfo, sendTime, expireIn, respectBlacklist);
    }

    public int commonHashCode() {
        // Objects.hash handles nulls, so no need to check if overcharge info is null. As in C# reference implementation
        return Objects.hash(encoding, senderName, text, sendTime, overChargeInfo);
    }

    @Override
    public String toString() {
        return "SmsMessage{" +
               "messageId='" + messageId + '\'' +
               ", flash=" + flash +
               ", msisdn='" + msisdn + '\'' +
               ", text='" + text + '\'' +
               ", senderName='" + senderName + '\'' +
               ", encoding=" + encoding +
               ", overChargeInfo=" + overChargeInfo +
               ", sendTime=" + sendTime +
               ", expireIn=" + expireIn +
               ", respectBlacklist=" + respectBlacklist +
               '}';
    }
}
