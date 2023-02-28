package com.yfdl.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SocketMsg {
    //信息列表，分聊天信息和心跳检测
    private String type;

    //发送人id
    private Long fromId;

    //发送人姓名
    private String nickName;

    //发送人头像
    private String avatar;

    //接受人id
    private Long toId;

    private String message;

    private Date createTime;

}
