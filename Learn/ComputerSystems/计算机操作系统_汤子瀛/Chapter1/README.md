# 第一章 操作系统引论 #

## 1.1 操作系统的目标和作用 ##

### 1.1.1 操作系统的目标 ###

#### 1.有效性 ####

（1）提高系统资源利用率
（2）提高系统的吞吐量

#### 2.方便性 ####

#### 3.可扩充性 ####

#### 4.开放性 ####

### 1.1.2 操作系统 ###

#### 1.OS作为用户与计算机硬件系统之间的接口 ####

OS作为用户与计算机硬件系统之间的接口的含义是：OS处于用户与计算机硬件系统之间，用户通过OS来使用计算机系统。
（1）命令方式
（2）系统调用方式
（3）图形、窗口方式

#### 2.OS作为计算机系统资源的管理者 ####

#### 3.OS实现了对计算机资源的抽象 ####

### 1.1.3 推动操作系统发展的主要动力 ###

## 1.2 操作系统的发展 ##

### 1.2.1 无操作系统的计算机系统 ###

#### 1.人工操作方式 ####


#### 2.脱机输入/输出方式 ####
为了解决人机矛盾及CPU和I/O设备之间速度不匹配的矛盾。
（1）减少了CPU的空闲时间。
（2）提高了I/O速度

### 1.2.2 单道批处理系统的处理过程 ###
#### 1.单道批处理系统的处理方式 ####

#### 2.单道批处理系统的特征 ####
（1）自动性
（2）顺序性
（3）单道性
### 1.2.3 多道批处理系统 ###
#### 1.多道程序设计的基本概念 ####
（1）提高CPU的利用率

（2）可提高内存的I/O设备利用率

（3）增加系统吞吐量

#### 2.多道批处理系统的优缺点 ####
（1）资源利用率高

（2）系统吞吐量大
（3）平均周转时间长
（4）无交互能力

#### 3.多道批处理系统需要解决的问题 ####
（1）处理机管理问题
（2）内存管理问题
（3）I/O设备管理问题
（4）文件管理问题
（5）作为管理问题
### 1.2.4 分时系统 ###
1.分时系统的产生
查询系统
（1）人-机交互
（2）共享主机
（3）便于用户上机
2.分时系统实现中的关机那问题
（1）及时接收
（2）及时处理
#### 3.分时系统的特征 ####
（1）多路性
（2）独立系
（3）及时性
（4）交互性
### 1.2.5 实时系统 ###
实时系统（Real Time System）是指系统能及时（或即时）响应外部事件的请求，在规定的时间内完成对该事件的处理，并控制所有实时任务协调一致地运行。
#### 1.应用需求 ####
（1）实时控制

（2）实时信息处理

#### 2.实时任务 ####
1）按任务执行时是否呈现周期性来划分
（1）周期性
（2）非周期性
2）根据对截止时间的要求来划分
（1）硬实时任务（）
（2）软实时任务（）
#### 3.实时系统与分时系统特征的比较 ####
（1）多路性。
（2）独立性。
（3）及时性。
（4）交互性。
（5）可靠性。
### 1.2.6 微机操作系统的发展 ###
1.单用户单任务操作系统
2.多用户多任务操作系统
## 1.3 操作系统的基本特征 ##
### 1.3.1 并发性 ###
#### 1.并行与并发 ####
并行性是指两个或多个事件在同一时刻发生；而并发性是指两个或多个事件在同一时间间隔内发生。
#### 2.引入进程 ####

#### 3.引入线程 ####

### 1.3.2 共享性 ###
共享（Sharing），是指系统中的资源可供内存中多个并发执行的进程（线程）共同使用。**并发**和**共享**是操作系统的两个最基本特征，它们又是相互存在的条件。
#### 1.互斥共享方式 ####
仅当A进程访问完并释放该资源后，才允许另一进程对该资源进行访问。互斥式共享，把一段时间内只允许一个进程访问的资源称为临界资源或独占资源。
#### 2.同时访问方式 ####
允许在一段时间内由多个进程“同事”对它们进行访问。
### 1.3.3 虚拟技术 ###
“虚拟”(Virtual)，是指通过某种技术把一个物理实体变为若干个逻辑上的对应物。
#### 1.时分复用技术 ####

1）虚拟处理机技术

2）虚拟设备技术

#### 2.空分复用技术 ####

1）虚拟磁盘技术

2）虚拟存储器技术
空分复用则是利用存储器的空闲空间来存放其他的程序，以提高内存的利用率。

### 1.3.4 异步性 ###

## 1.4 操作系统的主要功能 ##

### 1.4.1 处理机管理功能 ###
处理机管理的主要功能是创建和撤销进程（线程），对诸进程（线程进行协调），实现进程（线程）之间的信息交互。
#### 1.进程控制 ####

#### 2.进程同步 ####

（1）进程互斥方式。
（2）进程同步方式。

#### 3.进程通信 ####

#### 4.调度 ####

### 1.4.2 存储器管理功能 ###

1.内存分配

2.内存保护

3.地址映射

4.内存扩充

### 1.4.3 设备管理功能 ###
1.缓冲管理

2.设备分配

3.设备处理

### 1.4.4 文件管理功能 ###
文件管理的主要任务是对用户文件和系统文件进行管理。
1.文件存储空间的管理
2.目录管理
3.文件的读/写管理和保护

## 1.5 OS结构设计 ##

1.5.1 传统的操作系统结构


