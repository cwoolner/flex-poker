package com.flexpoker.framework.pushnotifier

import com.flexpoker.pushnotifications.PushNotification

fun interface PushNotificationPublisher {
    fun publish(pushNotification: PushNotification)
}