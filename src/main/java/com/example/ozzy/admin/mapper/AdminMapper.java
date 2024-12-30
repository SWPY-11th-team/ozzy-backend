package com.example.ozzy.admin.mapper;

import com.example.ozzy.admin.entity.UserDetail;
import com.example.ozzy.admin.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMapper {

    List<Users> selectUsers(Map<String, Object> map);
    int selectUsersCount();
    List<UserDetail> selectDetail(@Param("seq") int seq);

}
