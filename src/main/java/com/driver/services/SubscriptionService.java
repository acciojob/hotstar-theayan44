package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.Transformer.SubscriptionTransformer;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){
        Subscription subscription = SubscriptionTransformer.subscriptionDtoToSubscription(subscriptionEntryDto);
        Optional<User> optionalUser = userRepository.findById(subscriptionEntryDto.getUserId());
        User user = optionalUser.get();
        subscription.setUser(user);
        Subscription savedSubscription = subscriptionRepository.save(subscription);

        user.setSubscription(savedSubscription);
        userRepository.save(user);
        //Save The subscription Object into the Db and return the total Amount that user has to pay

        return savedSubscription.getTotalAmountPaid();
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.get();

        if(user.getSubscription().getSubscriptionType() == SubscriptionType.ELITE){
            throw new Exception("Already the best Subscription");
        }

        Subscription subscription = user.getSubscription();
        int currentAmountPaid = subscription.getTotalAmountPaid();

        if(subscription.getSubscriptionType() == SubscriptionType.BASIC){
            subscription.setSubscriptionType(SubscriptionType.PRO);
            subscription.setTotalAmountPaid(800 + (250 * subscription.getNoOfScreensSubscribed()));
        }else{
            subscription.setSubscriptionType(SubscriptionType.ELITE);
            subscription.setTotalAmountPaid(1000 + (350 * subscription.getNoOfScreensSubscribed()));
        }

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        user.setSubscription(savedSubscription);
        userRepository.save(user);

        return savedSubscription.getTotalAmountPaid() - currentAmountPaid;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb

        List<Subscription> subscriptionList = subscriptionRepository.findAll();
        int totalRevenue = 0;
        for(Subscription currSubscription : subscriptionList){
            totalRevenue += currSubscription.getTotalAmountPaid();
        }

        return totalRevenue;
    }

}
