package com.wojustme.myframe.aop.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

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
public class AspectProxy implements Proxy {

  private static final Logger LOGGER = LoggerFactory.getLogger(AspectProxy.class);


  @Override
  public Object doProxy(ProxyChain proxyChain) throws Throwable {
    Object result = null;

    Class<?> cls = proxyChain.getTargetClass();
    Method method = proxyChain.getTargetMethod();
    Object[] params = proxyChain.getMethodParams();


    // 开始定义一些钩子函数
    // 在子类中选择性的覆盖
    begin();
    try {
      if (intercept(cls, method, params)) {
        before(cls, method, params);
        result = proxyChain.doProxyChain();
        after(cls, method, params);
      } else {
        result = proxyChain.doProxyChain();
      }
    } catch (Exception e) {
      LOGGER.error("proxy failure", e);
      error(cls, method, params, e);
      throw e;
    } finally {
      end();
    }
    return result;

  }


  // 用于觉得该代理是否需要执行
  // 通过intercept来控制是否代理
  protected boolean intercept(Class<?> cls, Method method, Object[] params) throws Throwable {
    return true;
  }
  public void begin() {

  }
  public void before(Class<?> cls, Method method, Object[] params) {

  }
  public void after(Class<?> cls, Method method, Object[] params) {

  }
  public void error(Class<?> cls, Method method, Object[] params, Throwable e) {

  }
  public void end() {

  }

}
