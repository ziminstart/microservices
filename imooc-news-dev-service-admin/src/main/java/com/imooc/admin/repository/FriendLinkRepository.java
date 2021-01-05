package com.imooc.admin.repository;

import com.imooc.model.mo.FriendLinkMO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendLinkRepository extends MongoRepository<FriendLinkMO,String> {

    List<FriendLinkMO> getAllByIsDelete(Integer isDelete);

}
