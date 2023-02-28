package com.yfdl.vo.im;

import lombok.Data;

import java.util.Date;

@Data
public class ChatUserListVo {
    private Long id;
    private String nickName;
    private String avatar;
    private Date newTime =new Date();
    private String message ="";
    private Integer count =0;

}
