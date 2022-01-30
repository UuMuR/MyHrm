package com.umr.myhrm_common_model.domain.system.response;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户简洁数据返回对象
 */
@Data
@NoArgsConstructor
public class UserSimpleResult {
    private String id;
    private String username;

    public UserSimpleResult(String id,String username){
        this.id = id;
        this.username = username;
    }
}
