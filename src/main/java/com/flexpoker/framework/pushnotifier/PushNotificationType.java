package com.flexpoker.framework.pushnotifier;

/**
 * Enum used to define the type of PushNotification. PushNotifications are not
 * per-domain, unlike the Command and Event types, which is why this is an enum
 * and not an interface.
 */
public enum PushNotificationType {

    GameListUpdated

}
