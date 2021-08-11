package com.book.eurekausergroup.controller;

import com.book.eurekausergroup.dto.UserDto;
import com.book.eurekausergroup.service.UserService;
import com.book.eurekausergroup.vo.RequestUser;
import com.book.eurekausergroup.vo.ResponseUser;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;

@RestController
@RequestMapping("/")
@Slf4j
public class EurekaUserController {
    private final UserService userService;
    private final Environment env;

    @Autowired
    public EurekaUserController(UserService userService, Environment env) {
        this.userService = userService;
        this.env = env;
    }

    @GetMapping("/test")
    public String test() {
        return "User test";
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(user, UserDto.class);
        userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);
        log.debug("회원가입 성공!!");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId) {
        UserDto userDto = userService.getUserByUserId(userId);

        ResponseUser responseUser = new ModelMapper().map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }
}
