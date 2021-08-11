package com.book.eurekausergroup.dao;


import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(String userName);
    UserEntity findByUserId(String userId);
}
