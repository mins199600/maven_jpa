package kr.co.joneconsulting.myrestfulservice.dao;

import kr.co.joneconsulting.myrestfulservice.bean.User;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class UserDaoService {
    private static List<User> users = new ArrayList<>();
    private static int usersCount = 3;

    static {
        users.add(new User(1,"sumin",new Date()));
        users.add(new User(2,"moonsu",new Date()));
        users.add(new User(3,"choco",new Date()));
    }

    //전체 데이터 조회
    public List<User> findAll(){
        return users;
    }

    //새로운 유저를 저장
    public User save(User user){
        if(user.getId() == null){
            user.setId(++usersCount);
        }

        if (user.getJoinDate()==null){
            user.setJoinDate(new Date());
        }
        users.add(user);
        return user;
    }
    public User findOne(int id){
        for(User user : users){
            if (user.getId() == id){
                return user;
            }
        }
        return null;
    }
}
