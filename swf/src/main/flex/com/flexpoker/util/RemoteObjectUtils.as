package com.flexpoker.util {

    import mx.rpc.remoting.RemoteObject;
    import mx.messaging.ChannelSet;
    import mx.messaging.channels.SecureAMFChannel;

    public class RemoteObjectUtils {

        public static function addChannelSetToRemoteObject(remoteObject:RemoteObject):void {
            var channelSet:ChannelSet = new ChannelSet();
            var channel:SecureAMFChannel = new SecureAMFChannel("ch1", "https://flexpoker.com/poker/messagebroker/amf");
            channelSet.addChannel(channel);
            remoteObject.channelSet = channelSet;
        }

    }

}