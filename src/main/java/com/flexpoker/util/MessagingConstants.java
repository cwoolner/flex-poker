package com.flexpoker.util;

public class MessagingConstants {

    public static final String CHAT_LOBBY = "/topic/chat/lobby";

    public static final String CHAT_GAME = "/topic/chat/game/%s";

    public static final String CHAT_TABLE = "/topic/chat/game/%s/table/%s";

    public static final String TABLE_STATUS = "/topic/game/%s/table/%s";

    public static final String TICK_ACTION_ON_TIMER = "/topic/game/%s/table/%s/action-on-tick";

    public static final String PERSONAL_TABLE_STATUS = "/queue/personaltablestatus";

    public static final String POCKET_CARDS = "/queue/pocketcards";

    public static final String OPEN_GAMES_FOR_USER = "/queue/opengamesforuser";

    public static final String OPEN_TABLE_FOR_USER = "/queue/opentable";

    public static final String GAMES_UPDATED = "/topic/availabletournaments";

}
