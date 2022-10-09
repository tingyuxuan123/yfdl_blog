package com.yfdl.service;

import com.yfdl.common.R;
import com.yfdl.entity.UserEntity;

public interface AdminLoginService {

    R login(UserEntity user);

    R logout();


}
