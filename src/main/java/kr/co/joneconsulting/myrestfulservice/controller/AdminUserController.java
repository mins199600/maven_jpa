package kr.co.joneconsulting.myrestfulservice.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import jakarta.validation.Valid;
import kr.co.joneconsulting.myrestfulservice.bean.AdminUser;
import kr.co.joneconsulting.myrestfulservice.bean.User;
import kr.co.joneconsulting.myrestfulservice.dao.UserDaoService;
import kr.co.joneconsulting.myrestfulservice.exception.UserNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminUserController {
    private UserDaoService service;

    public AdminUserController(UserDaoService service) {
        this.service = service;
    }

    //우리는 admin/users/{id} 값을 받는다
    @GetMapping("/users/{id}")
    public MappingJacksonValue retrieveUser4Admin(@PathVariable int id){
        User user = service.findOne(id);

        AdminUser adminuser = new AdminUser();
        if(user == null){
            throw new UserNotFoundException(String.format("id[%s] not found", id));
        } else {
            BeanUtils.copyProperties(user,adminuser);
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name","joinDate","ssn");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);
        MappingJacksonValue mapping = new MappingJacksonValue(adminuser);
        mapping.setFilters(filters);
        return mapping;
    }

}
