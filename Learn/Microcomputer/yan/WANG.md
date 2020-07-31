# wang #

## 第1章 计算机系统概述 ##

### 1.1 计算机发展历程 ###

#### 1.1.1 计算机硬件的发展 ####

### 1.4 本章小结 ###



## 第3章 存储系统 ##

在虚拟存储器中，页面是设置得大一些好还是设置小一些好？

### 3.1 存储器概述 ###

#### 3.1.1 存储器的分类 ####

**1. 按在计算机中的作用（层次）分类**

**2. 按存储介质分类**


**3. 按存取方式分类**

1. 随机存储器（RAM）。
2. 只读存储器（ROM）。
3. 串行访问存储器。

**4. 按信息的可保存性分类**

#### 3.1.2 存储器的性能指标 ####

存储器有3个主要性能指标，即存储容量、单位成本和存储速度。

1. 存储容量
2. 单位成本
3. 存储速度：数据传输率 = 数据的宽度/存储周期
	1. 存取时间
	2. 存取周期
	3. 主存宽带

### 3.2 存储器的层次化结构 ###

#### 3.2.1 多级存储系统 ####

多级存储器结构 => 解决存储系统大容量、高速度和低成本3个相互制约的矛盾

实际上，存储系统层次结构主要体现在“Cache-主存”层次和“主存-辅存”层次：
	* 前者主要解决CPU和主存速度不匹配的问题
	* 后者解决存储系统的容量问题

### 3.3 半导体随机存储器 ###

#### 3.3.1 SRAM和DRAM ####

**1. SRAM的工作原理**

通常把存放一个二进制位的物理器件称为存储元，存储器的最基本的构件。

**2. DRAM的工作原理**

DRAM电容上的电荷一般只维持1~2ms，因此即使电源不断电，信息也会自动消失。为此，每隔一定时间必须刷新，通常取2ms，这个时间称为刷新周期。常用的刷新周期方式有3中：其中刷新、分散刷新和异步刷新。

1. 集中刷新：指在一个刷新周期内，利用一段固定的时间，依次对存储器的所有行进行逐一再生，在此期间停止对存储器的读写操作，称为“死时间”，又称访存“死区”。
2. 分散刷新：把对每行的刷新分散到各个工作周期中。
3. 异步刷新：异步刷新是前两种方法的结合

**3. 存储器的读、写周期**

1. RAM的读周期
2. RAM的写周期

### 3.4 主存储器与CPU的连接 ###

#### 3.4.1 连接原理 ####

1. 主存储器通过数据总线、地址总线和控制总线与CPU连接。
2. 数据总线的位数与工作频率的乘积正比于数据传输率。
3. 地址总线的位数决定了可寻址的最大内存空间。
4. 控制总线（读/写）指出总线周期的类型和本次输入/输出操作完成的时刻。

#### 3.4.2 主存容量的扩展 ####

通常采用位扩展法、字扩展法和字节同时扩展法来扩展主存容量。

**1. 位扩展法**

**2. 字扩展法**

**3. 字位同时扩展法**

#### 3.4.3 存储芯片的地址分配和片选 ####

**1. 线选法**

线选法用除片内寻址的高位地址线直接（或经反相器）分别接至存储芯片的片选端，当某地址线信息为“0”时，就选中与之对应的存储芯片。

**2. 译码片选法**

译码片选法用除片内寻址外的高位地址线通过地址译码器芯片产生片选信号。如用8片8K*8位的存储芯片组成64K*8位存储器（地址线为16位，数据线为8位），需要8个片选信号；

#### 3.4.4 存储器与CPU的连接 ####

**1. 合理选择存储芯片**

**2. 地址线的连接**

存储芯片的容量不同，其地址线数也不同，而CPU的地址线数往往比存储芯片的地址线数要多。

**3. 数据线的连接**

CPU的数据线数与存储芯片的数据线数不一定相等，在相等时可直接相连；在不等时必须对存储芯片扩位，使其数据位数与CPU的数据线数相等。

**4. 读/写命令线的连接**

**5. 片选线的连接**

片选线的连接是CPU与存储芯片连接的关键。

### 3.6 高速缓冲存储器 ###

#### 3.6.1 程序访问的局部性原理 ####

程序访问的局部性原理包括时间局部性和空间局部性。

高速缓冲技术就是利用程序访问的局部性原理，把程序中正在使用的部分存放在一个高速的容量较小的Cache中，使CPU的访存操作大多数针对Cache进行，从而大大提高程序的执行速度。

局部性原理

#### 3.6.2 Cache的基本工作原理 ####

Cache位于存储器层次结构的顶层，通常由SRAM构成。

Cache和主存都被划为相等的块（便于Cache和主存之间交互信息），Cache块又称Cache行，每块由若干字节组成，块的长度称为块长（Cache行长）。

#### 3.6.3 Cache和主存的映射方式 ####

Cache行中的信息是主存中某个块的副本，*地址映射*是指把主存地址空间映射到Cache地址空间，即把存放在主存中的信息按照某种规则装入Cache。

**1. 直接映射**

主存中的每一块只能装入Cache中的唯一位置。

**2. 全相联映射**

主存中的每一块可以装入Cache中的任何位置

**3. 组相联映射**

将Cache空间分成大小相同的组，主存的一个数据块可以转入一组内的任何一个位置，即组间采取直接映射，而组内采取全相联映射。

#### 3.6.5 Cache写策略 ####

Cache中的内容是主存块副本，当对Cache中的内容进行更新时，就需选用写操作策略使Cache内容和主存内容保持一致

1. 全写法（write-through）
2. 写回法（write-back）

### 3.7 虚拟存储器 ###

#### 3.7.1 虚拟存储器的基本概念 ####

#### 3.7.2 页式虚拟存储器 ####

#### 3.7.3 加快地址转换：快表（TLB） ####

#### 3.7.4 段式虚拟存储器 ####

#### 3.7.5 段页式虚拟存储器 ####

#### 3.7.6 虚拟存储器与Cache的比较 ####

虚拟存储器与Cache：

**1. 相同之处**

1. 最终目标是为了提高系统性能，两者都有容量、速度、价格的梯度。
2. 都把数据划分为小信息块，并作为基本的传递单位，虚存系统的信息快更大。
3. 都有地址的映射、替换算法、更新策略等问题。
4. 依据程序的局部性原理应用“快速缓存的思想”，将活跃的数据放在相对高速的部件中。

**2. 不同之处**

1. Cache主要解决系统速度，而虚拟存储器却是为了解决主存容量。


### 3.8 本章小结 ###

1. 存储器的层次结构
2. 存取周期和存取时间
3. 在虚拟存取中，页面是设置得大一些好还是设置得小一些好

### 3.9 常见问题和易混淆知识点 ###

## 第4章 指令系统 ##

### 考试内容

1. 指令格式
   1. 指令的基本操作
   2. 定长操作码指令格式
   3. 扩展操作码指令格式
2. 指令的寻址方式
   1. 有效地址的概念
   2. 数据寻址和指令寻址
   3. 常见寻址方式
3. CISC和RISC的基本概念

### 4.1 指令格式

指令（又称机器指令）是指示计算机执行某种操作的命令，是计算机运行的最小功能单位。一台计算机的所有指令的集合构成该机的指令系统，也称指令集。指令系统是计算机的主要属性，位于硬件和软件的交界面上。

#### 4.1.1 指令的基本格式

一条指令就是机器语言的一个语句。一条指令通常包括操作码字段和地址码字段两部分：

* 操作码：指出指令中该指令应该执行什么性质的操作和具有何种功能。操作码是识别指令、了解指令功能及区分操作数地址内容的组成和使用方法等的关键信息。
* 地址码：给出被操作指令的信息（指令或数据）的地址，包括参加运算的一个或多个操作数所在的地址、运算结果的保存地址、程序的转移地址、被调用的子程序的入口地址等。

1. **零地址指令**

   只给出操作码OP，没有显式地址

   * 不需要操作数的指令。
   * 零地址的运算符指令仅用在堆栈计算机中。

2. 一地址指令

   * 只有目的操作数的单操作数指令，按A~1~地址读取操作数，进行OP操作后，结果存回原地址。指令含义：OP(A~1~)->A~1~
   * 隐含约定目的地址的双操作数指令，按指令地址A~1~可读取源操作数，指令可隐含约定另一个操作数由ACC（累加器）提供，运算结果也将存放在ACC中。
     指令含义：（ACC）OP(A~1~)->ACC

3. 二地址指令
   指令含义：（A~1~）OP(A~2~)->A~1~

4. 三地址指令

   指令含义：（A~1~）OP(A~2~)->A~3~

5. 四地址指令

   指令含义：（A~1~）OP(A~2~)->A~3~，A~4~=下一条将要执行指令的地址

#### 4.1.2 定长操作码指令格式

定长操作码指令在指令字的最高位部分分配固定的若干位（定长）表示操作码。一般n位操作码字段的指令系统最大能够表示2^n^条指令。

#### 4.1.3 扩展操作码指令格式

为了在指令字长有限的前提下扔保持比较丰富的指令种类，可采取可变长度操作码，即全部指令的操作码字段的位数不固定，且分散地放在指令字的不同位置上。

通常情况下，对使用频率较高的指令分配较短的操作码，对使用频率较低的指令分配较长的操作码，从而尽可能减少指令译码和分析的时间。

### 4.2 指令的寻址方式

寻址方式是指寻找指令或操作数有效地址的方式，即确定本条指令的数据地址及下一条待执行指令的地址的方法。寻址方式分为指令寻址和数据寻址两大类。

#### 4.2.1 指令寻址和数据寻址

1. **指令寻址**
   * 顺序寻址通过程序计数器（PC）加1（1个指令字长），自动形成下一条指令的地址。
   * 跳跃寻址通过转移类指令实现。所谓跳跃，是指下条指令的地址码不由程序计数器给出，而由本条指令给出下条指令地址的计算方式。跳跃地址分为绝对地址和相对地址。

2. **数据寻址**

   数据寻址是指如何在指令中表示一个操作数的地址，如何用这种表示得到操作数或怎样计算给出操作数的地址。

#### 4.2.2 常见的数据寻址方式

1. **隐含寻址**
不明显给出操作数的地址，而在指令中隐含操作数的地址。隐含寻址的优点是有利于缩短指令字长；缺点是需增加存储操作数或隐含地址的硬件。
2. **立即（数）寻址**
3. 直接寻址
4. 间接寻址
5. 寄存器寻址
6. 寄存器间接寻址
7. 相对寻址
8. 基址寻址