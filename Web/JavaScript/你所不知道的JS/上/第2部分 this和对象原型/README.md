你不知道的JavaScript

第二部分 this和对象原型

序

# 第1章 关于this #

this关键字是JavaScript中最复杂的机制之一。

## 1.1 为什么要用this ##

为什么要使用`this`

	function identify() {
		return this.name.toUppserCase();
	}
	function speak() {
		var greeting = "Hello, I'm " + identify.call(this);
		console.log(greeting);
	}
	var me = {
		name: "Kyle"
	};
	var you = {
		name: "Reader"
	};
	identify.call(me); //KYLE
	identify.call(you); //READER

	speak.call(me); //Hello,我是KYLE
	speak.call(you); //Hello,我是READER

## 1.2　误解 ##

### 1.2.1 指向自身 ###

既然函数看作一个对象（JavaScript 中的所有函数都是对象），那就可以在调用函数时存储状态（属性的值）。

### 1.2.2 它的作用域 ###

this 指向函数的作用域。

## 1.3 this到底是什么 ##

this 是在运行时进行绑定的，并不是在编写时绑定，它的上下文取决于函数调用时的各种条件。 this 的绑定和函数声明的位置没有任何关系，只取决于函数的调用方式。

## 1.4 小结 ##

this 实际上是在函数被调用时发生的绑定，它指向什么完全取决于函数在哪里被调用。

# 第2章 this全面解析 #

## 2.1 调用位置 ##

在理解 this 的绑定过程之前，首先要理解调用位置：调用位置就是函数在代码中被调用的位置（而不是声明的位置）。

	function baz() {
		// 当前调用栈是：baz
		// 因此，当前调用位置是全局作用域
		console.log( "baz" );
		bar(); // <-- bar 的调用位置
	}
	function bar() {
		// 当前调用栈是 baz -> bar
		// 因此，当前调用位置在 baz 中
		console.log( "bar" );
		foo(); // <-- foo 的调用位置
	}	

	function foo() {
		// 当前调用栈是 baz -> bar -> foo
		// 因此，当前调用位置在 bar 中
		console.log( "foo" );
	}
	baz(); // <-- baz 的调用位置

## 2.2 绑定规则 ##

查看真正的调用位置

1. 调用栈想象成一个函数调用链
2. 运行代码时，调试器会在那个位置暂停，同时会展示当前位置的函数
调用列表，这就是你的调用栈。因此，如果你想要分析 this 的绑定，使用开发者工具得到调用栈，然后找到栈中第二个元素，这就是真正的调用位置

### 2.2.1 默认绑定 ###

独立函数调用

	function foo() {
		console.log(this.a);
	}
	var a = 2;
	foo(); //2

1. 声明在全局作用域中的变量（比如 var a = 2 ）就是全局对象的一个同名属性。
2. 当调用 foo() 时， this.a 被解析成了全局变量 a 。为什么？因为在本例中，函数调用时应用了 this 的默认绑定，因此 this 指向全局对象。
3. 这里应用了默认绑定呢？可以通过分析调用位置来看看 foo() 是如何调用的。在代码中， foo() 是直接使用不带任何修饰的函数引用进行调用的，因此只能使用默认绑定，无法应用其他规则。

如果使用严格模式（strict mode），那么全局对象将无法使用默认绑定，因此 this 会绑定到 undefined：

	function foo() {
		"use strict";

		console.log(this.a);
	}
	var a = 2;
	foo; //TypeError: this is undefined
虽然 this 的绑定规则完全取决于调用位置，但是只有 foo() 运行在非 strict mode 下时，默认绑定才能绑定到全局对象；严格模式下与 foo()
的调用位置无关。

	function foo() {
		console.log(this.a);
	}
	var a = 2;
	(function() {
		"use strict";
		
		foo	();
	})();

### 2.2.2 隐式绑定 ###

需要考虑的规则是调用位置是否有上下文对象，或者说是否被某个对象拥有或者包含。

	function foo() {
		console.log( this.a );
	}
	var obj = {
		a: 2,
		foo: foo
	};
	obj.foo(); // 2
1. 需要注意的是 foo() 的声明方式，及其之后是如何被当作引用属性添加到 obj 中的。但是无论是直接在 obj 中定义还是先定义再添加为引用属性，这个函数严格来说都不属于obj 对象。
2. 调用位置会使用 obj 上下文来引用函数，因此你可以说函数被调用时 obj 对象“拥有”或者“包含”它。
3. 无论你如何称呼这个模式，当 foo() 被调用时，它的落脚点确实指向 obj 对象。当函数引用有上下文对象时，隐式绑定规则会把函数调用中的 this 绑定到这个上下文对象。因为调用 foo() 时 this 被绑定到 obj ，因此 this.a 和 obj.a 是一样的。
4. 对象属性引用链中只有最顶层或者说最后一层会影响调用位置。

	function foo() {
		console.log( this.a );
	}
	var obj2 = {
		a: 42,
		foo: foo
	};
	var obj1 = {
		a: 2,
		obj2: obj2
	}
	obj1.obj2.foo(); //42

**隐式丢失**

一个最常见的 this 绑定问题就是被隐式绑定的函数会丢失绑定对象，也就是说它会应用默认绑定，从而把 this 绑定到全局对象或者 undefined 上，取决于是否是严格模式。

	function foo() {
		console.log( this.a );
	}
	var obj = {
		a: 2,
		foo: foo
	};
	var bar = obj.foo; //
	var a = "oops, global";
	bar();
虽然 bar 是 obj.foo 的一个引用，但是实际上，它引用的是 foo 函数本身，因此此时的bar() 其实是一个不带任何修饰的函数调用，因此应用了默认绑定。

	function foo() {
		console.log( this.a );
	}
	function doFoo(fn) {
		//fn其实引用的是foo
		fn(); // <-- 调用位置！
	}
	var obj = {
		a: 2,
		foo: foo
	};
	var a = "oops, global';
	doFoo(obj.foo); 

回调函数丢失 this 绑定是非常常见的。

调用回调函数的函数可能会修改 this 。在一些流行的JavaScript 库中事件处理器常会把回调函数的 this 强制绑定到触发事件的 DOM 元素上。

### 2.2.3 显式绑定 ###

在分析隐式绑定时，我们必须在一个对象内部包含一个指向函数的属性，并通过这个属性间接引用函数，从而把 this 间接（隐式）绑定到这个对象上。

可以使用函数的 call(..) 和 apply(..) 方法。

它们的第一个参数是一个对象，它们会把这个对象绑定到this ，接着在调用函数时指定这个 this 。因为你可以直接指定 this 的绑定对象，因此我
们称之为显式绑定。

	function foo() {
		console.log(this.a);
	}
	var obj = {
		a:2
	};
	foo.call(obj); // 2
通过foo.call(..) ,我们可以在调用 foo 时强制把它的 this 绑定到 obj 上。

如果你传入了一个原始值（字符串类型、布尔类型或者数字类型）来当作 this 的绑定对象，这个原始值会被转换成它的对象形式（也就是 new String(..) 、 new Boolean(..) 或者 new Number(..) ）。这通常被称为“装箱”。

**1. 硬绑定**

	function foo() {
		console.log( this. a);
	}
	var obj = {
		a:2
	};
	var bar = function() {
		foo.call(obj);
	}
	bbar(); // 2
	setTimeout(bar, 100); //2

	// 硬绑定的 bar 不可能再修改它的 this
	bar.call(window); // 2

1.创建了函数bar()，并在它的内部手动调用了foo.call(obj)，因此强制把foo的this绑定到了obj。无论之后如何调用函数bar，它总会手动在obj上调用foo。（这种绑定是一种显式的强制绑定，因此我们称之为硬绑定）。 

### 2.2.4 new绑定 ###

在传统的面向类的语言中，“构造函数”是类中的一些特殊方法，使用 new 初始化类时会调用类中的构造函数。

	something = new MyClass(); 

JavaScript 也有一个 new 操作符，使用方法看起来也和那些面向类的语言一样，绝大多数开发者都认为 JavaScript 中 new 的机制也和那些语言一样。然而，JavaScript 中 new 的机制实际上和面向类的语言完全不同。

1. 重新定义一下 JavaScript 中的“构造函数”。在 JavaScript 中，构造函数只是一些使用 new 操作符时被调用的函数。它们并不会属于某个类，也不会实例化一个类。实际上，它们甚至都不能说是一种特殊的函数类型，它们只是被 new 操作符调用的普通函数而已。
2. 包括内置对象函数（比如 Number(..) ，详情请查看第 3 章）在内的所有函数都可以用 new 来调用，这种函数调用被称为构造函数调用。这里有一个重要但是非常细微的区别：实际上并不存在所谓的“构造函数”，只有对于函数的“构造调用”。

使用 new 来调用函数，或者说发生构造函数调用时，会自动执行下面的操作。

1. 创建（或者说构造）一个全新的对象。
2. 这个新对象会被执行 [[ 原型 ]] 连接。
3. 这个新对象会绑定到函数调用的 this 。
4. 如果函数没有返回其他对象，那么 new 表达式中的函数调用会自动返回这个新对象。

	function foo(a) {
		this.a = a;
	}
	var bar = new foo(2);
	console.log( bar.2 ); //2
使用new来调用foo(..)时，，构造一个新对象并把它绑定到foo（..）调用中的this上。new是最后一种可以影响函数调用时this绑定行为的方法，我们称之为new绑定。

## 2.3 优先级 ##

默认绑定的优先级是四条规则中最低的。

	function foo() {
		console.log( this.a );
	}
	var obj1 = {
		a: 2,
		foo: foo
	};
	var obj2 = {
		a: 3,
		foo: foo
	};
	obj1.foo(); //2
	obj2.foo(); //3
	obj1.foo.call(obj2); //3
	obj2.foo.call(obj1); //2
显式绑定优先级更高，也就是说在判断时应当先考虑是否可以应用显式绑定。

	function foo(something) {
		this.a = something;
	}
	var obj1 = {
		foo: foo
	};
	var obj2 = {};
	obj1.foo( 2 );
	console.log( obj1.a ); // 2
	obj1.foo.call( obj2, 3 );
	console.log( obj2.a ); // 3
	var bar = new obj1.foo( 4 );
	console.log( obj1.a ); // 2
	console.log( bar.a ); // 4
可以看到 new 绑定比隐式绑定优先级高。

new 和 call / apply 无法一起使用，因此无法通过 new foo.call(obj1) 来直接进行测试。但是我们可以使用硬绑定来测试它俩的优先级。

ES5 中内置的 Function.prototype.bind(..) 更加复杂。

	if (!Function.prototype.bind) {
		Function.prototype.bind = function(oThis) {
			if (typeof this !== "function") {
				// 与 ECMAScript 5 最接近的
				// 内部 IsCallable 函数
				throw new TypeError(
				"Function.prototype.bind - what is trying " +
				"to be bound is not callable"
				);
			}
			var aArgs = Array.prototype.slice.call(arguments, 1);
			fToBind = this,
			fNop = function(){},
			fBound = function() {
				return fToBind.apply(
				(this instanceof fNOP && oThis ? this : oThis),
				aArgs.concat(Array.prototype.slice.call(arguments))
				)
			}
			fNOP.prototype = this.prototype;
			fBound.prototype = new fNOP();

			return fBound;
		}
	}

# 第3章 对象 #

## 3.1 语法 ##

对象可以通过两种形式定义：声明（文字）形式和构造形式。

	var myObj = {
		key: value
		// ...
	};

构造形式大概是这样：

	var myObj = new Object();
	myObj.key = value;

构造形式和文字形式生成的对象是一样的。唯一的区别是，在文字声明中你可以添加多个键 / 值对，但是在构造形式中你必须逐个添加属性。

## 3.2 类型 ##

对象是 JavaScript 的基础。

* string
* number
* boolean
* null
* undefined
* object

简单基本类型（ string 、 boolean 、 number 、 null 和 undefined ）本身并不是对象。但是这其实只是语言本身的一个 bug，即对 null 执行 typeof null 时会返回字符串 "object" 。 实际上， null 本身是基本类型。

函数就是对象的一个子类型（从技术角度来说就是“可调用的对象”）。JavaScript 中的函数是“一等公民”，因为它们本质上和普通的对象一样（只是可以调用），所以可以像操作其他对象一样操作函数（比如当作另一个函数的参数）。

**内置对象**

JavaScript 中还有一些对象子类型，通常被称为内置对象。

* String
* Number
* Boolean
* Object
* Function
* Array
* Date
* RegExp
* Error

JavaScript 中还有一些对象子类型，通常被称为内置对象。

	var strPrimitive = "I am a string";
	typeof strPrimitive; // "string"
	strPrimitive instanceof String; // false
	
	var strObject = new String( "I am a string" );
	typeof strObject; // "object"
	strObject instanceof String; // true
	
	// 检查 sub-type 对象
	Object.prototype.toString.call( strObject ); // [object String]

## 3.3 内容 ##

对象的内容是由一些存储在特定命名位置的（任意类型的）值组成的，
我们称之为属性。

在引擎内部，这些值的存储方式是多种多样的，一般并不会存在对象容器内部。存储在对象容器内部的是这些属性的名称，它们就像指针（从技术角度
来说就是引用）一样，指向这些值真正的存储位置。

	var myObject = {
		a: 2
	};
	myObject.a; //2
	myObject["a"]; //2
如果要访问 myObject 中 a 位置上的值，我们需要使用 . 操作符或者 [] 操作符。 .a 语法通常被称为“属性访问”， ["a"] 语法通常被称为“键访问”。实际上它们访问的是同一个位置，并且会返回相同的值 2

这两种语法的主要区别在于 . 操作符要求属性名满足标识符的命名规范，而 [".."] 语法可以接受任意 UTF-8/Unicode 字符串作为属性名。

### 3.3.1 可计算属性名 ###

如果你需要通过表达式来计算属性名，那么我们刚刚讲到的 myObject[..] 这种属性访问语法就可以派上用场了，如可以使用 myObject[prefix + name] 。但是使用文字形式来声明对象时这样做是不行的。

ES6 增加了可计算属性名，可以在文字形式中使用 [] 包裹一个表达式来当作属性名。

	var prefix = "foo";
	var myObject = {
		[prefix + "bar"]:"hello",
		[prefix + "baz"]: "world"
	};
	myObject["foobar"]; // hello
	myObject["foobaz"]; // world

### 3.3.2 属性与方法 ###

有些函数具有 this 引用，有时候这些 this 确实会指向调用位置的对象引用。


### 3.3.3 数组 ###

数组也支持 [] 访问形式，数组有一套更加结构化的值存储机制（不过仍然不限制值的类型）。数组期望的是数值下标，也就是说值存储的位置（通
常被称为索引）是整数。

	var myArray = [ "foo", 42, "bar" ];
	myArray.length; // 3
	myArray[0]; // "foo"
	myArray[2]; // "bar"

数组也是对象，所以虽然每个下标都是整数，你仍然可以给数组添加属性。

	var myArray = [ "foo", 42, "bar" ];
	myArray.baz = "baz";
	myArray.length; // 3
	myArray.baz; // "baz"

可以看到虽然添加了命名属性（无论是通过 . 语法还是 [] 语法），数组的 length 值并未发生变化。

你完全可以把数组当作一个普通的键 / 值对象来使用，并且不添加任何数值索引，但是这并不是一个好主意。数组和普通的对象都根据其对应的行为和用途进行了优化，所以最好*只用对象来存储键 / 值对，只用数组来存储数值下标 / 值对*。

如果你试图向数组添加一个属性，但是属性名“看起来”像一个数字，那它会变成一个数值下标（因此会修改数组的内容而不是添加一个属性）

	var myArray = [ "foo", 42, "bar" ];
	myArray["3"] = "baz";
	myArray.length; // 4
	myArray[3]; // "baz"

### 3.3.4 复制对象 ###

如何复制一个对象。

	function anotherFunction() { /*..*/ }

	var anotherObject = {
		c: true
	};

	var anotherArray = [];

	var myObject = {
		a: 2,
		b: anotherObject, // 引用，不是复本！
		c: anotherArray, // 另一个引用！
		d: anotherFunction
	};

	anotherArray.push( anotherObject, myObject );

我们应该判断它是浅复制还是深复制。对于浅拷贝来说，复制出的新对象中 a 的值会复制旧对象中 a 的值，也就是 2，但是新对象中 b 、 c 、 d 三个属性其实只是三个引用，它们和旧对象中 b 、 c 、 d 引用的对象是一样的。对于深复制来说，除了复制 myObject 以外还会复制 anotherObject 和 anotherArray 。这时问题就来了，anotherArray 引用了 anotherObject 和 myObject ，所以又需要复制 myObject ，这样就会由于循环引用导致死循环。

对于 JSON 安全（也就是说可以被序列化为一个 JSON 字符串并且可以根据这个字符串解析出一个结构和值完全一样的对象）的对象来说，有一种巧妙的复制方法：

	var newObj = JSON.parse( JSON.stringify( someObj ) );

相比深复制，浅复制非常易懂并且问题要少得多，所以 ES6 定义了 Object.assign(..) 方法来实现浅复制。 Object.assign(..) 方法的第一个参数是目标对象，之后还可以跟一个或多个源对象。它会遍历一个或多个源对象的所有可枚举（enumerable，参见下面的代码）的自有键（owned key，很快会介绍）并把它们复制（使用 = 操作符赋值）到目标对象，最后返回目标对象

	var newObj = Object.assign( {}, myObject );
	newObj.a; // 2
	newObj.b === anotherObject; // true
	newObj.c === anotherArray; // true
	newObj.d === anotherFunction; // true

*由于 Object.assign(..) 就是使用 = 操作符来赋值，所
以源对象属性的一些特性（比如 writable ）不会被复制到目标对象。*

### 3.3.5 属性描述符 ###

在 ES5 之前，JavaScript 语言本身并没有提供可以直接检测属性特性的方法，比如判断属性是否是只读。

但是从 ES5 开始，所有的属性都具备了属性描述符。

	var myObject = {
		a : 2
	};
	Object.getOwnPropertyDescriptor( myObject, "a" );
	// {
	// value: 2,
	// writable: true,
	// enumerable: true,
	// configurable: true
	// }

这个普通的对象属性对应的属性描述符（也被称为“数据描述符”，因为它
只保存一个数据值）可不仅仅只是一个 2。它还包含另外三个特性： writable（可写）、enumerable（可枚举）和 configurable （可配置）。

在创建普通属性时属性描述符会使用默认值，我们也可以使用 Object.defineProperty(..)来添加一个新属性或者修改一个已有属性（如果它是 configurable ）并对特性进行设置。

	var myObject = {};
	Object.defineProperty( myObject, "a", {
		value: 2,
		writable: true,
		configurable: true,
		enumerable: true
	} );
	myObject.a; // 2

defineProperty(..) 给 myObject 添加了一个普通的属性并显式指定了一些特性。

1. Writable writable 决定是否可以修改属性的值。

	var myObject = {};
	Object.defineProperty( myObject, "a", {
		value: 2,
		writable: false, // 不可写！
		configurable: true,
		enumerable: true
	});
	myObject.a = 3;
	myObject.a; // 2

2. Configurable 只要属性是可配置的，就可以使用 defineProperty(..) 方法来修改属性描述符：

	var myObject = {
		a:2
	};
	myObject.a = 3;
	myObject.a; // 3
	Object.defineProperty( myObject, "a", {
		value: 4,
		writable: true,
		configurable: false, // 不可配置！
		enumerable: true
	});

	myObject.a; // 4
	myObject.a = 5;
	myObject.a; // 5

	Object.defineProperty( myObject, "a", {
		value: 6,
		writable: true,
		configurable: true,
		enumerable: true
	} ); // TypeError

delete 只用来直接删除对象的（可删除）属性。

3. Enumerable

从名字就可以看出，这个描述符控制的是属性是否会出现在对象的属性枚举中，比如说 for..in 循环。如果把 enumerable 设置成 false ，这个属性就不会出现在枚举中，虽然仍然可以正常访问它。相对地，设置成 true 就会让它出现在枚举中。

### 3.3.6 不变性 ###

所有的方法创建的都是浅不变形，也就是说，它们只会影响目标对象和
它的直接属性。如果目标对象引用了其他对象（数组、对象、函数，等），其他对象的内容不受影响，仍然是可变的

	myImmutableObject.foo; // [1,2,3]
	myImmutableObject.foo.push( 4 );
	myImmutableObject.foo; // [1,2,3,4]

1. 对象常亮

结合 writable:false 和 configurable:false 就可以创建一个真正的常量属性（不可修改、重定义或者删除）

	var myObject = {};

	Object.defineProperty( myObject, "FAVORITE_NUMBER", {
		value: 42,
		writable: false,
		configurable: false
	});

2. 禁止扩展

如果你想禁止一个对象添加新属性并且保留已有属性，可以使用 Object.preventExtensions(..) 

	var myObject = {
		a:2
	};
	Object.preventExtensions( myObject );
	myObject.b = 3;
	myObject.b; // undefined

在非严格模式下，创建属性 b 会静默失败。在严格模式下，将会抛出 TypeError 错误。

3. 密封

Object.seal(..) 会创建一个“密封”的对象，这个方法实际上会在一个现有对象上调用 Object.preventExtensions(..) 并把所有现有属性标记为 configurable:false 。

所以，密封之后不仅不能添加新属性，也不能重新配置或者删除任何现有属性（虽然可以修改属性的值）。

4. 冻结

Object.freeze(..) 会创建一个冻结对象，这个方法实际上会在一个现有对象上调用Object.seal(..) 并把所有“数据访问”属性标记为 writable:false ，这样就无法修改它们的值。

这个方法是你可以应用在对象上的级别最高的不可变性，它会禁止对于对象本身及其任意直接属性的修改（不过就像我们之前说过的，这个对象引用的其他对象是不受影响的）。

你可以“深度冻结”一个对象，具体方法为，首先在这个对象上调用 Object.freeze(..) ，然后遍历它引用的所有对象并在这些对象上调用 Object.freeze(..) 。但是一定要小心，因为这样做有可能会在无意中冻结其他（共享）对象。

### 3.3.7  [[Get]] ###

属性访问在实现时有一个微妙却非常重要的细节

	var myObject = {
		a: 2
	};
	myObject.a; // 2

myObject.a 是一次属性访问，但是这条语句并不仅仅是在 myObjet 中查找名字为 a 的属性，虽然看起来好像是这样。

在语言规范中， myObject.a 在 myObject 上实际上是实现了 [[Get]] 操作（有点像函数调用： \[[Get]]() ）。对象默认的内置 [[Get]] 操作首先在对象中查找是否有名称相同的属性，
如果找到就会返回这个属性的值。

如果无论如何都没有找到名称相同的属性，那 [[Get]] 操作会返回值 undefined ：

	var myObject = {
		a:2
	};
	myObject.b; // undefined

### 3.3.8 [[PUT]] ###

既然有可以获取属性值的 [[Get]] 操作，就一定有对应的 [[Put]] 操作。

[[Put]] 被触发时，实际的行为取决于许多因素，包括对象中是否已经存在这个属性（这是最重要的因素）。

如果已经存在这个属性，[[Put]] 算法大致会检查下面这些内容。

1. 属性是否是访问描述符（参见 3.3.9 节）？如果是并且存在 setter 就调用 setter。
2. 属性的数据描述符中 writable 是否是 false ？如果是，在非严格模式下静默失败，在严格模式下抛出 TypeError 异常。
3. 如果都不是，将该值设置为属性的值。

### 3.3.9 Getter和Setter ###

对象默认的 [[Put]] 和 [[Get]] 操作分别可以控制属性值的设置和获取。

setter 会覆盖单个属性默认的 [[Put]] （也被称为赋值）操作。通常来说 getter 和 setter 是成对出现的（只定义一个的话通常会产生意料之外的行为）。

	var myObject = {
		// 给 a 定义一个 getter
		get a() {
			return this._a_;
		},
		// 给 a 定义一个 setter
		set a(val) {
			this._a_ = val * 2;
		}
	};
	myObject.a = 2;
	myObject.a; // 4

### 3.3.10 存在性 ###

如 myObject.a 的属性访问返回值可能是 undefined ，但是这个值有可能是属性中存储的 undefined ，也可能是因为属性不存在所以返回 undefined。

我们可以在不访问属性值的情况下判断对象中是否存在这个属性：
	var myObject = {
		a:2
	};
	("a" in myObject); // true
	("b" in myObject); // false
	myObject.hasOwnProperty( "a" ); // true
	myObject.hasOwnProperty( "b" ); // false

in 操作符会检查属性是否在对象及其 [[Prototype]] 原型链中。相比之下，hasOwnProperty(..) 只会检查属性是否在 myObject 对象中，不会检查 [[Prototype]] 链。

所有的普通对象都可以通过对于 Object.prototype 的委托来访问hasOwnProperty(..) ，但是有的对象可能没有连接到 Object.prototype 。在这种情况下，形如 myObejct.hasOwnProperty(..)就会失败。

Object.prototype.hasOwnProperty.call(myObject,"a")，它借用基础的 hasOwnProperty(..) 方法并把它显式绑定到 myObject 上。

1. 枚举

	var myObject = { };

	Object.defineProperty(
		myObject,
		"a",
		// 让 a 像普通属性一样可以枚举
		{ enumerable: true, value: 2 }
	);

	Object.defineProperty(
		myObject,
		"b",
		// 让 b 不可枚举
		{ enumerable: false, value: 3 }
	);

	myObject.b; // 3
	("b" in myObject); // true
	myObject.hasOwnProperty( "b" ); // true

	// ......

	for (var k in myObject) {
		console.log( k, myObject[k] );
	}
	// "a" 2

可以看到，myObject.b 确实存在并且有访问值，但是却不会出现在 for..in 循环中（尽管可以通过 in 操作符来判断是否存在）。原因是“可枚举”就相当于“可以出现在对象属性的遍历中”。

propertyIsEnumerable(..) 会检查给定的属性名是否直接存在于对象中（而不是在原型链上）并且满足 enumerable:true 。

Object.keys(..) 会返回一个数组，包含所有可枚举属性， Object.getOwnPropertyNames(..)会返回一个数组，包含所有属性，无论它们是否可枚举。

in 和 hasOwnProperty(..) 的区别在于是否查找 [[Prototype]] 链，然而， Object.keys(..) 和 Object.getOwnPropertyNames(..) 都只会查找对象直接包含的属性。

## 3.4 遍历 ##

for..in 循环可以用来遍历对象的可枚举属性列表（包括[[Prototype]] 链）。

对于数值索引的数组来说，可以使用标准的 for 循环来遍历值：

	var myArray = [1, 2, 3];
	
	for (var i = 0; i < myArray.length; i++) {
		console.log( myArray[i]);
	}
	//1 2 3

ES5 中增加了一些数组的辅助迭代器，包括 forEach(..) 、 every(..) 和 some(..) 。每种辅助迭代器都可以接受一个回调函数并把它应用到数组的每个元素上，唯一的区别就是它们对于回调函数返回值的处理方式不同。

forEach(..) 会遍历数组中的所有值并忽略回调函数的返回值。 every(..) 会一直运行直到回调函数返回 false （或者“假”值）， some(..) 会一直运行直到回调函数返回 true （或者“真”值）。

every(..) 和 some(..) 中特殊的返回值和普通 for 循环中的 break 语句类似，它们会提前终止遍历。

使用 for..in 遍历对象是无法直接获取属性值的，因为它实际上遍历的是对象中的所有可枚举属性，你需要手动获取属性值。

ES6 增加了一种用来遍历数组的 for..of 循环语法（如果对象本身定义了迭代器的话也可以遍历对象）

	var myArray = [ 1, 2, 3 ];
	
	for (var v of myArray) {
		console.log( v );
	}
	// 1
	// 2
	// 3
for..of 循环首先会向被访问对象请求一个迭代器对象，然后通过调用迭代器对象的next() 方法来遍历所有返回值。

数组有内置的 @@iterator ，因此 for..of 可以直接应用在数组上。我们使用内置的 @@iterator 来手动遍历数组。

	var myArray = [ 1, 2, 3 ];
	var it = myArray[Symbol.iterator]();
	it.next(); // { value:1, done:false }
	it.next(); // { value:2, done:false }
	it.next(); // { value:3, done:false }
	it.next(); // { done:true }

Symbol.iterator 来获取对象的 @@iterator 内部属性。引用类似 iterator 的特殊属性时要使用符号名，而不是符号包含的值。此外，虽然看起来很像一个对象，但是 @@iterator 本身并不是一个迭代器对象，而是一个返回迭代器对象的函数。

## 3.5 小结 ##

JavaScript 中的对象有字面形式（比如 var a = { .. } ）和构造形式（比如 var a = new Array(..) ）。

对象就是键 / 值对的集合。可以通过 .propName 或者 ["propName"] 语法来获取属性值。访问属性时，引擎实际上会调用内部的默认 [[Get]] 操作（在设置属性值时是 [[Put]] ），[[Get]] 操作会检查对象本身是否包含这个属性，如果没找到的话还会查找 [[Prototype]]链。

属性的特性可以通过属性描述符来控制，比如 writable 和 configurable 。此外，可以使用Object.preventExtensions(..) 、 Object.seal(..) 和 Object.freeze(..) 来设置对象（及其
属性）的不可变性级别。

# 第4章 混合对象“类” #

实例化（instantiation）、继承（inheritance）和（相对）多态（polymorphism）。

## 4.1 类理论 ##

类 / 继承描述了一种代码的组织结构形式——一种在软件中对真实世界中问题领域的建模方法。

面向对象编程强调的是数据和操作数据的行为本质上是互相关联的（当然，不同的数据有不同的行为），因此好的设计就是把数据以及和它相关的行为打包（或者说封装）起来。这在正式的计算机科学中有时被称为数据结构。

### 4.1.1 “类”设计模式 ###

迭代器模式、观察者模式、工厂模式、单例模式

### 4.1.2 JavaScript中的“类” ###

JavaScript 中实际上有类呢？简单来说：不是。

## 4.2 类的机制 ##

“标准库”会提供 Stack 类，它是一种“栈”数据结构（支持压入、弹出，等等）。 Stack 类内部会有一些变量来存储数据，同时会提供一些公有的可访问行为（“方法”），从而让你的代码可以和（隐藏的）数据进行交互（比如添加、删除数据）。

### 4.2.1 建造 ###

### 4.2.2 构造函数 ###

类实例是由一个特殊的类方法构造的，这个方法名通常和类名相同，被称为构造函数。这个方法的任务就是初始化实例需要的所有信息（状态）。

	class CoolGuy {
		specialTrick = nothing
		CoolGuy( trick ) {
			specialTrick = trick
		}
		showOff() {
			output( "Here's my trick: ", specialTrick )
		}
	}

## 4.3 类的继承 ##

在面向类的语言中，你可以先定义一个类，然后定义一个继承前者的类。

	class Vehicle {
		engines = 1

		ignition() {
			output( "Turning on my engine.");
		}
		drive() {
			ignition();
			output("Steering and moving forard!')
		}
	}
	class Car inherits Vehicle {
		wheels = 4
		drive() {
			inherited:drive()
			output( "Rolling on all ", wheels, " wheels!" )
		}
	}
	class SpeedBoat inherits Vehicle {
		engines = 2
		ignition() {
			output( "Turning on my ", engines, " engines." )
		}
		pilot() {
			inherited:drive()
			output( "Speeding through the water with ease!" )
		}
	}

### 4.3.1 多态 ###

Car 重写了继承自父类的 drive() 方法，但是之后 Car 调用了 inherited:drive() 方法，这表明 Car 可以引用继承来的原始 drive() 方法。

快艇的 pilot() 方法同样引用了原始drive() 方法。

多态或者虚拟多态，相对多态。

### 4.3.2 多重继承 ###

有些面向类的语言允许你继承多个“父类”。多重继承意味着所有父类的定义都会被复制到子类中。

## 4.4 混入 ##

在继承或者实例化时，JavaScript 的对象机制并不会自动执行复制行为。
JavaScript 中只有对象，并不存在可以被实例化的“类”。一个对象并不会被复制到其他对象，它们会被关联起来。

模拟类的复制行为，显式和隐式。

### 4.4.1 显式混入 ###

	//mixin(..)例子
	function mixin( sourceObj, targetObj) {
		for (var key in sourceObj) {
			// 只会在不存在的情况下复制
			if(!(key in targetObj)) {
				targetObj[key] = sourceObj[key];
			}
		}
		return targetObj;
	}
	var Vehicle = {
		engines: 1,
		ignition: function() {
			console.log("Turning on my engine.");
		},
		drive: function() {
			this.ignition();
			console.log("Steering and moving forward!");
		}
	};
	var Car = mixin(Vehicle, {
		wheels: 4,
		drive: function() {
			Vehicle.drive.call( this );
			console.log(
				"Rolling on all " + this.wheels + " wheels !"
			);
		}
	})

#### 1.再说多态 ####

	 Vehicle.drive.call( this ) 显式多态

	 inherited:drive() 相对多态

JavaScript并没有相对多态的机制。所以，由于 Car 和Vehicle 中都有 drive() 函数，为了指明调用对象，我们必须使用绝对（而不是相对）引用。我们通过名称显式指定 Vehicle 对象并调用它的 drive() 函数。

#### 2. 混合复制 ####

## 4.5 小结 ##

类是一种设计模式。JavaScript 也有类似的语法，但是和其他语言中的类完全不同。

类意味着复制。

传统的类被实例化时，它的行为会被复制到实例中。类被继承时，行为也会被复制到子类中。

多态（在继承链的不同层次名称相同但是功能不同的函数）看起来似乎是从子类引用父类，但是本质上引用的其实是复制的结果。

混入模式（无论显式还是隐式）可以用来模拟类的复制行为，但是通常会产生丑陋并且脆弱的语法，比如显式伪多态（ OtherObj.methodName.call(this, ...) ），这会让代码更加难懂并且难以维护。

# 第5章 原型 #

## 5.1 [[Prototype]] ##

JavaScript 中的对象有一个特殊的 [[Prototype]] 内置属性，其实就是对于其他对象的引用。几乎所有的对象在创建时 [[Prototype]] 属性都会被赋予一个非空的值。

	var myObject = {
		a: 2
	};
	myObject.2; //2

对于默认的 [[Get]] 操作来说，第一步是检查对象本身是否有这个属性，如果有的话就使用它。	

ES6 中的 Proxy

对于默认的 [[Get]] 操作来说，如果无法在对象本身找到需要的属性，就会继续访问对象的 [[Prototype]] 链：

	var anotherObject = {
		a: 2
	};
	// 创建一个关联到anotherObejct的对象
	var myObject = Object.create( anotherObject );
	myObject.a; //2

现在 myObject 对象的 [[Prototype]] 关联到了 anotherObject 。显然 myObject.a 并不存在，但是尽管如此，属性访问仍然成功地（在 anotherObject 中）找到了值 2。

### 5.1.1 Object.prototype ###

所有普通的 [[Prototype]] 链最终都会指向内置的 Object.prototype 。由于所有的“普通”（内置，不是特定主机的扩展）对象都“源于”（或者说把 [[Prototype]] 链的顶端设置为）这个 Object.prototype 对象，所以它包含 JavaScript 中许多通用的功能。

### 5.1.2 属性设置和屏蔽 ###

给一个对象设置属性并不仅仅是添加一个新属性或者修改已有的属性值。

	myObject.foo = "bar";

如果 myObject 对象中包含名为 foo 的普通数据访问属性，这条赋值语句只会修改已有的属性值。

如果 foo 不是直接存在于 myObject 中， [[Prototype]] 链就会被遍历，类似 [[Get]] 操作。如果原型链上找不到 foo ， foo 就会被直接添加到 myObject 上。

如果 foo 存在于原型链上层，赋值语句 myObject.foo = "bar" 的行为就会有些不同（而且可能很出人意料）。

如果属性名 foo 既出现在 myObject 中也出现在 myObject 的 [[Prototype]] 链上层，那么就会发生屏蔽。 myObject 中包含的 foo 属性会屏蔽原型链上层的所有 foo 属性，因为myObject.foo 总是会选择原型链中最底层的 foo 属性。

如果 foo 不直接存在于 myObject 中而是存在于原型链上层时 myObject.foo = "bar" 会出现的三种情况。

1. 如果在 [[Prototype]] 链上层存在名为 foo 的普通数据访问属性并且没有被标记为只读（ writable:false ），那就会直接在 myObject 中添加一个名为 foo 的新属性，它是屏蔽属性。
2. 如果在 [[Prototype]] 链上层存在 foo ，但是它被标记为只读（ writable:false ），那么无法修改已有属性或者在 myObject 上创建屏蔽属性。如果运行在严格模式下，代码会抛出一个错误。否则，这条赋值语句会被忽略。总之，不会发生屏蔽。
3. 如果在 [[Prototype]] 链上层存在 foo 并且它是一个 setter，那就一定会调用这个 setter。 foo 不会被添加到（或者说屏蔽于） myObject ，也不会重新定义 foo 这个 setter。

如果你希望在第二种和第三种情况下也屏蔽 foo ，那就不能使用 = 操作符来赋值，而是使用 Object.defineProperty(..) 来向 myObject 添加 foo 。

## 5.2 “类” ##

JavaScript 和面向类的语言不同，它并没有类来作为对象的抽象模式
或者说蓝图。JavaScript 中只有对象。

### 5.2.1 “类”函数 ###

“类似类”的行为利用了函数的一种特殊特性：所有的函数默认都会拥有一个
名为 prototype 的公有并且不可枚举的属性。

	function Foo() {
		// ...
	}
	Foo.prototype; // {}
这个对象通常被称为 Foo 的原型，因为我们通过名为 Foo.prototype 的属性引用来访问它。

这个对象是在调用 new Foo() 时创建的，最后会被（有点武断地）关联到这个“Foo 点 prototype”对象上。

	function Foo() {
		// ...
	}
	var a = new Foo();
	Object.getPrototypeOf(a) === Foo.prototype; //true
调用new Foo()时会创建a，其中的一步就是给a一个内部的[[Prototype]]链接，关联到Foo.prototype指向的那个对象。

在 JavaScript 中，并没有类似的复制机制。你不能创建一个类的多个实例，只能创建多个对象，它们 [[Prototype]] 关联的是同一个对象。但是在默认情况下并不会进行复制，因此这些对象之间并不会完全失去联系，它们是互相关联的。

new Foo() 会生成一个新对象（我们称之为 a ），这个新对象的内部链接 [[Prototype]] 关联的是 Foo.prototype 对象。

在JavaScript中，我们并不会将一个对象（“类”）复制到另一个对象（“实例”），只是将他们关联起来。
	
这类机制通常称为原型继承，常常被视为动态语言版本的类继承

继承意味着复制操作，JavaScript（默认）并不会复制对象属性。相反，JavaScript 会在两个对象之间创建一个关联，这样一个对象就可以通过委托访问另一个对象的属性和函数。

基本原则是在描述对象行为时，使用其不同于普遍描述的特质。

### 5.2.2 “构造函数” ###

其中一个原因是我们看到了关键字 new ，在面向类的语言中构造类实例时也会用到它。另一个原因是，看起来我们执行了类的构造函数方法， Foo() 的调用方式很像初始化类时类构造函数的调用方式。

	function Foo() {
		//...
	}
	Foo.prototype.constructor === Foo; //true
	var a = new Foo();
	a.constructor === Foo; // true

Foo.prototype默认有一个公有并且不可枚举的属性.constructor，这个属性引用的是对象关联的函数。

我们可以看到通过“构造函数”调用 new Foo() 创建的对象也有一个 .constructor 属性，指向“创建这个对象的函数”。

1. 构造函数还是调用

当你在普通的函数调用前面加上 new 关键字之后，就会把这个函数调用变成一个“构造函数调用”

new 会劫持所有普通函数并用构造对象的形式来调用它。

### 5.2.3 技术 ###

	function Foo(name) {
		this.name = name;
	}

	Foo.prototype.myName = function() {
		return this.name;
	};
	var a = new Foo("a");
	var b = new Foo("b");
	a.myName(); // "a"
	b.myName(); // "b"

1. this.name = name 给每个对象都添加了.name属性，优点像类实例封装的数据值。	
2. Foo.prototype.myName = ... ，它会给Foo.prototype对象添加一个属性（函数）

看起来似乎创建a和b会把Foo.prototype对象复制到这两个对象中，然而事实并不是这样。默认的[[Get]]算法会在当属性不直接存在于对象中时通过[[Prototype]]进行查找。

在创建的过程中， a 和 b 的内部 [[Prototype]] 都会关联到 Foo.prototype 上。当 a和 b 中无法找到 myName 时，它会通过委托在 Foo.prototype 上找到。

**回顾“构造函数”**

当你在普通的函数调用前面加上 new 关键字之后，就会把这个函数调用变成一个“构造函数调用”

new 会劫持所有普通函数并用构造对象的形式来调用它。


## 5.5 小结 ##

如果要访问对象中并不存在的一个属性， [[Get]] 操作就会查找对象内部[[Prototype]] 关联的对象。这个关联关系实际上定义了一条“原型链”（有点像嵌套的作用域链），在查找属性时会对它进行遍历。

所有普通对象都有内置的 Object.prototype ，指向原型链的顶端（比如说全局作用域），如果在原型链中找不到指定的属性就会停止。 toString() 、 valueOf() 和其他一些通用的功能都存在于 Object.prototype 对象上，因此语言中所有的对象都可以使用它们。

关联两个对象最常用的方法是使用 new 关键词进行函数调用，在调用的 4 个步骤中会创建一个关联其他对象的新对象。

使用 new 调用函数时会把新对象的 .prototype 属性关联到“其他对象”。带 new 的函数调用通常被称为“构造函数调用”，尽管它们实际上和传统面向类语言中的类构造函数不一样。

虽然这些 JavaScript 机制和传统面向类语言中的“类初始化”和“类继承”很相似，但是 JavaScript 中的机制有一个核心区别，那就是不会进行复制，对象之间是通过内部的[[Prototype]] 链关联的。

相比之下，“委托”是一个更合适的术语，因为对象之间的关系不是复制而是委托。

# 第6章 行为委托 #

## 6.1 面向委托的设计 ##

为了更好地学习如何更直观地使用 [[Prototype]] ，我们必须认识到它代表的是一种不同于类的设计模式。

*面向类的设计中 有些 原则依然有效，因此不要把所有知识都抛掉。（只需要抛掉大部分就够了！）举例来说， 封装 是非常有用的，它同样可以应用在委托中（虽然不太常见）。*

### 6.1.1 类理论 ###

	Task = {
		setID: function(ID) { this.id = ID; },
		outputID: function() { console.log( this.id ); }
	};
	// 让 XYZ 委托 Task
	XYZ = Object.create( Task );
	XYZ.prepareTask = function(ID,Label) {
		this.setID( ID );
		this.label = Label;
	};
	XYZ.outputTaskDetails = function() {
		this.outputID();
		console.log( this.label );
	};
	// ABC = Object.create( Task );
	// ABC ... = ...

1. 互相委托（禁止）

之所以要禁止互相委托，是因为引擎的开发者们发现在设置时检查（并禁止！）一次无限循环引用要更加高效，否则每次从对象中查找属性时都需要进行检查。

2. 调试

	function Foo() {}
	var a1 = new Foo();
	a1; // Foo {}

Chrome 实际上想说的是“ {} 是一个空对象，由名为 Foo 的函数构造”。Firefox 想说的是“ {}
是一个空对象，由 Object 构造”。之所以有这种细微的差别，是因为 Chrome 会动态跟踪并把
实际执行构造过程的函数名当作一个内置属性，但是其他浏览器并不会跟踪这些额外的信息。

	function Foo() {}
	var a1 = new Foo();
	a1.constructor; // Foo(){}
	a1.constructor.name; // "Foo"

### 6.1.3 比较思维模型 ###

子类 Bar 继承了父类 Foo ，然后生成了 b1 和 b2 两个实例。 b1 委托了 Bar.prototype ，后者委托了 Foo.prototype 。

## 6.2 类与对象 ##

创建 UI 控件（按钮、下拉列表，等等）

### 6.2.1 控件“类” ###

一个包含所有通用控件行为的父类（可能叫作 Widget ）和继承父类的特殊控件子类（比如 Button ）

//父类
function Widget(width, height) {
	this.width = width || 50;
	this.height = height || 50;
	this.$elem = null;
}

Widget.prototype.render = function($where) {
	if (this.$elem) {
		this.$elem.css( {
			width: this.width + "px",
			height: this.height + "px"
		} ).appendTo($where);
	}
}

// 子类
function Button(width, height, label) {
	//调用“super”构造函数
	Widget.call(this, width, height);
	this.label = label || "Default";

	this.$elem = $( "<button>" ).text(this.label);
}

//让Button“继承”Widget
Button.prototype = Object.create(Widget.prototype);

//重写render(..)
Button.prototype.render = function($where) {
	// “super”调用
	Widget.prototype.render.call( this, $where );
	this.$elem.click( this.onClick.bind( this ) );
};

Button.prototype.onClick = function(evt) {
	console.log( "Button '" + this.label + "' clicked!" );
};

$( document ).ready( function(){
	var $body = $( document.body );
	var btn1 = new Button( 125, 30, "Hello" );
	var btn2 = new Button( 150, 40, "World" );
	btn1.render( $body );
	btn2.render( $body );
} );

## 6.6 小结 ##

选择是否使用类和继承设计模式。大多数开发者理所当然地认为类是
唯一（合适）的代码组织方式，但是本章中我们看到了另一种更少见但是更强大的设计模式：行为委托。

行为委托认为对象之间是兄弟关系，互相委托，而不是父类和子类的关系。JavaScript 的[[Prototype]] 机制本质上就是行为委托机制。也就是说，我们可以选择在 JavaScript 中努力实现类机制，也可以拥抱更自然的 [[Prototype]] 委托机制。

对象关联（对象之前互相关联）是一种编码风格，它倡导的是直接创建和关联对象，不把它们抽象成类。对象关联可以用基于 [[Prototype]] 的行为委托非常自然地实现。

# 附录A #

类是一种可选（而不是必须）的设计模式，而且在 JavaScript 这样的 [[Prototype]] 语言中实现类是很别扭的。

繁琐杂乱的 .prototype 引用、试图调用原型链上层同名函数时的显式伪多
态以及不可靠、不美观而且容易被误解成“构造函数”的 .constructor 。

## A.1 class ##

	class Widget {
		constructor(width, height) {
			this.width = width || 50;
			this.height = height || 50;
			this.$elem = null;
		}
		render($where) {
			if (this.$elem) {
				this.$elem.css ({
					width: this.width + "px";
					height: this.height + "px";
				}).appendTo($where);
			}
		}
	}
	class Button extends Widget {
		constructor(width, height, label) {
			super(width, height);
			this.label = label || "Default";
			this.$elem = $("<button>").text( this.label);
		}
		render($where) {
			super($where);
			this.$elem.click(this.onClick.bind(this));
		}
		onClick(evt) {
			console.log("Button '" + this.label + "' clicked!");
		}
	}

1. 不再引用杂乱的.prototype了。
2. Button声明时直接“继承”了Widget，不再需要通过Object.create(..)来替换.prototype对象，也不需要._proto_或者Object.setPrototypeOf(..)。
3. 构造函数不属于类，所以无法互相引用——super()可以完美解决构造函数的问题。
4. class字面语法不能声明属性（只能声明方法）。原型链末端的“实例”可能会意外地获取其他地方的属性（这些属性隐式被所有“实例”所“共享”）。所以， class 语法实际上可以帮助你避免犯错。
5. 可以铜鼓oextends扩展对象（子）类型，甚至是内置的对象（子）类型，比如Array或RegExp。没有class ..extends语法时。

## A2 class陷阱 ##
然而，class语法并没有解决所有的问题，在JavaScript中使用“类”设计模式仍然存在许多深层问题。

1. 认为 ES6 的 class 语法是向 JavaScript 中引入了一种新的“类”机制，其实不是这样。 class 基本上只是现有 [[Prototype]] （委托！）机制的一种语法糖。
2.  class 并不会像传统面向类的语言一样在声明时静态复制所有行为。如果你（有意或无意）修改或者替换了父“类”中的一个方法，那子“类”和所有实例都会受到影响，因为它们在定义时并没有进行复制，只是使用基于 [[Prototype]] 的实时委托。

	class C {
		constructor() {
			this.num = Math.random();
		}
		rand() {
			console.log("Random: " + this.num);
		}
	}

	var c1 = new C();
	c1.rand(); // "Random: 0.4324299..."

	C.prototype.rand = function() {
		console.log("Random:" + Math.round(this.num * 1000 ));
	}

	var c2 = new C();
	c2.rand(); // "Random: 867"
	c1.rand(); // "Random: 432"——噢！

## A.3 静态大于动态吗 ##

ES6的class最大的问题在于，（像传统的类一样）它的语法有时会让你认为，定义一个class后，它就变成了一个（未来会被实例化的）东西的静态定义。

在传统面向类的语言中，类定义之后就不会进行修改，所以类的设计模式就不支持修改。但是JavaScript最强大的特性之一就是它的动态性，任何对象的定义都可以修改（除非你把它设置成不可变）。

# A4 小结 #

class 很好地伪装成 JavaScript 中类和继承设计模式的解决方案，但是它实际上起到了反作用：它隐藏了许多问题并且带来了更多更细小但是危险的问题。

class 加深了过去 20 年中对于 JavaScript 中“类”的误解，在某些方面，它产生的问题比解决的多，而且让本来优雅简洁的 [[Prototype]] 机制变得非常别扭。

结论：如果 ES6 的 class 让 [[Prototype]] 变得更加难用而且隐藏了 JavaScript 对象最重要的机制——对象之间的实时委托关联，我们难道不应该认为 class 产生的问题比解决的多吗？难道不应该抵制这种设计模式吗？