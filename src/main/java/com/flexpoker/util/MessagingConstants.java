package com.flexpoker.util;

public class MessagingConstants {

    public static final String CHAT_GLOBAL_USER = "/topic/chat/global/user";

    public static final String CHAT_GLOBAL_SYSTEM = "/topic/chat/global/system";
    
    public static final String CHAT_GAME_USER = "/topic/chat/game/%s/user";
    
    public static final String CHAT_GAME_SYSTEM = "/topic/chat/game/%s/system";
    
    public static final String CHAT_TABLE_USER = "/topic/chat/game/%s/table/%s/user";
    
    public static final String CHAT_TABLE_SYSTEM = "/topic/chat/game/%s/table/%s/system";
    
    public static final String CHAT_PERSONAL_USER = "/topic/chat/personal/user";
    
    public static final String CHAT_PERSONAL_SYSTEM = "/topic/chat/personal/system";

    public static final String GAME_STATUS_EVENTS = "/topic/game/%s";
    
    public static final String OPEN_GAMES_FOR_USER = "/queue/opengamesforuser";

}
