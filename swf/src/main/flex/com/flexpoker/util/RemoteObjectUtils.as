package com.flexpoker.util {

    import mx.rpc.remoting.RemoteObject;
    import mx.messaging.ChannelSet;
    import mx.messaging.channels.AMFChannel;

    public class RemoteObjectUtils {

        public static function addChannelSetToRemoteObject(remoteObject:RemoteObject):void {
            var channelSet:ChannelSet = new ChannelSet();
            var channel:AMFChannel = new AMFChannel("ch1", "http://flexpoker.com/poker/messagebroker/amf");
            channelSet.addChannel(channel);
            remoteObject.channelSet = channelSet;
        }

    }

}