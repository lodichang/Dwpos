package com.dw.component;

import com.ablegenius.netty.client.MessageNonAck;
import com.ablegenius.netty.common.Message;
import com.alibaba.fastjson.JSONObject;
import com.dw.dto.NettyMessageDto;
import com.dw.enums.NettyMessageTypeEnum;
import com.dw.netty.NettyClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.ablegenius.netty.common.NettyCommonProtocol.REQUEST;

/**
 * Created by lodi on 2018/5/24.
 */
@Component
public class NettyComponent {
    @Autowired
    private NettyClient nettyClient;

    public void sendMessage(NettyMessageTypeEnum nettyMessageTypeEnum){
        //補單后发通知刷新檯號
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Channel channel = nettyClient.getChannel();
                NettyMessageDto msgDto = new NettyMessageDto(nettyMessageTypeEnum,null);
                String text = JSONObject.toJSONString(msgDto);
                Message message = new Message();
                message.sign(REQUEST);
                message.setClientId(NettyClient.NETTY_CLIENT_ID);
                message.data(text);
                channel.writeAndFlush(message).addListener((ChannelFutureListener) future -> {});
                //防止对象处理发生异常的情况
                MessageNonAck msgNonAck = new MessageNonAck(message, channel);
                nettyClient.getClientConnector().addNeedAckMessageInfo(msgNonAck);
                return null;
            }
        };
        new Thread(task).start();
    }


    public void sendMessage(NettyMessageTypeEnum nettyMessageTypeEnum,Map<String,String> tableMap){
        //補單后发通知刷新檯號
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                Channel channel = nettyClient.getChannel();
                NettyMessageDto msgDto = new NettyMessageDto(nettyMessageTypeEnum,JSONObject.toJSONString(tableMap));
                String text = JSONObject.toJSONString(msgDto);
                Message message = new Message();
                message.sign(REQUEST);
                message.setClientId(NettyClient.NETTY_CLIENT_ID);
                message.data(text);
                channel.writeAndFlush(message).addListener((ChannelFutureListener) future -> {});
                //防止对象处理发生异常的情况
                MessageNonAck msgNonAck = new MessageNonAck(message, channel);
                nettyClient.getClientConnector().addNeedAckMessageInfo(msgNonAck);
                return null;
            }
        };
        new Thread(task).start();
    }


}
