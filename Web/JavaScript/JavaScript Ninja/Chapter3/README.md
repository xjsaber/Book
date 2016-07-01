# 第3章 函数是根基 #
本章涵盖以下内容：
介绍本书的目的和结构

## 3.2 函数声明 ##
JavaScript函数是使用函数字面量（function literal）进行声明从而创建函数值的，就像使用数字字面量创建数字值一样。

函数字面量由四个部分组成。
1. function关键字。
2. 可选名称，如果指定名称，则必须是一个有效的JavaScript标识符。
3. 括号内部，一个以逗号分隔的参数列表。各个参数名称必须是有效的标识符、而且参数列表允许为空。即使是空参数列表，圆括号也必须始终存在。
4. 函数体，包含在大括号内的一系列JavaScript语句。函数体可以为空，但大括号必须始终存在。