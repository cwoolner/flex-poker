package com.flexpoker.pushnotifications;

import com.flexpoker.framework.pushnotifier.BasePushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationType;

public class GameListUpdatedPushNotification extends BasePushNotification {

    private static final PushNotificationType TYPE = PushNotificationType.GameListUpdated;

    public GameListUpdatedPushNotification() {
        super(TYPE);
    }

}
