# 第5章 初始化与清理 #

初始化和清理（cleanup）正是涉及安全的两个问题。

C++引入了构造器（construct）的概念，这是一个在创建对象时被自动调用的特殊方法。Java中也采用了构造器，并额外提供了“垃圾回收器”。

## 5.1 用构造器确保初始化 ##


    initialize()
`new Rock();`将会为对象分配存储空间，并调用相应的构造器。确保在能操作对象之前，已经被恰当地初始化了。

构造器的名称必须与类名完全相同，所以“每个方法首字母小写”的编码风格不适用于构造器。

构造器是一种特殊类型的方法，因为它没有返回值。这与返回值为空（void）明显不同。对于空返回值，尽管方法本身不会自动返回什么，但仍可选择让它返回别的东西。

构造器则不会返回任何东西，别无选择。（new 表达式确实返回了对新建对象的引用，但构造器本身并没有任何返回值）。假如构造器具有返回值，并且允许人们自行选择返回类型，那么势必让编译器知道如何处理此返回值。

## 5.2 方法重载 ##

### 5.2.1 区分重载方法 ###

参数顺序的不同也足以区分两个方法，但一般会使代码难以维护。每个重载的方法都必须有一个独一无二的参数类型列表。

### 5.2.2 涉及基本类型的重载 ###

基本类型能从一个“较小”的类型自动提升至一个“较大”的类型。

### 5.2.3 以返回值区分重载方法 ###

根据方法的返回值来区分重载方法是行不通的。

## 5.3 默认构造器 ##

默认构造器（又名“无参”构造器）是没有形式参数的——它的作用是创建一个“默认对象”。如果你写的类中没有构造器，则编译器会自动帮你创建一个默认构造器。

如果已经定义了一个构造器（无论是否有参数），编译器就不会帮你自动创建默认构造器。

## 5.4 this关键字 ##

	Banana a = new Banana(),
		   b = new Banana();
	a.peel(1);
	b.peel(2);

等于

	Banana.peel(a, 1);
	Banana.peel(b, 2);

假设希望在方法的内部获得对当前对象的引用。由于这个引用是由编译器“偷偷”传入的，所以没有标识符可用。但是，为此有个专门的关键字：this。this关键字只能在方法内部使用，表示对“调用方法的那个对象”的引用。

//TODO 8

### 5.4.1 在构造器中调用构造器 ###

this调用一个构造器，但却不能调用两个，必须将构造器调用置于最起始处，否则编译器会报错。

this的另一个用法，由于参数s的名称和数据成员s的名字相同，所以会产生歧义，使用this.s来代表数据成员解决问题。

//TODO 9

### 5.4.2 static的含义 ###

static方法就是没有this的方法。在static方法的内部不能调用非静态方法，反过来可以。可以在没有创建任何对象的前提下，仅仅通过类本身来调用static方法。

Java中禁止使用全局方法，但再类中置入static方法就可以访问其他static方法和static域。

## 5.5 清理：终结处理和垃圾回收 ##

一旦垃圾回收器准备好释放对象占用的存储空间，将首先调用其finalize()方法，并且在下一次垃圾回收动作发生时，才会真正回收对象占用的内存。

1. 对象可能不被垃圾回收
2. 垃圾回收并不等于“析构”

只要程序没有濒临存储空间用完的那一刻，对象占用的空间就总也得不到释放。如果程序执行结束，并且垃圾回收器一直都没有释放你创建的任何对象的存储空间，则随着程序的推出，那些资源也会全部交还给操作系统。

### 5.5.1 finalize()的用途何在 ###

3. 垃圾回收只与内存有关

使用垃圾回收器的唯一原因是为了回收程序不再使用的内存。所以对于与垃圾回收有关的任何行为来说（尤其是finalize()方法），它们也必须同内存及其回收有关。

finalize()

### 5.5.2 你必须实施清理 ###

垃圾回收器的存在并不能完全代替析构函数。无论是“垃圾回收”还是“终结”，都不保证一定会发生。如果Java虚拟机（JVM）并未面临内存耗尽的情形，它是不会浪费时间去执行垃圾回收以恢复内存的。

无论是“垃圾回收”还是“终结”，都不保证一定会发生。如果Java虚拟机（JVM）并未面临内存耗尽的情形，它是不会浪费时间去执行垃圾回收以恢复内存的。

### 5.5.3 终结条件 ###

不能指望finalize()，必须创建其他的“清理”方法

当对某个对象不再感兴趣——也就是它可以被清理了，这个对象应该处于某种状态，使它占用的内存可以被安全地释放。

System.gc()用于强制进行终结动作

//TODO 10-12

### 5.5.4 垃圾回收器如何工作 ###

垃圾回收器对于提高对象的创建速度，具有明显的效果。

在某些Java虚拟机中，堆的实现截然不同：它更像一个传送带，每分配一个新对象，它就往前移动一格。这意味着对象存储空间分配速度非常快。Java的“堆指针”只是简单地移动到尚未分配的区域，其效率比得上C++在堆栈上分配空间的效率。

引用记数是一种简单但速度很慢的垃圾回收技术。每个对象都含有一个引用计数器，当有引用连接至对象时，引用计数加1.当引用离开作用域或被置为null时，引用计数减1。（如果对象之间存在循环引用，可能会出现“对象应该被回收，但引用计数却不为零”）。

更快的模式：对任何“活”的对象，一定能最终追溯到其存活在堆栈或静态存储区之中的引用。这个引用链条可能会穿过数个对象层次。对于发现的每个引用，必须追逐它所引用的对象，然后此对象包含的所有引用，如此反复进行，直到“根源于堆栈和静态存储区的引用”所形成的网络被访问为止。

Java虚拟机将采用一种自适应的垃圾回收技术。

1. 停止——复制（stop-and-copy）。显然这意味着，先暂停程序的运行（所以它不属于后台回收模式）然后将所有存活的对象从当前堆复制到另一个堆，没有被复制的全部都是垃圾。当对象被复制到新堆时，它们是一个挨着一个的，所以新堆保持紧凑排列，然后就可以按前述方法简单、直接地分配新控件了。
2. 当把对象从一处搬到另一处时，所有指向它的那些引用都必须修正。位于堆或静态存储区的引用可以直接被修正，但可能还有其他指向这些对象的引用，它们在遍历的过程中才能被找到（可以想象成有个表格，将旧地址映射至新地址）。

缺点
 
1. 效率低，两个堆来回倒腾，比实际需要多一倍的空间。
2. 程序进入稳定状态后，会产生少量垃圾甚至没有垃圾。而复制式回收站仍然会将所有内存自一处复制到另一处。

为了避免这种情形，一些Java虚拟机会进行检查：要是没有新垃圾产生，就会转换到另一种工作模式（即“自适应”）。这种模式称为标记——清扫（mark-and-sweep）。对于一般用途而言，“标记-清扫”方式速度相当慢，但是只会产生少量垃圾甚至不会产生垃圾时，它的速度就很快了。

“标记-清扫”所依据的思路同样是从堆栈和静态存储区出发，遍历所有的引用，进而找出所有存活的对象。每当它找到一个存活丢向，就会给对象设一个标记，这个过程中不会回收任何对象。只有全部标记工作完成的时候，清理动作才会开始。在清理过程中，没有标记的对象将被释放，不会发生任何复制动作。所以剩下的堆空间是不连续的，垃圾回收器要是希望得到连续空间的话，就得重新整理剩下的对象。

“停止-复制”的意思是这种垃圾回收动作不是在后台进行的；相反，垃圾回收动作发生的同时，程序将会被暂停。当可用内存数量较低时，Sun版本的垃圾回收器会暂停运行程序，同样，“标记——清扫”工作也必须在程序暂停的情况下才能进行。

Java虚拟机中，内存分配以较大的“块”为单位。如果对象较大，它会占用单独的块。严格来说，“停止——复制”要求在释放旧有对象之前，必须先把所有存活对象从旧堆复制到新堆，这将导致大量内存复制行为。有了块之后，垃圾回收器在回收的时候就可以往废弃的块里拷贝对象了。每个块都由相应的代数（generation count）来记录它是否还存活。通常，如果块在某处被引用，某代数会增加；垃圾回收器将对上次回收动作之后新分配的块进行整理。这对处理大量短命的临时对象很有帮助。垃圾回收器会定期进行完整的清理动作——大型对象仍然不会被复制（只是其代数会增加），内含小型对象的那些快则被复制并整理。Java虚拟机会进行检视，如果所有对象很稳定，垃圾回收器的效率降低的话，就会切换到“标记-清扫”方式；同样，Java虚拟机会跟踪“标记-清扫”的效果，要是堆空间出现很多碎片，就会切换回“停止——复制”方式。这就事“自适应”技术，即“自适应的、分代的、停止-复制、标记-清扫”式垃圾回收器。

Java虚拟机中由许多附加技术用以提升速度。尤其是与加载器操作有关的，被称为“即时”（Just-In-Time, JIT）编译器的技术。可以把程序全部或部分翻译成本地机器码，程序运行速度因此得以提升。当需要转载某个类（通常是在为该类创建第一个对象）时，编译器会先找到其.class文件，然后将该类的字节码装入内存。此时由两种方案可供选择。

1. 让即时编译器编译所有代码。 但这种方式会有两个缺陷：这种加载动作散落在整个程序声明周期内，累加起来要话很多时间；而且会增加可执行代码的长度（字节码比即时编译器展开后的本地机器码小很多），这将导致页面调度，从而降低程序速度。
2. 惰性评估（lazy evaluation），意思是即时编译器只在必要的时候才编译代码。新版的JDK的Java HotSpot技术都采用类似的方法。

## 5.6 成员初始化 ##

所有变量在使用前都能得到恰当的初始化。对于方法的局部变量，Java以编译时错误的形式来贯彻这种保证。

	void f() {
		int i;
		i++; //Error -- i not initialized
	}

就会得到一条出错消息，告诉i可能尚未初始化。当然，编译器也可以为i赋一个默认值，但是未初始化的局部变量更有可能是程序员的疏忽，所以采用默认值反而会掩盖这种失误。

在类里定义一个对象引用时，如果不将其初始化，此引用就会获得一个特殊值null。

### 5.6.1 指定初始化 ###

定义类成员变量的地方为其赋值。

## 5.7 构造器初始化 ##

可以用构造器来进行初始化。

对于所有基本类型和对象引用，包括再定义时已经指定初值的变量，编译器不会强制你一定要再构造器的某个地方或在使用它们之前对元素进行初始化——因为初始化早已得到了保证。

### 5.7.1 初始化顺序 ###

在类的内部，变量定义的先后顺序决定了初始化的顺序。即使变量定义散布于方法定义之间，它们仍旧会在任何方法（包括构造器）被调用之前得到初始化。

	class House {
		Window w1 = new Window(1); //Before constructor
		House() {
			// Show that we're in the constructor:
			print("House()");
			w3 = new Window(33); // Reinitialize w3
		}
		Window w2 = new Window(2); // after constructor
		void f() { print("f()");}
		WIndow w3 = new Window(3); // at end
	}

w3这个引用会被初始化两次：一次再调用构造器前，一次再调用期间（第一次引用的对象将被丢弃，并作为垃圾回收）。如果定义了一个重载构造器，它没有初始化w3；同时再w3的定义里也没有指定默认值。

### 5.7.2 静态数据的初始化 ###

无论创建多少个对象，静态数据都只占用一份存储区域。static关键字不能应用与局部变量，因此它只能作用于域。如果一个域是静态的基本类型域，且也没有对它进行初始化，那么它就会获得基本类型的标准初值；如果它是一个对象引用，那么它的默认初始化值就是null。

初始化的顺序是先静态对象，而后是“非静态”对象。从输出结果中可以观察到这一点。要执行main()(静态方法)，必须加载StaticInitialization类，然后其静态域table和cupboard被初始化，这将导致它们对应的类也被加载，而且由于它们也都包含静态的Bowl对象，因此Bowl随后也被加载。

总结下对象的创建过程：

1. 即使没有显式地使用static关键字，构造器实际上也是静态方法。因此，当首次创建类型为Dog的对象时（构造器可以看成静态方法），或者Dog类的静态方法/静态域首次被访问时，Java解释器必须查找类路径，以定位Dog.class文件。
2. 然后载入Dog.class，有关静态初始化的所有动作都会执行。因此，静态初始化只在Class对象首次加载的时候进行一次。
3. 当用new Dog()创建对象的时候，首先将在堆上为Dog对象分配足够的存储空间。
4. 这块存储空间会被清零，这就自动地将Dog对象中的所有基本类型数据被设置成了默认值（对数字来说就是0，对布尔型和字符型也相同），而引用则被设置成了null。
5. 执行所有出现于字段定义处的初始化动作。
6. 执行构造器。

### 5.7.3 显式的静态初始化 ###

Java允许将多个静态初始化动作组织成一个特殊的“静态子句”（有时也叫做“静态块”）。

### 5.7.4 非静态实例初始化 ###

用来初始化每一个对象的非静态变量。

## 5.8 数组初始化 ##

数组只是相同类型的、用一个标识符名称封装到一起的一个对象序列或基本类型数据序列。数组是通过方括号下标操作符[]来定义和使用的。

如果在编写程序时，并不能确定在数组里需要多少个元素。可以直接用new在数组里创建元素。尽管创建的是基本类型数组，new仍然可以工作。

## 5.9 枚举类型 ##

	public enum Spiciness {
		NOT, MILD, MEDIUM, HOT, FLAMING
	}

尽管enum看起来像是一种心的数据类型，但是这个关键字只是为enum生成对应的类时，产生了某些编译器行为，在很多程度上，可以将enum当作其他任何类来处理。

//TODO 21-22

## 5.10 总结 ##

初始化在Java中占有至关重要的地位。构造器能保证正确的初始化和清理（没有正确的构造器调用，编译器就不允许创建对象）。

在Java中，垃圾回收器会自动为对象释放内存。