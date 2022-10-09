package com.yfdl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.common.R;
import com.yfdl.service.TagService;
import com.yfdl.mapper.TagMapper;
import com.yfdl.entity.TagEntity;
import com.yfdl.utils.BeanCopyUtils;
import com.yfdl.vo.TagListVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2022-10-01 17:06:03
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, TagEntity> implements TagService {
    @Override
    public R getTagList() {
        List<TagEntity> list = list();
        List<TagListVo> tagListVos = BeanCopyUtils.copyBeanList(list, TagListVo.class);
        return R.successResult(tagListVos);
    }
}
