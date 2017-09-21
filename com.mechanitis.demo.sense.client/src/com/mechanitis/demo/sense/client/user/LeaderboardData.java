package com.mechanitis.demo.sense.client.user;

import com.mechanitis.demo.sense.service.MessageListener;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;
import static javafx.collections.FXCollections.observableArrayList;

public class LeaderboardData implements MessageListener {
    private static final int NUMBER_OF_LEADERS = 17;
    private final Map<String, TwitterUser> allTwitterUsers = new HashMap<>();

    private ObservableList<TwitterUser> items = observableArrayList();

    ObservableList<TwitterUser> getItems() {
        return items;
    }

    @Override
    public void onMessage(String message) {
        TwitterUser twitterUser = allTwitterUsers.computeIfAbsent(message, TwitterUser::new);
        twitterUser.incrementCount();

        List<TwitterUser> topTweeters = allTwitterUsers.values()
                                                       .stream()
                                                       .sorted(comparingInt(TwitterUser::getTweetCount)
                                                               .reversed())
                                                       .limit(NUMBER_OF_LEADERS)
                                                       .collect(Collectors.toList());
        Platform.runLater(() -> items.setAll(topTweeters));
    }
}
