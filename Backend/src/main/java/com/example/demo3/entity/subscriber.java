package com.example.demo3.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "subinfo", schema = "certmanager")
public class subscriber {

    @Id
    @Column(name = "subscriber_id")
    private String subscriberId;

    @Column(name = "subscriber_name")
    private String subscriberName;

    @Column(name = "subscription_id")
    private String subscriptionId;

    @Column(name = "subscription_name")
    private String subscriptionName;

    @Column(name = "san")
    private String san;

    // Getters and setters

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getSubscriberName() {
        return subscriberName;
    }

    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getSubscriptionName() {
        return subscriptionName;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    public String getSan() {
        return san;
    }

    public void setSan(String san) {
        this.san = san;
    }
}
