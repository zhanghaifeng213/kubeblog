package com.qingfeng.blog.config;

import com.qingfeng.blog.po.Tag;
import com.qingfeng.blog.po.Type;
import com.qingfeng.blog.po.User;
import com.qingfeng.blog.service.TagService;
import com.qingfeng.blog.service.TypeService;
import com.qingfeng.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class InitAdminConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @Bean
    public User initAdminUser() {

        //init types
        List<Type> types = typeService.listType();

        if(types.isEmpty()){
            Type newType = new Type();
            newType.setName("技术");
            newType.setId(Long.valueOf(1));
            typeService.saveType(newType);
        }

        //init tags
        List<Tag> tags = tagService.listTag();
        if(tags.isEmpty()){
            Tag newTag = new Tag();
            newTag.setId(Long.valueOf(1));
            newTag.setName("docker");
            tagService.saveTag(newTag);
        }

//        init Admin user
        User user = userService.checkuser("admin", "password");
        if(user == null){
            return userService.saveUser("admin","password");
        } else {
            return user;
        }



    }
}