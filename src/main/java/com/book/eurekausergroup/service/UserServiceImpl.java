package com.book.eurekausergroup.service;

import com.book.eurekausergroup.dao.UserEntity;
import com.book.eurekausergroup.dao.UserRepository;
import com.book.eurekausergroup.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @brief 유저가 처음에 로그인했을 때 유저를 DB에 저장
     * @param userDto : 처음에 로그인한 UserDto
     * @return UserDto : 일관성을 위해 DB에 저장한 UserEntity를 model 사용해 UserDto로 반환
     */
    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);

        userRepository.save(userEntity);

        return mapper.map(userEntity, UserDto.class);
    }

    /**
     * @brief 이메일로 UserDto 객체 DB에서 가져오는 함수
     * @data 21/07/18
     * @param email : 로그인한 이메일
     * @return UserDto : 로그인한 이메일의 UserDto 객체
     */
    @Override
    public UserDto getUserDetailByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null)
            throw new UsernameNotFoundException(email);

        return new ModelMapper().map(userEntity, UserDto.class);
    }

    /**
     * @brief 아이디로 UserDto 객체 DB에서 가져오는 함수
     * @date 21/07/18
     * @param userId : 로그인한 아이디
     * @return UserDto : 로그인한 아이디의 UserDto 객체
     */
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null)
            throw new UsernameNotFoundException(userId);

        return new ModelMapper().map(userEntity, UserDto.class);
    }

    /**
     * @brief 유저 이름으로 Spring Security의 UserDetails 객체를 반환하는 함수.
     * @param userName : 로그인한 DTO 객체의 유저 이름
     * @return UserDetails : Spring Security의 UserDetails 객체를 반환
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(userName);

        if (userEntity == null)
            throw new UsernameNotFoundException(userName);

        return new User(userEntity.getEmail(), userEntity.getEId(),
                true, true, true, true, new ArrayList<>());
    }


}
