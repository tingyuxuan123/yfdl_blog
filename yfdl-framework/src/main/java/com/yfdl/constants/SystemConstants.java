package com.yfdl.constants;

public class SystemConstants {

    /**
     * 文章是草稿类型
     */
    public static final char ARTICLE_STATUS_DRAFT = '1';

    /**
     * 文章是正常发布
     */
    public static final char ARTICLE_STATUS_NORMAL = '0';

    /**
     * 分类正常使用
     */
    public static final char CATEGORY_STATUS_NORMAL = '0';

    /**
     * 分类被禁用
     */
    public static final char CATEGORY_STATUS_DISABLE = '1';

    /**
     * 友连状态为审核通过
     */
    public static final char LINK_STATUS_NORMAL = '0';

    /**
     * 友连状态为未通过
     */
    public static final char LINK_STATUS_NOTNORMAL = '1';

    /**
     * 友链待审核
     */
    public static final char LINK_STATUS_WAIT ='2';

    /**
     *  评论类型： 友链
     */
    public static final char LINK_COMMENT = '1';

    /**
     * 评论类型:文章
     */

    public static final char ARTICLE_COMMENT ='0';

    /**
     * 菜单类别为主菜单
     */
    public static final char MENU = 'M';
    /**
     *菜单类别为 按钮
     */
    public static final char BUTTON = 'F';
    /**
     * 菜单类别为 分类 （组菜单下的子类）
     */
    public static final char Category = 'C';

    /**
     *普通用户
     */
    public static final Long USER_ROLE_COMMON = 3L;

    /**
     * 管理员
     */
    public static final Long USER_ROLE_ADMIN = 2L;

    /**
     * 超级管理员
     */
    public static final Long USER_ROLE_SUPERADMIN= 1L;

}
