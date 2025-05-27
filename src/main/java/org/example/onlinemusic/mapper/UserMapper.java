package org.example.onlinemusic.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.onlinemusic.model.User;

@Mapper
public interface UserMapper {
    User login(User loginUser);

    // 查找是否有某个用户
    User selectByName(String username);
}
