package com.flexpoker.util

object MessagingConstants {
    const val CHAT_LOBBY = "/topic/chat/lobby"
    const val CHAT_GAME = "/topic/chat/game/%s"
    const val CHAT_TABLE = "/topic/chat/game/%s/table/%s"
    const val TABLE_STATUS = "/topic/game/%s/table/%s"
    const val TICK_ACTION_ON_TIMER = "/topic/game/%s/table/%s/action-on-tick"
    const val PERSONAL_TABLE_STATUS = "/queue/personaltablestatus"
    const val POCKET_CARDS = "/queue/pocketcards"
    const val OPEN_GAMES_FOR_USER = "/queue/opengamesforuser"
    const val OPEN_TABLE_FOR_USER = "/queue/opentable"
    const val GAMES_UPDATED = "/topic/availabletournaments"
}