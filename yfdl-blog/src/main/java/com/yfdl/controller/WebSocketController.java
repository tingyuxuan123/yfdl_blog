package com.yfdl.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.yfdl.dto.webScoket.MsgDto;
import com.yfdl.entity.ImEntity;
import com.yfdl.entity.SocketMsg;
import com.yfdl.service.ImService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
@RestController
@ServerEndpoint(value = "/websocket/{userId}")
public class WebSocketController {

    private  ImService imService = SpringUtil.getBean(ImService.class);

    private Long userId;
    private Session session;

    //用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketController> webSocketSet = new CopyOnWriteArraySet<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    //用来记录sessionId和该session进行绑定
    private static Map<Long, Session> map = new HashMap<Long, Session>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) {
        Map<String,Object> message=new HashMap<String, Object>();
        this.session = session;
        this.userId = userId;
        map.put(userId, session);
        webSocketSet.add(this);//加入set中
        System.out.println("有新连接加入:" + userId + ",当前在线人数为" + map.size());
        //this.session.getAsyncRemote().sendText("恭喜" + nickname + "成功连接上WebSocket(其频道号：" + session.getId() + ")-->当前在线人数为：" + webSocketSet.size());
        message.put("type",0); //消息类型，0-连接成功，1-用户消息
        message.put("people",webSocketSet.size()); //在线人数
        message.put("name",userId); //昵称
        message.put("aisle",session.getId()); //频道号
        this.session.getAsyncRemote().sendText(new Gson().toJson(message));
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this); //从set中删除
        System.out.println("有一连接关闭！当前在线人数为" + webSocketSet.size());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("userId") String userId) {



        //从客户端传过来的数据是json数据，所以这里使用jackson进行转换为SocketMsg对象，
        ObjectMapper objectMapper = new ObjectMapper();
        SocketMsg socketMsg;


        try {
            socketMsg = objectMapper.readValue(message, SocketMsg.class);

            //如果前端发布心跳，则转发，示意保持连接
            if(socketMsg.getType().equals("heartbeat")){
                Map<String,Object> m=new HashMap<String, Object>();
                m.put("type","heartbeat");
                session.getAsyncRemote().sendText(new Gson().toJson(m));
                return;
            }

            System.out.println("来自客户端的消息-->" + socketMsg.getFromId() + ": " + socketMsg.getMessage()+"发送给"+socketMsg.getToId());
                /**
                 * 保存到数据库
                 */
                ImEntity imEntity = new ImEntity();
                imEntity.setFromId(socketMsg.getFromId());
                imEntity.setToId(socketMsg.getToId());
                Date date = new Date();
                imEntity.setCreateTime(date);
                imEntity.setMessage(socketMsg.getMessage());



            //单聊.需要找到发送者和接受者.
                Session fromSession = map.get(socketMsg.getFromId());
                Session toSession = map.get(socketMsg.getToId());
                //发送给接受者.
                if (toSession != null) {
                    imEntity.setIsRead(1);
                    toSession.getAsyncRemote().sendText(message);
                }else{
                    imEntity.setIsRead(0);
                }
              boolean save = imService.save(imEntity);

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }



}
