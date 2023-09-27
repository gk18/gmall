package com.king.gmall.oauth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.king.gmall.model.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/***
 * ClassName: UserInfoMapper
 * Package: com.king.gmall.oauth.mapper
 * @author GK
 * @date 2023/9/27 20:39
 * @description
 * @version 1.0
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
