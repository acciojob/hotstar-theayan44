package com.driver.Transformer;

import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;

import java.util.Date;

public class SubscriptionTransformer {
    public static Subscription subscriptionDtoToSubscription(SubscriptionEntryDto subscriptionEntryDto){
        Subscription subscription = new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        subscription.setStartSubscriptionDate(new Date());

        int totalAmountPaid = 0;

        switch(subscriptionEntryDto.getSubscriptionType().toString()){
            case "BASIC": totalAmountPaid = 500 + (200 * subscriptionEntryDto.getNoOfScreensRequired());
            case "PRO": totalAmountPaid = 800 + (250 * subscriptionEntryDto.getNoOfScreensRequired());
            case "ELITE": totalAmountPaid = 1000 + (350 * subscriptionEntryDto.getNoOfScreensRequired());
        }

        subscription.setTotalAmountPaid(totalAmountPaid);

        return subscription;
    }
}
