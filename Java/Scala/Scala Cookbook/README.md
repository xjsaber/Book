Scala 实战

# 第1章 字符串 #

## 1.4 字符串中的变量代换 ##

### 问题 ###

将变量代换进一个字符串

### 解决方法 ###

#### 在字符串字面量中使用表达式 ####

${}内可嵌入任何表达式

## 1.6 字符串中的查找模式 ##

### 问题 ###

判断一个字符串是否符合一个正则表达式

### 解决方法 ###

String.r创建一个Regex对象，之后查找是否含有一个匹配时就可以用findFirstIn

