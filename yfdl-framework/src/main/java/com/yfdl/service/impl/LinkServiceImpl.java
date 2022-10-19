package com.yfdl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.common.R;
import com.yfdl.constants.SystemConstants;
import com.yfdl.service.LinkService;
import com.yfdl.mapper.LinkMapper;
import com.yfdl.entity.LinkEntity;
import com.yfdl.utils.BeanCopyUtils;
import com.yfdl.vo.LinkVo;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2022-09-25 17:51:55
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, LinkEntity> implements LinkService {
    @Override
    public R<List<LinkVo>> getAllLink() {

        LambdaQueryWrapper<LinkEntity> linkEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        linkEntityLambdaQueryWrapper.eq(LinkEntity::getStatus, SystemConstants.CATEGORY_STATUS_NORMAL);

        List<LinkEntity> list = list(linkEntityLambdaQueryWrapper);



        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(list, LinkVo.class);

        return R.successResult(linkVos);
    }
}
