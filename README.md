# KTea
Ktea是kotlin开发的Android库, 它可以让Android开发更简单更快速更容易维护. 它很容易入门和使用, 方便构建高质量的应用, 减少崩溃和内存泄露.

## 引入
- 在项目module下的build.gradle中添加
```
dependencies {
    //... 其他库的引用
    implementation 'com.mengwei:ktea:1.1.0'
}

```
- 在项目的Application的类中初始化KTea参数

```
class MyApp : Application() {
    private val activitys: LinkedList<Activity> = LinkedList()

    override fun onCreate() {
        super.onCreate()

        // 初始化KTea的主要配置参数
        Settings.init {
            appCtx = this@MyApp //设置应用Module的ApplicationContext
            baseUrl = "http://..." //网络请求时的baseUrl
            activityStack = activitys //传入一个activity任务栈
            isDEBUG = BuildConfig.DEBUG //当是debug模式时,会打印日志;release时是不打印的
            jsonObjectStyle = ... //这行根据需求定义, 可不写, 具体使用方法看下面的教程
        }
    }
}
```
## 入门
KTea的首要目标是提供一种更简洁更高效更安全的Android开发方式, 让我们从一个小例子开始, 来看看KTea的使用:

```
http {
    url = "getRequest/demo"
    onSuccess = {
        //这里是请求成功请求到的数据
        logger(it)
    }
    onError = {
        //这里是请求失败返回的错误信息
        logger(it)
    }
}
```
网络请求是Android开发中常见的任务, 在Ktea中可以通过**http函数**来完成这个任务, 上面的代码就是一个网络请求的例子.
- url参数类型是String,它的值可以是完整的网址路径, 也可以是配置了baseUrl后相对的路径.
- **onSuccess**参数类型是一个lambda表达式, 在这里你可以接收到请求成功后的数据, it是返回的实际数据,  同时在这里已经切换到了**UI线程**, 你不需要处理线程的切换.
- onError参数类型也是一个lambda表达式, 在这里返回请求失败的信息, 当然这里也切换到了UI线程.

你喜欢这样的代码么? Ktea处理网络请求这样的常见任务不但可以节省大量的样板代码, 而且**已经做好了线程的切换**, 你不需要再去关心.

#### 如何把请求数据解析为Entity对象
如果返回的是**json**格式的数据, 你可以使用AndroidStudio的GsonFormat插件先自动生成一个Entity类, 然后在**onSuccess**中把String数据直接转换成对应的Entity对象:

```
// 解析形如这样的jsonobject格式: {key1:value1, key2:value2, ...}

// 对应json的entity
class xxxEntity {
    private String key1;
    private String key2;
    ...get/set 方法
}

onSuccess = {
        val entity = it.toEntity<xxEntity>()
    }
```
同样, 如果是**JsonArray**的数据, 你可以直接转换成Entity的List

```
// 解析形如这样的jsonarray数据 : [{...},{...},...]
onSuccess = {
        val entityList = it.toEntityList<xxEntity>()
    }
```
> toEntity和toEntityList是ktea的内置函数, 可以对String对象直接使用, 不过必须保证该String对象必须是xxEntity对应jsonobject或者jsonarray格式.


下面让我们来看一个开发中更常见的例子, 来展示Ktea中网络请求组件的使用:
- 请求方式是Post
- 需要给服务器提交参数
- 参数中的密码要用MD5加密
- 想要返回的数据直接就是entity对象

想让数据接收到的直接是entity对象, 我们可以使用另外一个函数**httpEntity**

```
fun login(mobile: String, password: String) {
    // 网络请求代码
    httpEntity<LoginEntity> {
        url = "user/login"
        method = Method.POST
        params = mutableMapOf("mobile" to mobile, "password" to password.MD5())
        onSuccess = {
            it //这里的it就是LoginEntity对象
        }
        onError = { logger(it) }
    }

}
```
我们看到这里有两个新的参数名称: **method** 和 **params** ,分别代表请求的方式和请求参数.
- **method参数默认是get方式**, 所以get请求时可写可不写, **但是当post请求时就必须要写了**.
- **params**是一个**Map**对象, 保存网络请求的参数, **可以使用mutableMapOf函数构建一个Map对象并且初始化赋值**, 这样只需要一行代码就可以同时完成创建对象和赋值的操作.

**httpEntity**函数和http函数使用时的区别是需要一个泛型类型, 用于解析json数据对应的Entity类型, 这样在onSuccess下就可以直接接收到**entity类型的对象**.

**string.MD5**是ktea的内置函数, 可以把string类型进行MD5加密, 返回加密后的字符串, 相似的内置函数还有string.AESEncrypt, string.Base64等可以使用.

在开发中, 你可以灵活运用ktea中的多个网络请求函数来满足需要, 除了上面介绍的**http**和**httpEntity**, 还有和**httpEntity**类似的**httpEntityList** ,它可以把JsonArray字符串数据直接解析成Entity的List, 使用方法和httpEntity一样.

```
httpEntityList<xxEntity> {
    ...
    onSuccess = {
            it //这里的it就是一个xxEntity的List集合
        }
}
```
#### 如何处理嵌套的json数据
为了满足网络请求中更复杂的任务, 下面来介绍另外一个函数**httpBase**, 它用来把嵌套Json数据解析成Entity实体类.

```
{
  "status": 1,
  "msg": "success",
  "datas": {...}
}
```
形如上面的json格式, 也是开发中常见的服务器返回json串, 它的外层是状态码和提示信息, 实际需要的数据在"datas"的内层, 下面我们来解决这个问题.
- 给ktea配置时设置一个jsonObjectStyle对象来声明json的格式

```
Settings.init {
    ...
    jsonObjectStyle= JsonStyle().apply {
        statusName = "status"
        dataName = "datas"
        messageName = "msg"
        successStatusValue = "1"
    }
}
```
根据实际服务器请求数据的格式, 把外层的json键名称写上之后, 就可以使用**httpBase**函数了.

```
httpBase<xxxEntity> {
    ...
    onSuccess = {
       it // 这里就是json内层的datas数据解析出来entity对象
    }
}
```
**httpBase函数和httpEntity函数使用上没有区别**, 只不过需要**设置一个JsonStyle对象来指定外层的键名**, 这样就可以**把内层的json数据而不是整个json数据解析成entity对象**了.

**jsonObjectStyle只需要在ktea初始化时设置一次**即可, 之后使用httpBase函数时都不需要设置.

同样, 和httpBase函数对应的还有一个**httpBaseList**函数, 用来**把内层的jsonarray直接解析成entity的List**.

```
httpBaseList<Entity> { ...  }
```
#### 如何处理请求头约定

移动端网络请求的复杂性在于服务器端请求约定没有固定的标准, 我们没法控制. 每个服务器端的代码都是不同的开发人员编写, 所以每个公司几乎都不相同. 在ktea中还涵盖了一些其他常见问题的解决方法.
- 诸如adviceID/时间戳等的**参数在请求头中提交**. **除token外**的其他需要在请求头中提交的参数都可以在**HttpHead.params**赋值.
**HttpHead.params是一个Map对象**, 可以通过key/value的形式赋值提交参数, 这样请求的时候就会把请求参数添加到请求头中.
```
HttpHead.params["ADVICEID"] = "..."
HttpHead.params["TIMESTAMPS"] = "..."
```
- 有的请求需要在请求头中添加token信息, token和上面一类参数的不同在于token需要**保存在本地**以方便app关闭再打开时还能获取到token. 如果需要在请求头中添加token数据, 可以在获取token后这样写:

```
// 在这里给token赋值
Token.token= "TOKEN" to "..."
```
上面的代码会在token赋值后把token保存在本地, 当app关闭再打开时同样不会丢失, 但是**重启app后需要判断一下token是否存在, 每次重启只需要一次判断即可, 不用每次请求时都调用这个方法.** 所以**最佳的做法是在启动页或者个人中心页面(具体看需求)调用一次**:

```
class SplashActivity : BaseActivity() {
    ...

    if (Token.token == null) {
            gotoLoginActivity()
        } else {
            gotoMainActivity()
    }
}
```
修改请求头参数的**HttpHead.params和Token.token都是单例的全局变量**, 只需要赋值一次即可, 你可以根据需求在合适的时机进行赋值和修改, 而不用每次网络请求都进行赋值. 但是当这些数据变化时, 比如时间戳, 这样就需要在使用网络请求函数前进行赋值.

## 进阶
还有更多的网络请求功能并不能一一讲解,需要在使用中去慢慢发现和体会. **下面我们来介绍一些更重要的内容.**

在实际开发中, 如果把网络请求的代码直接放在activity中不但违背了类的单一性原则而让维护代码非常困难, 更重要的是还会产生一个很严重的问题: **内存泄露**. 为了避免内存泄露, 就需要额外的代码来处理, 而且因为很多人对内存泄漏的不了解, 当应用表现偶尔崩溃时往往无法定位原因.

#### 如何构建稳定, 易维护的高质量应用
构建易维护应用的关键在于减少代码之间的耦合, 遵守类的**职能单一性原则**. 为此,  移动端的架构设计引入了流行的MVP和MVVM的模式.

MVP的缺点很明显, 它需要额外创建契约接口类和其他分层的类并声明功能重复的方法在各层来回调用, 当页面本来只需要添加一个很小的功能时却要同时修改很多的类, 需要大量的烦人的代码, 大大加重开发负担.

我不喜欢MVVM模式的原因在于databinding需要在xml文件中写功能代码, 我还是希望xml能维持单一的布局功能, 这样出现问题时就可以在尽量小的范围进行排查.

##### 那么, Ktea如何实现让开发简单方便, 又易于维护呢?
- **BaseViewModel**是ktea中viewmodel层的基类, viewmodel层的职责是更新数据并在数据变化时进行通知, 它并不持有activity对象, 它持有的是当前activity生命周期变化的对象.
- **LiveData**在数据变化时会通知订阅它的对象, 它是连接viewmodel和activity的桥梁.

下面我们来看如何使用它们:

```
class NewsListModel:BaseViewModel() {
    val errorLiveData by lazy { MutableLiveData<String>() }
    val newsLiveData by lazy { MutableLiveData<List<NewsEntity>>() }

    fun getNews(pageNum: Int) {

        jobs + httpBaseList<NewsEntity> {
            url = "article/listNews"
            params = mutableMapOf("pageNum" to pageNum, "pageSize" to 20)
            onSuccess = { newsLiveData.value = it }
            onError = { errorLiveData.value = it }
        }

    }
}
```
**ViewModel层总是继承BaseViewModel并由一系列的请求数据的函数和LiveData组成.** 常用的LiveData是MutableLiveData, 泛型指定数据源对象的类型, **通过给LiveData.value赋值来通知数据的变化**.  在ViewModel中我们只需要获取数据并通知给LiveData即可, 不需要和任何的类和模块交互.

我们在ViewModel中并没有持有activity的引用, 那么我们如何去更新UI呢? 所以我们必须在activity订阅LiveData, 这样当数据变化时, 我们就会得到通知.

```
class NewsListActivity : BaseActivity() {

    private val model by lazy { getViewModel<NewsListModel>() }
    private val adapter by lazy { NewsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_list)
    }

    override fun initData() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        // 订阅livedate的数据变化通知
        model.errorLiveData.observe(this) {
            dismissLoading()
            errorToast(it)
        }
        model.newsLiveData.observe(this) {
            adapter.update(it)
            dismissLoading()
        }
        //获取数据
        model.getNews(index)
        showLoading()
    }
}
```
Ktea中定义了BaseViewModel和BaseActivity类, 它们加入了很多简化开发和提高性能的代码, 在开发中继承它们会帮助你省了很多麻烦.

要在activity中得到viewmodel对象必须通过函数**getViewModel**获取而不能直接创建, 因为viewmodel需要获取当前activity的生命周期对象.

上面的代码还演示了通过livedata的**observe方法**订阅livedata的数据变化来更新UI的方法.

## 最后
上面只是介绍了KTea库里很少的一部分功能, 以后会陆续提供更多的Ktea教程来针对性的深入介绍每个模块的使用技巧.