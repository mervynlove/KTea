# KTea
Ktea是kotlin开发的Android库, 它可以让Android开发更简单更快速更容易维护. 它很容易入门和使用, 方便架构高质量的应用, 减少崩溃和内存泄露.
##### [KTea最佳实践教程(正在整理中, 将陆续开放)](https://github.com/mervynlove/KTea)

## 在Gradle引入Ktea
- 在项目module下的build.gradle中添加
```
repositories {
    maven { url 'https://jitpack.io' }
}

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
        }
    }
}
```
## KTea的组件
KTea组件是为了协同工作而构建的, 不过也可以单独使用. 当协同使用时能更好的编写一致性的代码, 更便于维护和开发.
#### 1.网络请求组件
KTea的网络请求组件使用kotlin的协程编写, 并且使用kotlin的高级lambda表达式进行封装, 比线程更加节省资源, 更加方便管理, 使用时的代码更加简洁优雅, 和架构组件协同使用时保证了应用不会有内存泄露.
- 更简单的优雅的网络请求代码, 如下:

```
http {
    url = "getRequest/demo"
    onSuccess={
        //这里是成功请求到的数据
        logger(it)
    }
    onError= {
        //这里是失败返回的错误信息
        logger(it)
    }
}
```
是不是如此的清奇? 当然, 因为是get请求, 所以可以不写请求方式(get请求是默认的), 因为没有请求参数, 也就不需要配置. 那我们现在实现一些更复杂的请求:
- 直接把请求数据解析成entity对象

```
httpEntity<LoginEntity> {
        url = "user/login"
        method = Method.POST
        params = mutableMapOf("id" to id, "password" to password)
        onSuccess = {
            //这里的it就是请求到的实体类
            it
        }
        onError = {
            //这里是失败返回的错误信息
            logger(it)
        }
    }
```
##### 总结
- 请求方式分为: GET/POST/FILE 分别对应get/post/上传文件 的网络请求.
- 请求函数包括:

```
http {} //原始格式

httpEntity<T>{} //Entity对象

httpEntityList<T>{} //List对象

httpBase<T>{} //嵌套的json中的数据Entity对象

httpBaseList<T>{} //嵌套的Json中的数据List对象
```
> 更详细的教程请查看<<KTea最佳实践教程>>
#### 2.架构组件
通过架构组件, 可以编写更健壮, 职责更清晰, 更一致性的代码; 架构组件虽然使用也很简单, 但需要理解它的功能和特点, 而不能死记.
- BaseActivity : View层
- BaseViewModel : ViewModel层
###### view层可以和viewmodel层通信, 通过调用viewmodel层的方法来请求数据, 并且监听viewmodel层的数据源的通知来获取数据更新UI ; viewmodel层不能主动和view层通信, 也不能调用view层的方法, 而是通过感知view层的生命周期, 在数据发生变化时通知给监听的view层对象.
##### 简单实例:

```
// viewmodel层

class NewsListModel:BaseViewModel() {
    val errorLiveData by lazy { MutableLiveData<String>() }
    val newsLiveData by lazy { MutableLiveData<List<HomeNewsEntity>>() }

    fun getNews(pageNum: Int) {
        httpBaseList<HomeNewsEntity> {
            url = "article/listNews"
            params = mutableMapOf("pageNum" to pageNum, "pageSize" to 20)
            onSuccess = { newsLiveData.value = it }
            onError = { errorLiveData.value = it }
        }
    }
}


// view层
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
        model.errorLiveData.observe(this) {
            dismissLoading()
            errorToast(it)
        }
        model.newsLiveData.observe(this) {
            adapter.update(it)
            dismissLoading()
        }
        model.getNews(index)
        showLoading()
    }
}
```
上面的示例去掉了下拉刷新和加载等的代码, 演示了架构组件的简单使用方法:
1. view层通过监听viewmodel层的数据变化更新UI
2. viewmodel层通过感知组件LiveData把数据传递给订阅的对象
3. view层调用viewmodel的方法请求数据.
##### 总结
通过架构组件可以编写职责单一的类, 更易于维护的代码, 更加灵活. viewmodel还可以感知view层的生命周期, 使用简单, 不需要额外的代码就可以避免view销毁时内存泄露.