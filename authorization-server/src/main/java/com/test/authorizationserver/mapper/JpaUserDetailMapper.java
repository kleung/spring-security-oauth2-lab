package com.test.authorizationserver.mapper;

import com.test.authorizationserver.model.userdetail.JpaUserDetail;
import com.test.authorizationserver.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(uses=JpaAuthorityMapper.class)
public interface JpaUserDetailMapper {

    JpaUserDetail mapUserDetail(User user);

}
