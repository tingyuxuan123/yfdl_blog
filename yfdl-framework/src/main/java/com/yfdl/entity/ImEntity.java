package com.yfdl.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * (Im)表实体类
 *
 * @author makejava
 * @since 2023-02-22 17:50:54
 */
@Data
@NoArgsConstructor
@TableName("sg_im")
public class ImEntity implements Serializable {
    private static final long serialVersionUID = -26956528909619044L;

    
        /**
    * 主键
    */
            @TableId(value = "id", type = IdType.AUTO)
        private Long id;

        /**
    * 发送用户id
    */
            private Long fromId;

        /**
    * 接受用户id
    */
            private Long toId;

        /**
    * 消息
    */
            private String message;

        /**
    * 创建时间
    */
            private Date createTime;

            private Integer isRead;

}
