package com.umr.myhrm_system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.umr.myhrm_common_model.domain.system.response.UserResult;
import com.umr.myhrm_common_model.domain.system.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {
    boolean saveRole(String userId, List<String> roleIds);
    boolean saveOne(User user);
    IPage<User> getList(String companyID, Map<String, Object> queryMap, int page, int size);
    UserResult getOne(String id);
    boolean deleteOne(String id);
    boolean updateOne(User user);
    User getUser(String id);
    User login(String mobile);
    String upload(String id, MultipartFile img) throws IOException;
}
