package com.yfdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yfdl.common.R;
import com.yfdl.entity.LinkEntity;
import com.yfdl.vo.LinkVo;

import java.util.List;

/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2022-09-25 17:51:55
 */
public interface LinkService extends IService<LinkEntity> {
    R<List<LinkVo>> getAllLink();
}
