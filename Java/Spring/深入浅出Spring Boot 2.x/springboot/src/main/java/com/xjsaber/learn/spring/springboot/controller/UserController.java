package com.xjsaber.learn.spring.springboot.controller;

import com.xjsaber.learn.spring.springboot.pojo.MongoUser;
import com.xjsaber.learn.spring.springboot.pojo.User;
import com.xjsaber.learn.spring.springboot.pojo.UserValidator;
import com.xjsaber.learn.spring.springboot.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author xjsaber
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 定义请求 RequestMapping
     * 转换为JSON RequestBody
     * @param id 序号
     * @param userName 用户名称
     * @param note 编号
     * @return
     */
    @RequestMapping("/print")
    @ResponseBody
    public User printUser(Long id, String userName, String note) {
        User user = new User();
        user.setId(id);
        user.setUsername(userName);
        user.setNote(note);
        // 若user = null， 则执行afterthrowing方法
        userService.printUser(user);
        return user;
    }

    /**
     * TODO 出错，review的时候关注下
     * 定义请求
     * @param id
     * @param userName
     * @param note
     * @return
     */
    @RequestMapping("/vp")
    @ResponseBody
    public User validateAndPrint(Long id, String userName, String note){
        User user = new User();
        user.setId(id);
        user.setUsername(userName);
        user.setNote(note);
        // 强制转换
        UserValidator userValidator = (UserValidator)userService;
        if (userValidator.validator(user)){
            userService.printUser(user);
        }
        return user;
    }

    @RequestMapping("/table")
    public ModelAndView table(){
        List<User> userList = userService.findUsers(null, null);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("user/table");
        mv.addObject("userList", userList);
        return mv;
    }

    @ResponseBody
    @RequestMapping("/list")
    public List<User> list(
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "note", required = false) String note) {
        return userService.findUsers(userName, note);
    }

    @RequestMapping("details")
    public ModelAndView details(Long id){
        MongoUser user = userService.getUser(id);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("user/details");
        mv.addObject("user", user);
        return mv;
    }
}
