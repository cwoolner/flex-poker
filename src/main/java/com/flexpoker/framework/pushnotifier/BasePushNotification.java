package com.flexpoker.framework.pushnotifier;

public abstract class BasePushNotification implements PushNotification {

    private final PushNotificationType type;

    public BasePushNotification(PushNotificationType type) {
        this.type = type;
    }

    @Override
    public PushNotificationType getType() {
        return type;
    }

}
