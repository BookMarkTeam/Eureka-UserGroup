package com.book.eurekausergroup.service;

import com.book.eurekausergroup.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto getUserDetailByEmail(String userName);
    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId);
}
