# TimePayment
自定义gradle插件，实现编译时在被注解的方法内部注入代码，实现计算方法耗时时间。

rootProject下：
```groovy
buildscript {

repositories {
        google()
        jcenter()
        maven {
            url uri("\\timepayment\\repo") //mven指定本地路径，因为我没有上传到远程库中
        }
    }
    dependencies {
        classpath "com.rhythm7:timepayment:beta-0.1.5"
    }
}

```

app module 下(目前插件的使用位置被限定在了app module中)：
```groovy
apply plugin: 'timepayment'

dependencies {
    implementation project(':timepayment_core')
}
```

示例代码：
```java
public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foo1();
            }
        });
    }


    @TimePayment
    public void foo1() {
        int i = 0;
        while (i < 1000) {
            i++;
        }
    }
}
```
运行结果:
```
I/System.out:             >>>========================================================>>>
                                          方法foo1(...):
                                              >>>耗时1000 毫秒
                          >>>========================================================>>>

```

