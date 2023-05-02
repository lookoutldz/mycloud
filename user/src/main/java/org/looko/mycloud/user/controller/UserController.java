package org.looko.mycloud.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.looko.mycloud.user.domain.User;
import org.looko.mycloud.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/id/{id}")
    public Object getById(@PathVariable String id) {
        return userService.getById(id);
    }

    @GetMapping("/all")
    public Object getAll(@RequestParam int pageSize, @RequestParam int pageIndex) {
        Page<User> page = new Page<>(pageIndex, pageSize);
        List<User> users =  userService.listAll(page);
        return Map.of("result", users, "pagination", page);
    }
}
