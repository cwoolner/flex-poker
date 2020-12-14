package com.flexpoker.framework.pushnotifier

import com.flexpoker.pushnotifications.PushNotification

fun interface PushNotificationHandler<T : PushNotification?> {
    fun handle(pushNotification: T)
}