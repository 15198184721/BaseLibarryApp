package com.baselibarryapp.calculater;

import android.content.ComponentName;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.baselibrary.logutil.Lg;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-6-5
 * Description
 * </pre>
 */
@RunWith(AndroidJUnit4.class) //让其允许在android允许环境
public class DyTestTest {

    DyTest dyTest = null;
    Context appContext = null;

    /**
     * Before：初始化方法 对于每一个测试方法都要执行一次（注意与BeforeClass区别，后者是对于所有方法执行一次）
     * After：释放资源 对于每一个测试方法都要执行一次（注意与AfterClass区别，后者是对于所有方法执行一次）
     * Test：测试方法，在这里可以测试期望异常和超时时间
     * Test(expected=ArithmeticException.class)检查被测方法是否抛出ArithmeticException异常
     * Ignore：忽略的测试方法
     * BeforeClass：针对所有测试，只执行一次，且必须为static void
     * AfterClass：针对所有测试，只执行一次，且必须为static void
     * @throws Exception
     */
    @Before
    public void before() throws Exception{
        dyTest = new DyTest();
        appContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void testAdd() throws Exception{
        // 验证1 2 经过add方法是否 ==3，如果不正确，测试不通过
        Assert.assertEquals(dyTest.add(1, 2), 3);
    }

    @Test
    public void testAdd2() {
        // 验证1 2 经过add方法是否 == 3，如果不正确，测试不通过
        Assert.assertEquals(dyTest.add(1, 2), 5);
    }

    @Test
    public void testRequestHttp() {
        dyTest.requestHttp(suc->{
            Lg.e("返回结果(来自单元测试):"+suc);
        });
        //让当前线程等待，否则还没返回数据就退出了
        //注意:需要手动停止。否则不会停止
        CountDownLatch countdown = new CountDownLatch(1);
        try {
            countdown.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStartPlugins() {
        ComponentName componentName = dyTest.startPlugins(appContext);
        Assert.assertEquals(componentName.getClassName(),
                "plugin1.com.plugin1.WebViewService");
    }
}
