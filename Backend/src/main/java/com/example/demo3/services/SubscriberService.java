package com.example.demo3.services;
import com.example.demo3.entity.subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.example.demo3.repository.SubscriberRepository;

@Service
public class SubscriberService {

    @Autowired
    private SubscriberRepository subscriberRepository;

    public Set<String> getSubscriptionIdsBySubscriber(String subscriberId) {
        List<subscriber> subscribers = subscriberRepository.findBySubscriberId(subscriberId);
        return subscribers.stream()
                .map(subscriber::getSubscriptionId)
                .collect(Collectors.toSet()); // Using Set to ensure unique values
    }

    public List<String> getSansBySubscription(String subscriptionId) {
        List<subscriber> subscribers = subscriberRepository.findBySubscriptionId(subscriptionId);
        return subscribers.stream()
                .map(subscriber::getSan)
                .collect(Collectors.toList());
    }
}
