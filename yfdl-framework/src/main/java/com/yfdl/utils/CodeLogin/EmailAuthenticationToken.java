package com.yfdl.utils.CodeLogin;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class EmailAuthenticationToken extends AbstractAuthenticationToken {
    //邮箱
    private String email;

    //密码
    private String code;

    /**
     *
     * @param authorities 权限集合
     * @param email  登录身份
     * @param code  登录凭证
     */
    public EmailAuthenticationToken(Collection<? extends GrantedAuthority> authorities, String email, String code) {
        super(authorities);
        this.email = email;
        this.code = code;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return code;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }

    // 擦除登录凭据
    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        code = null;
    }

    // 获取认证token的名字
    @Override
    public String getName() {
        return "mobilePhoneAuthenticationToken";
    }

}
