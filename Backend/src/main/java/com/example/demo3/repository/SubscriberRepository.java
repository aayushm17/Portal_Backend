package com.example.demo3.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import com.example.demo3.entity.subscriber;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

public interface SubscriberRepository extends JpaRepository<subscriber, String> {
    List<subscriber> findBySubscriberId(String subscriberId);
    List<subscriber> findBySubscriptionId(String subscriptionId);
}
