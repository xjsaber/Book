# ch11 CompletableFuture 组合式异步编程 #

## 11.1 Future接口 ##

[code 11-1]()

如果该长时间运行的操作永远不返回会怎样？为了处理这种可能性，虽然Future提供了一个无需任何参数的get方法，推荐大家使用重载版本的get方法，它接受一个超时的参数，通过它，那你可以定义你的线程等待Future结果的最长时间，而不是不休止的等待下去。

### 11.1.1 Future接口的局限性 ###

### 11.1.2 使用CompletableFuture构建异步应用 ###

## 11.2 实现异步API ##

### 11.2.1 将同步方法转换位异步方法 ###


### 12.1.3 机器的日期和时间格式 ###

### 12.1.4 定义Duration和Period ###

## 12.2 操纵、解析和格式化日期 ##

withAttribute方法会创建对象的一个副本，并按照需要修改它的属性。

### 12.2.1 使用TemporalAdjuster ###

### 12.2.2 打印输出及解析日期-时间对象 ###

## 12.3 处理不同的时区和历法 ##

java.time.ZoneId <- java.util.TimeZone

### 12.3.1 利用和UTC/格林尼治时间的固定偏差计算时区 ###

### 12.3.2 使用别的日历系统 ###

