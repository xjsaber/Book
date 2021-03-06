# The Little SAS Book中文版 #

所有SAS关键词全部用大写字母。

## 第1章 SAS软件使用入门 ##

### 1.1 SAS语言 ###

SAS语句：每一条SAS语句都以分号结尾

1. 以星号（\*）开头，分号（\;）结尾
2. 以斜杠星号（\/\*），星号斜杆（\*\/）结尾

### 1.2 SAS数据集 ###

变量和观测

数据类型：两种数据类型（数值型和字符型）

缺失数据

SAS数据集大小

SAS数据集与变量命名规则

SAS数据集中存储的说明信息

### 1.3 DATA步和PROC步 ###

SAS程序由两个基本部分构成：DATA步和PROC步。典型的程序由DATA步起始创建SAS数据集，而后将数据传递给PROC步进行处理。

	DATA distance;
		Miles = 26.22;
		Kilometers = 1.61 * Miles;
	PROC PRINT DATA = distance;
	RUN;

DATA步读取、修改数据；PROC步分析数据、执行实用功能以及打印报表。

DATA步以DATA语句开始，DATA关键字后面紧跟着SAS的数据集名称。而SAS过程则以PROC语句开头，该语句由关键字PROC及其后的过程名（比如PRINT、SORT或者MEANS）组成。

|DATA步|PROC步|
|--|--|
|以DATA语句开发|以PROC语句开始|
|读取、修改数据|完成特定分析或者特定功能|
|创建数据集|产生结果或报表|

### 1.4 DATA步的内置循环 ###

DATA步逐行执行语句、逐条处理观测。

### 1.5 选择提交SAS程序的模式 ###




### 1.10 SAS数据逻辑库 ###

SAS逻辑库只是一个存储SAS数据集（以及其他类型SAS文件）的位置。

* 当前逻辑库窗口：WORK逻辑库是SAS数据集的临时存储位置，它也是默认逻辑库。
* 创建新逻辑库：工具->新建逻辑库

### 1.12 用SAS资源管理器查看数据集属性 ###

### 1.13 使用SAS系统选项 ###

	PROC OPTIONS;
	RUN;

OPTIONS语句　OPTIONS语句是SAS程序的一部分，并影响其后的所有步。它以关键字OPTIONS开头，后面紧跟选项列表及其值。

OPTIONS语句是不属于PROC或DATA步的特殊SAS语句之一。这个全局语句可以出现在SAS程序中的任何地方，但通常最合理的方式是将其置于程序的第一行。

## 第2章 导入数据到SAS ##

### 2.1 导入数据到SAS的方法 ###

* 直接将数据输入SAS数据集
* 利用原始数据文件创建SAS数据集
* 将其他软件的数据文件转换成SAS数据集
* 直接读取其他软件的数据文件

### 2.2 使用VIEWTABLE窗口输入数据 ###

### 2.3 使用导入向导读取文件 ###

### 2.4 指定原始数据位置 ###

程序的内部数据。当数据量很小或者你准备

### 2.5 读取空格分隔的原始数据 ###

一般来说，变量名称不能超过32个字符，以字母或者下划线开头，且只包含字母、下划线和数字。如果变量值为字符（非数值）类型，在变量名称之后放置一个美元符号（$）。在变量名称之间至少留一个空格，记得在语句的结尾处放置一个分号。

	Data person;
		INPUT Name $ Age Height;
	PROC PRINT DATA = person;
	RUN;
该语句告知SAS读取三个数据值。Name之后的美元符号（$）声明该变量为字符型，而Age和Height变量都是数值型。

这个数据文件看起来不是很整洁，但是它确实满足了列表输入所有要求：字符数据长度不超过8个字符且没有内嵌空格，所有的数值被至少一个空格分隔，缺失数据用一个句点标识。注意，Noisy的数据溢出到了下一行。这并没有问题，因为如果INPUT语句中的变量个数比一行数据中的值的数量多，SAS将默认到下一行读取。

### 2.6 读取按列排列的原始数据 ###

一些数据文件在所有值或表示缺失值的句点之间没有空格（或者其他分隔符），因此无法使用列表输入来读取该文件。

列输入对比列表输入具有如下优势：

* 值之间无须空格
* 缺失值可以留空
* 字符型数据可以内嵌空格
* 可以跳过不需要的变量