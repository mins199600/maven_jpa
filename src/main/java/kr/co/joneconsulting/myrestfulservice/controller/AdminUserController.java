package kr.co.joneconsulting.myrestfulservice.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import kr.co.joneconsulting.myrestfulservice.bean.AdminUser;
import kr.co.joneconsulting.myrestfulservice.bean.AdminUserV2;
import kr.co.joneconsulting.myrestfulservice.bean.User;
import kr.co.joneconsulting.myrestfulservice.dao.UserDaoService;
import kr.co.joneconsulting.myrestfulservice.exception.UserNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminUserController {
    private UserDaoService service;

    public AdminUserController(UserDaoService service) {
        this.service = service;
    }

    //우리는 admin/users/{id} 값을 받는다
    //@GetMapping("/v1/users/{id}")
    //@GetMapping(value = "/users/{id}", params = "version=1")
    @GetMapping(value = "/users/{id}", produces = "application/vnd.company.appV1+json")
    public MappingJacksonValue retrieveUser4Admin(@PathVariable int id){
        User user = service.findOne(id);

        AdminUser adminUser = new AdminUser();
        if(user == null){
            throw new UserNotFoundException(String.format("id[%s] not found", id));
        } else {
            BeanUtils.copyProperties(user,adminUser);
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name","joinDate","ssn");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);
        MappingJacksonValue mapping = new MappingJacksonValue(adminUser);
        mapping.setFilters(filters);
        return mapping;
    }

    //@GetMapping("/v2/users/{id}")
    //@GetMapping(value = "/users/{id}", params = "version=2")
    //@GetMapping(value = "/users/{id}", headers = "X-API-VERSION=2")
    @GetMapping(value = "/users/{id}", produces = "application/vnd.company.appV2+json")
    public MappingJacksonValue retrieveUser4AdminV2(@PathVariable int id){
        User user = service.findOne(id);

        AdminUserV2 adminUser = new AdminUserV2();
        if(user == null){
            throw new UserNotFoundException(String.format("id[%s] not found", id));
        } else {
            BeanUtils.copyProperties(user,adminUser);
            adminUser.setGrade("VIP");
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name","joinDate","grade");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2",filter);
        MappingJacksonValue mapping = new MappingJacksonValue(adminUser);
        mapping.setFilters(filters);
        return mapping;
    }


    @GetMapping("/users")
    public MappingJacksonValue retrieveAllUser4Admin(){
        List<User> users = service.findAll();

        List<AdminUser> adminUsers = new ArrayList<>();
        AdminUser adminUser = null;
        for(User user : users){
            adminUser = new AdminUser();
            BeanUtils.copyProperties(user,adminUser);

            adminUsers.add(adminUser);
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name","joinDate","ssn");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);

        MappingJacksonValue mapping = new MappingJacksonValue(adminUsers);
        mapping.setFilters(filters);
        return mapping;
    }

}
