package com.example.ozzy.admin.service;

import com.example.ozzy.admin.entity.UserDetail;
import com.example.ozzy.admin.entity.Users;
import com.example.ozzy.admin.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminMapper adminMapper;

    public List<Users> selectUsers(int page, int size) {
        int offset = (page - 1) * size;
        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("limit", size);
        return adminMapper.selectUsers(params);
    }

    public int selectUsersCount() {
        return adminMapper.selectUsersCount();
    }

    public List<UserDetail> selectDetail(int seq) {
        return adminMapper.selectDetail(seq);
    }

}
