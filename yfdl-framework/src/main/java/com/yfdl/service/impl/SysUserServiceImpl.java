package com.yfdl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.service.SysUserService;
import com.yfdl.mapper.SysUserMapper;
import com.yfdl.entity.SysUserEntity;
import org.springframework.stereotype.Service;

/**
 * 用户表(SysUser)表服务实现类
 *
 * @author makejava
 * @since 2022-09-25 19:18:32
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserEntity> implements SysUserService {
}
