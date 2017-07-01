package com.wojustme.myframe.aop.proxy;

/**
 * ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_                               //
 * //                         o8888888o                              //
 * //                         88" . "88                              //
 * //                         (| ^_^ |)                              //
 * //                         O\  =  /O                              //
 * //                      ____/`---'\____                           //
 * //                    .'  \\|     |//  `.                         //
 * //                   /  \\|||  :  |||//  \                        //
 * //                  /  _||||| -:- |||||-  \                       //
 * //                  |   | \\\  -  /// |   |                       //
 * //                  | \_|  ''\---/''  |   |                       //
 * //                  \  .-\__  `-`  ___/-. /                       //
 * //                ___`. .'  /--.--\  `. . ___                     //
 * //              ."" '<  `.___\_<|>_/___.'  >'"".                  //
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
 * //      ========`-.____`-.___\_____/___.-`____.-'========         //
 * //                           `=---='                              //
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
 * //             佛祖保佑       永无BUG     永不修改                   //
 * ////////////////////////////////////////////////////////////////////
 * <p>
 * wojustme于2017/7/1祈祷...
 */

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 一个代理链
 * 存在着多个代理情况
 * 一个一个按需进行，顺序不可控
 */
public class ProxyChain {

  // 目标类
  private final Class<?> targetClass;
  // 目标对象
  private final Object targetObject;
  // 目标方法
  private final Method targetMethod;
  // 代理的方法
  private final MethodProxy methodProxy;
  // 方法的参数
  private final Object[] methodParams;
  // 代理链中的列表
  private List<Proxy> proxyList = new ArrayList<>();
  // 代理的序号
  private int proxyIndex = 0;

  public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxyList) {
    this.targetClass = targetClass;
    this.targetObject = targetObject;
    this.targetMethod = targetMethod;
    this.methodProxy = methodProxy;
    this.methodParams = methodParams;
    this.proxyList = proxyList;
  }

  public Class<?> getTargetClass() {
    return targetClass;
  }

  public Method getTargetMethod() {
    return targetMethod;
  }

  public Object[] getMethodParams() {
    return methodParams;
  }


  public Object doProxyChain() throws Throwable {
    Object methodResult;

    if (proxyIndex < proxyList.size()) { // 如果被切面很多个类，递归依次执行
      methodResult = proxyList.get(proxyIndex++).doProxy(this);
    } else { // 如果只有一个切面，则执行
      methodResult = methodProxy.invokeSuper(targetObject, methodParams);
    }

    return methodResult;
  }

}
