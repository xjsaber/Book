Java从零到企业级电商项目实战-服务端

# 第1章 #

## 1-1 课程导学 ##

数据安全体系，应用安全体系，前端安全体系，开发者工具链

核心模块->演进细节->核心架构->设计思想->高性能高并发

## 1-2 项目功能与业务逻辑介绍 ##

## 1-3 课程安排与学习收获 ##

本地缓存和远程缓存（单机缓存和分布式缓存）

负载均衡调度服务器，负载均衡调度策略（轮训，权重，散列，目标ip地址散列，最好连接，加权最少连接，升级出来的策略）

### Session共享 ###

1. Session 1的服务器重启，Session会全部消失
2. Session Copy Application1会把Session复制到Application2上
3. Cookie with session，携带带着session信息的cookie带到服务器
	* cookie的长度有限制
	* cookie的安全性有问题
4. Session Server（所有用户的session信息全部保存到session服务器当中），可以把server也做成一个集群

### 数据库读写分离 ###

多数据源，如果封装对中间层没有代码侵入

### CDN，反向代理 ###

### 分布式文件 ###

1. 如何不影响线上的业务访问

### 专库专用（根据产品分类区分） ###

分布式事务，水平拆分，SQL路由（User1， User2）

抽取部分数据做搜索引擎，部分内容用NoSQL Server

# 第2章 开发环境的安装预配置讲解 #

## 2-1 ##

操作系统

1. Linux：centos6.8 64bit

源配置

1. 阿里云源配置官网：http://mirrors.aliyun.com/
2. 所用centos：http://mirrors.aliyun.com/help/centos
3. 源配置步骤
	* 备份 sudo mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.baclup
	* 下载 CentOS-Base.repo 到 /etc/yum.repos.d/ 下载方式：wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-6.repo
	* 运行 yum makecache生成缓存

学习建议

## 2-2 Linxu软件源配置实操 ##

## 2-3 Java环境 ##

1. 清理系统 查询默认自带：`jdk rpm -qa|grep jdk` 卸载命令：`sudo yum remove XXX`
2. 赋予权限：sudo chmod 777 jdk-7u80-linux-x64.rpm
3. 安装 sudo rpm -ivh jdk-7u80-linux-x64.rpm
4. 默认安装路径/usr/java
5. jdk配置环境变量

* sudo vim /etc/profile
* 在最下方增加 
	export JAVA_HOME=/usr/java/jdk1.7.0_80
	export CLASSPATH=.:$JAVA_HOME/jre/lib/rt.jar:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
* 在export PATH种添加$JAVA_HOME/bin
* 保存退出，通过vim的“:wq”命令进行保存退出
* 使配置生效 source /etc/profile

### Java 总结 ###

## 2-4 jdk安装 ##

## 2-6 tomcat ##

1. 下载：登录http://learning.happpymall.com/env.html
2. 解压缩：tar -zxvf apache-tomcat-7.0.73.tar.gz
3. 配置环境变量
	* sudo vim /etc/profile
	* 在最下方增加 export CATALINA_HOME=/developer/apache-tomcat-7.0.73
	* :wq保存退出
	* 配置生效 source /etc/profile
4. 配置UTF-8字符集
	* 进入tomcat安装的conf文件夹，编辑server.xml
	* 找到配置8080默认端口的位置，再xml节点末尾增加URIEncoding="UTF-8"

### tomcat总结 ###

下载、安装、配置环境变量、配置UTF-8字符集

## 2-7 tomcat安装实操 ##

## 2-8 Maven简介 ##

1. 确保安装了JDK
2. 下载
3. 通过tar或unzip进行解压缩 `tar -zxvf apache-maven-3.0.5-bin.tar.gz`
4. 配置环境变量 
	1. sudo vim /etc/profile 再最下面增加Maven的环境变量
	2. export MAVEN_HOME=/developer/apache-maven-3.0.5
	3. export PATH=$PATH:$JAVA_HOME/bin:$MAVEN_HOME/bin
	4. source /etc/profile 生效

### Maven项目配置 ###

	settings.xml

### Maven常用命令 ###

* 清除命令：mvn clean
* 编译命令：mvn compile
* 打包命令：mvn package
* 跳过单元测试：mvn clean package -Dmaven.test.skip=true

### Maven总结 ###

## 2-9 maven实操 ##

## 2-12 vsftpd ##

### vsftpd简介 ###

very secure FTP daemon

### vsftpd安装 ###

Linux：安装、创建虚拟用户、配置、防火墙设置

1. 安装
	* yum -y install vsftpd
		1. 是否使用sudo权限执行根据具体环境
		2. rpm -qa|grep vsftpd通过检查是否已经安装vsftpd
		3. 默认配置文件在/etc/vsftpd/vsftpd.conf
		
2. 创建虚拟用户
	1. 选择在根或者用户目录下创建ftp文件夹：mkdir ftpfile，如：/ftpfile
	2. 添加匿名用户：useradd ftpuser -d /ftpfile -s /sbin/nologin
	3. 修改ftpfile权限：chown -R ftpuser.ftpuser /ftpfile
	4. 重设ftpuser密码：passwd ftpuser
3. 配置
	1. cd /etc/vsftpd
	2. sudo vim chroot_list
	3. 把刚才新增的虚拟用户添加到此配置文件中 sudo cat chroot_list ftpuser
	4. :wq保存退出
	5. sudo vim /etc/selinux/config，修改为SELINUX=disabled
	6. :wq保存退出
		* 可能碰到550拒绝访问的情况 sudo setsebool -P ftp_home_dir 1
	7. sudo vim /etc/vsftped/vsftpd.conf
	8. 添加和更新配置
4. 防火墙配置
	1. sudo vim /etc/sysconfig/iptables
	2. -A INPUT -p TCP --dport 61001:62000 -j ACCEPT A OUTPUT -p TCP --sport 61001:62000 -j ACCEPT -A INPUT -p TCP --....添加到防火墙
	3. :wq保存退出
	4. sudo service iptables restart 执行命令重启
	
### vsftpd验证 ###

1. 执行 sudo service vsftpd restart
2. 执行ifconfig查看运行vsftpd服务器的ip地址
3. 打开浏览器访问：ftp://
4. 输入密码

### 常用命令 ###

* 启动：sudo service vsftpd start
* 关闭：sudo service vsftpd stop
* 重启：sudo service vsftpd restart

## ftp服务器 实操 ##

## Nginx ##

### Nginx概览 ###

* 简介
* 安装
* 常用命令
* 项目配置及验证

### 简介 ###

### 安装 ###

1. 安装gcc（yum install gcc）
2. 安装pcre （yum install pcre-devel）
3. 安装zlib （yum install zlib zlib-devel）
4. 安装openssl （yum install openssl openssl-devel）支持ssl
5. 下载源码包
	1. wget
	2. tar -zxvf nginx.tar.gz
6. Nginx安装
	1. 进入nginx目录之后执行./configure
		* 指定安装目录，增加参数--prefix=/usr/nginx
		* 如果不指定路径，可以通过`whereis nginx`进行查询
		* 默认安装在/usr/local/nginx
	2. make
	3. make intall


### 常用命令 ###

* 测试配置命令 /nginx/sbin/nginx-t
* 开始命令 /nginx/sbin/nginx
* 停止命令 /nginx/sbin/nginx -s stop 或者是 nginx -s quit
* 重启命令 /nginx/sbin/nginx -s reload
* 查看进程命令 ps -ef|grep nginx
* 平滑重启 kill -HUP [Nginx主进程好（即查看进程命令查到的PID）]
* 增加防火墙访问权限
	1. sudo vim /etc/sysconfig/iptables
	2. -A INPUT -p tcp -m state --state NEW -m tcp --dport 80 -j ACCEPT
	3. 保存退出
	4. 重启防火墙 sudo service iptables restart

### 虚拟域名配置及测试验证 ###

* 配置步骤
	1. 编辑 sudo vim /usr/local/nginx/conf/nginx.conf
		* 增加 inlcude vhost/*.conf
		* 保存退出
	2. 在/usr/local/nginx/conf 目录新建vhost文件夹 即：/usr/local/nginx/conf/vhost
	3. 创建域名转发配置文件
	4. 启动（重启）验证
		* 启动：${nginx}/sbin/nginx
		* 重启：${nginx}/sbin/nginx -s reload
	5. 访问验证
* 指向端口
* 指向目录
* 测试验证

### Nginx本地玩耍注意事项 ###

* 配置域名转发，配置host，并且使host生效，设置完成重启浏览器

1. Linux:
	* sudo vim /etc/hosts
	* 添加好对应的域名及ip
	* :wq保存退出

### Nginx总结 ###

安装和常用命令

反向代理及测试验证

[http://learning.happymmall.com/env.html](http://learning.happymmall.com/env.html)

## 2-16 nginx实操 ##

## 2-17 nginx反向代理 ##

	inlucde vhost/*conf

	server { 
		listen 80; 
		autoindex on; 
		server_name admin.happymmall.com; 
		access_log /usr/local/nginx/logs/access.log combined; 
		index index.html index.htm index.jsp index.php; #root /devsoft/apache-tomcat-7.0.73/webapps/mmall;
		#error_page 404 /404.html; 
		if ( $query_string ~* ".*[\;'\<\>].*" ){ 
			return 404; 
		} 
		location = / { root /product/front/mmall_admin_fe/dist/view; index index.html; } 
		location ~ .*\.(html|htm)$ { 
			root /product/front/mmall_admin_fe/dist/view; index index.html; } 
			location / { proxy_pass http://127.0.0.1:8080/; add_header Access-Control-Allow-Origin '*'; } 
		} 

## 2-22 Mysql ##

### MySQL配置 ###

用户配置、权限配置、新建database

### MySQL安装 ###

1. 安装
	* 执行 yum -y install mysql-server
	* rpm -qa|grep mysql-server
	* 默认配置文件在/etc/my.cnf
2. 字符集配置
	* vim /etc/my.cnf
	* 添加配置，在mysqld节点下添加：
		default-charcter-set=utf-8
		character-set-server=utf-8
	* :wq保存退出

关于中文乱码问题

1. 在5.1版本时，为了解决中文乱码问题，my.ini内[mysql]和[mysqld]中都写：

	default-character-set=utf8

2. 在5.5版本时，[mysql]内可以这么写，[mysqld]内不能这么写：

	character-set-server=utf8

### MySQL验证 ###



### MySQL常用命令 ###

# 第3章 数据表结构设计 #

## 3-1 数据表结构设计 ##

* 表结构
* 表关系
* 唯一索引
* 单索引及组合
* 后悔药-时间戳

### 表结构 ###

## 3-2 表关系 ##

外键，触发器都不建议使用

## 3-3 数据表索引与时间戳讲解 ##

唯一索引unique，保证数据唯一性

单索引和组合索引

时间戳 查业务问题的后悔药

create_time：数据创建时间
update_time：数据更新时间 

### 总结 ###

表结构

表关系

唯一索引

单索引及组合索引

后悔药时间戳

# 第4章 #

## 4-1 项目初始化概要 ##

## 4-2 数据库初始化 ##

## 4-3 idea项目开发工具安装实操（mac） ##

## 4-4 idea项目开发工具安装实操（windows） ##

## 4-5 jdk、tomcat、maven配置及初始化 ##

## 4-6 git初始化 ##

## 4-7 maven的pom文件配置 ##

## 4-8 项目包结构初始化 ##

## 4-9 mybatis三剑客之mybatis-generator配置 ##



## 4-10 mybatis三剑客之mybatis-generator生成数据对象和时间戳优化 ##

## 4-11 mybatis三剑客之mybatis-plugin讲解 ##

安装mybatis-plugin

## 4-12 mybatis三剑客之mybatis-pagehelper分页插件讲解 ##

github上的mybatis-pagehelper开源分页控件

## 4-13 spring官方demo指引及配置 ##

## 4-14 spring、springmvc配置实操 ##

## 4-15 logback配置讲解 ##

## 4-16 ftp服务器配置讲解 ##

## 4-17 idea的注入和自动编译配置 ##

## 4-18 项目初始化代码提交 ##

## 4-19 两个提高工作效率的神器-Restlet Client和fe助手 ##

Restlet Client和fe助手

# 第5章 #

## 5-1 用户模块 ##

* 功能介绍
* 学习目标
* 数据表设计
* 接口设计

### 功能介绍 ###

* 登录 
* 用户验证 
* 注册 
* 忘记密码 
* 提交问题答案 
* 重置密码
* 获取用户信息
* 更新用户信息
* 退出登录

### 学习目标 ###

* 横向越权、纵向越权安全漏洞
* MD5明文加密及增加salt值
* Guava缓存的使用
* 高服用服务响应对象的设计思想及抽象封装
* Mybatis-plugin的使用技巧
* Session的使用
* 方法局部演进
* 横向越权、纵向越权安全漏洞
	* 横向越权：攻击者尝试访问与他拥有相同权限的用户的资源
	* 纵向越权：低级别攻击者尝试访问高级别用户的资源

### 接口设计 ###

#### 前台用户接口设计 ####

* /user/login.do(POST) 登录接口 
	* request
		1. username
		2. password
	* response
		1. fail	
			{
			    "status": 1,
			    "msg": "密码错误"
			}
		2. success 
			{
			    "status": 0,
			    "data": {
			        "id": 12,
			        "username": "aaa",
			        "email": "aaa@163.com",
			        "phone": null,
			        "role": 0,
			        "createTime": 1479048325000,
			        "updateTime": 1479048325000
			    }
			}
* /user/register.do(POST) 注册接口
	* request
		1. username
		2. password
		3. email
		4. phone
		5. question
		6. answer
	* response
		1. success
		{
		    "status": 0,
		    "msg": "校验成功"
		}
		2. fail
		{
		    "status": 1,
		    "msg": "用户已存在"
		}
* /user/check_valid.do(GET) 检查用户名是否有效
	* request
		1. str 是用户名也可以是email
		2. type username和email
	* response
		1. success
		{
		    "status": 0,
		    "msg": "校验成功"
		}
		2. fail
		{
		    "status": 1,
		    "msg": "用户已存在"
		}
* /user/get_user_info.do(GET) 获取登录用户信息
	* request

	* response
		1. success
		{
		    "status": 0,
		    "data": {
		        "id": 12,
		        "username": "aaa",
		        "email": "aaa@163.com",
		        "phone": null,
		        "role": 0,
		        "createTime": 1479048325000,
		        "updateTime": 1479048325000
		    }
		}
		2. fail
		{
		    "status": 1,
		    "msg": "用户未登录,无法获取当前用户信息"
		}
* /user/forget_get_question.do(POST) 忘记密码
	* request
		1. username
	* response
		1. success
		{
		    "status": 0,
		    "data": "这里是问题"
		}
		2. fail
		{
		    "status": 1,
		    "msg": "该用户未设置找回密码问题"
		}
* /user/forget_check_answer.do(GET) 提交问题答案
	* request	
		1. username
		2. question
		3. answer
	* reponse
		1. success
		{
		    "status": 0,
		    "data": "531ef4b4-9663-4e6d-9a20-fb56367446a5"
		}
		2. fail
		{
		    "status": 1,
		    "msg": "问题答案错误"
		}
* /user/forget_reset_password.do（GET）忘记密码的重设密码
	* request
		1. username
		2. passwordNew
		3. forgetToken （不设置token则任何username和password都可以直接更新密码，常规30min）
	* response
		1. success
		{
		    "status": 0,
		    "msg": "修改密码成功"
		}
		2. fail
		{
		    "status": 1,
		    "msg": "修改密码操作失效"
		}
* /user/reset_password.do(POST) 登录中状态重置密码
	* request
		1. passwordOld
		2. passwordNew
	* response 
		1. success
		{
		    "status": 0,
		    "msg": "修改密码成功"
		}
		2. fail
		{
		    "status": 1,
		    "msg": "旧密码输入错误"
		}
* /user/update_information.do(POST) 登录状态更新个人信息
	* request
		1. email
		2. phone
		3. question
		4. answer
	* response  
		1. success
		{
		    "status": 0,
		    "msg": "更新个人信息成功"
		}
		2. fail
		{
		    "status": 1,
		    "msg": "用户未登录"
		}
* /user/get_information.do(GET) 获取当前登录用户的详细信息，并强制登录
	* request
	* response
		1. success
		2. fail 
		{
		    "status": 10,
		    "msg": "用户未登录,无法获取当前用户信息,status=10,强制登录"
		}
* /user/logout.do(GET) 退出登录

#### 后台用户接口设计 ####

* /manager/user/login.do 后台管理员登录
	* request
		1. username
		2. password
	* response
		1. success
		{
		    "status": 0,
		    "data": {
		        "id": 12,
		        "username": "aaa",
		        "email": "aaa@163.com",
		        "phone": null,
		        "role": 0,
		        "createTime": 1479048325000,
		        "updateTime": 1479048325000
		    }
		}
		2. fail
		{
		    "status": 1,
		    "msg": "密码错误"
		}
* /manager/user/list.do 用户列表
	* request
		1. pageSize(default=10)
		2. pageNum(default=1)
	* response
		1. success
		2. fail
		{
		  "status": 1,
		  "msg": "没有权限"
		}

## 5-3 登出，注册，校验功能开发 ##

## 5-4 获取用户登录信息 ##

## 5-5 忘记密码的重置密码功能的开发 ##

## 5-6 登录状态下的重置密码功能的开发 ##

## 5-7 更新个人用户信息 ##

## 5-8 获取用户信息功能开发 ##

存入Session的信息

## 5-9 用户模块所有功能自测试 ##
 
restlet_client

# 第6章 分类管理模块开发 #

## 6-1 分类管理模块开发概要与接口设计讲解 ##

* 功能介绍
* 学习目标
* 数据表设计
* 接口设计

### 功能介绍 ###

获取节点->增加节点->修改名字->获取分类ID->递归子节点ID->...

### 学习目标 ###

* 如何设计及封装无限层级的树状数据结构
* 递归算法的设计思想
* 如何处理复杂对象排重
* 重写hashcode和equal的注意事项

### 接口设计 ###

## 6-2 添加分类和更新分类名字功能开发 ##



## 6-3 查询节点和递归查找功能开发 ##

## 6-4 分类管理模块所有功能 ##

# 第7章 商品模块 #

## 7-1 商品管理模块开发概要与接口设计讲解 ##

* 功能介绍
* 学习目标
* 数据表设计
* 接口设计

### 功能介绍 ###

* 前台功能

产品搜索 动态排序列表 商品详情

* 后台功能

商品列表 商品搜索 图片上传 富文本上传 商品详情 商品上下架 增加商品 更新商品

### 学习目标 ###

* FTP服务对接
* SpringMVC文件上传
* 流读取Properties配置文件
* 抽象POJO、BO、VO对象之间的转换关系及解决思路

* 静态块
* Mybatis-PageHelper高效准确地分页及动态排序
* Mybatis对List遍历的实现方法
* Mybatis对where语句动态拼装的几个版本演变

* POJO、BO、VO
* POJO、VO

### 接口设计 ###

* 前台商品接口
* 后台品类接口

## 7-2 后台商品新增，保存，更新，上下架功能开发 ##

## 7-3 后台获取商品 ##


# 第8章 购物车 #

## 8-1 购物车模块开发概要 ##

购物车模块

* 功能介绍
* 学习目标
* 数据表设计
* 接口设计

### 功能介绍 ###

加入商品、更新商品数、查询商品数、移除商品、单选/取消、全选/取消、购物车列表

### 学习目标 ###

* 购物车模块的设计思想
* 如何封装一个高复用购物车核心方法
* 解决浮点型商业运算中丢失精度的问题

### 接口 ###

* list

limitQuantity：限制数量成功，限制数量失败。
失败则给与提示，表示已满。

* add
* update
* delete_product
* select
* un_select
* get_cart_product_count
* select_all
* un_select_all

# 第9章 收货地址模块 #

* 功能介绍
* 学习目标
* 数据库设计
* 接口设计

## 功能介绍 ##

添加地址、删除地址、更新地址、地址列表、地址分页

## 学习目标 ##

* SpringMVC数据绑定中对象绑定
* mybatis自动生成主键、配置和使用
* 如何避免横向越权漏洞的巩固

## 数据表设计 ##

## 接口设计 ##

添加地址












