package com.ljh.mongoMapper;

import com.ljh.domain.UserMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author Jiahui Li
 * @DATE 2019/9/16 10:04
 */
public interface UserMongomMapper extends MongoRepository<UserMongo,String> {
}
