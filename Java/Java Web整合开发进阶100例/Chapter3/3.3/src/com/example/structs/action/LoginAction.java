package com.example.structs.action;

import com.opensymphony.xwork2.ActionContext;

/**
 * Created by xjsaber on 3/10/2016.
 */
public class LoginAction {
    //Action类公用私有变量，用来做页面导航标志
    private static String FORWARD = null;
    private String username;
    private String password;
    public String getUsername(){    //取得用户名值
        return username;
    }
    public void setUsername(String username){   //设置用户名值
        this.username = username;
    }
    public String getPassword(){    //取得密码值
        return password;
    }
    public void setPassword(String password){ //设置密码值
        this.password = password;
    }

    public String execute() throws Exception{
        username = getUsername();   //属性值即JSP页面上输入值
        password = getPassword();   //属性值即JSP页面上输入值

        try{
            //判断输入值是否是空对象或没有输入
            if (username != null && !username.equals("") && password != null && !password.equals("")){
                ActionContext.getContext().getSession().put("user", getUsername());
                FORWARD = "success"; //根据标志内容导航到操作成功页面
            }else{
                FORWARD = "input"; //根据标志内容导航到操作失败页面
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return FORWARD;
    }
}
