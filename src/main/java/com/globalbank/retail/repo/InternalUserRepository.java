package com.globalbank.retail.repo;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.globalbank.retail.entity.InternalUser;
  
public interface InternalUserRepository extends MongoRepository<InternalUser, String> {
}