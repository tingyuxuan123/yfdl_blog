package com.yfdl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yfdl.entity.CommentEntity;
import com.yfdl.vo.article.ArticleAudit;
import com.yfdl.vo.comment.CommentAuditVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 评论表(Comment)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-28 13:45:57
 */
@Mapper
public interface CommentMapper extends BaseMapper<CommentEntity> {
    CommentAuditVo[] commentAuditList(@Param("skipNumber") Long skipNumber, @Param("pageSize") Long pageSize, @Param("userName") String userName, @Param("audit") Integer audit);

    Long getCount();
}
