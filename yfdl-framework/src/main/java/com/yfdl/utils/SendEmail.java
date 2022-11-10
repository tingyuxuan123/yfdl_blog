package com.yfdl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component //将当前类放入组件中，从而能够实现自动注入
public class SendEmail {

    //引入邮件接口
    @Resource
    private JavaMailSender mailSender;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public  void sendEmail(String toEmail){
        String from = "1146713212@qq.com";
        //创建邮件
        SimpleMailMessage message = new SimpleMailMessage();
        //设置发件人信息
        message.setFrom(from);
        //发给谁
        message.setTo(toEmail);
        message.setSubject("您本次的验证码是");
        //生成六位随机验证码
        String verCode = VerCodeGenerateUtil.generateVerCode();
        stringRedisTemplate.opsForValue().set(RedisConstant.BLOG_LOGIN_CODE+toEmail,verCode,RedisConstant.BLOG_LOGIN_CODE_Time, TimeUnit.SECONDS);

        message.setText("尊敬的用户,您好:\n"
                + "\n本次请求的邮件验证码为:" + verCode + ",本验证码 1 分钟内效，请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。\n(这是一封通过自动发送的邮件，请不要直接回复）");

        mailSender.send(message);
    }
}