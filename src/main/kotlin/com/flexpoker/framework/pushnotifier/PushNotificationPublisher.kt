package com.flexpoker.framework.pushnotifier

import com.flexpoker.pushnotifications.PushNotification

interface PushNotificationPublisher {
    fun publish(pushNotification: PushNotification)
}