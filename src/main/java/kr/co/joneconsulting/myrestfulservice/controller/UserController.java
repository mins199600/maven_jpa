package kr.co.joneconsulting.myrestfulservice.controller;

import jakarta.validation.Valid;
import kr.co.joneconsulting.myrestfulservice.bean.User;
import kr.co.joneconsulting.myrestfulservice.dao.UserDaoService;
import kr.co.joneconsulting.myrestfulservice.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class UserController {
    private final UserDaoService userDaoService;
    private UserDaoService service;

    public UserController(UserDaoService service, UserDaoService userDaoService) {
        this.service = service;
        this.userDaoService = userDaoService;
    }
    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
        return service.findAll();
    }
    @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable int id){
        User user = service.findOne(id);

        if(user == null){
            throw new UserNotFoundException(String.format("id[%s] not found", id));
        }
        return user;
    }
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
         User savedUser = service.save(user);

         URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                 .path("/{id}")
                 .buildAndExpand(savedUser.getId())
                 .toUri();

         return ResponseEntity.created(location).build();
    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteUser(@PathVariable int id){
        User deletedUser = service.deleteById(id);

        if(deletedUser == null){
            throw new UserNotFoundException(String.format("id[%s] not found", id));
        }
        return ResponseEntity.noContent().build();
    }
}
