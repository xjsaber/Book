# 第10章 数组和指针 #
## 10.1 数组 ##

### 10.1.1 初始化数组 ###
以逗号分隔的值列表（用花括号括起来）来初始化数组。

const声明数组，设置只读数组，程序只能从数组中检索值，不能把新值写入数组。

### 10.1.2 指定初始化器（C99） ###
	int arr[6] = {0, 0, 0, 0, 0, 212}; //传统语法
	int arr[6] = {[5] = 212}; //把arr[5]初始化位212，指明初始化元素

1. 如果指定初始化器后面有更多的值：[4]=31,30,31，那么后面这些值将被用于初始化指定元素后面的元素（days[4]=31,days[5]=30.days[6]=31）
2. 如果再次初始化指定的有元素，那么最后的初始化将会取代之前的初始化。

### 10.1.3 给数组元素赋值 ###
C不允许把数组作为一个单元赋给另一个数组，除初始化以外爷不允许使用花括号列表的形式赋值。

	int oxen[SIZE] = {5, 3, 2, 8}; //没问题
	int yaks[SIZE];
	yaks = oxen; //不允许
	yaks[SIZE] = {5, 3, 2, 8} //不起作用
### 10.1.4 数组边界 ###
声明数组时使用符号常量来表示数组的大小。

### 10.1.5 指定数组的大小 ###
C99 变长数组

## 10.2 多维数组 ##

### 10.2.1 初始化二维数组 ###

### 10.2.2 其他多维数组 ###

## 10.3 指针和数组 ##

## 10.6 保护数组中的数据 ##
### 10.6.2 const的其他内容 ###


## 10.7 指针和多维数组 ##
