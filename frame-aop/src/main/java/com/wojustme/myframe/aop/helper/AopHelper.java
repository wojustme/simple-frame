package com.wojustme.myframe.aop.helper;

import com.wojustme.myframe.aop.annotation.Aspect;
import com.wojustme.myframe.aop.proxy.AspectProxy;
import com.wojustme.myframe.aop.proxy.Proxy;
import com.wojustme.myframe.aop.proxy.ProxyManager;
import com.wojustme.myframe.ioc.BeanFactory;
import com.wojustme.myframe.ioc.ClassFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

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
public final class AopHelper {


  private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);

  static {
    try {
      Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
      Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
      for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
        Class<?> targetClass = targetEntry.getKey();
        List<Proxy> proxyList = targetEntry.getValue();
        Object proxy = ProxyManager.createProxy(targetClass, proxyList);
        BeanFactory.setBean(targetClass, proxy);
      }
    } catch (Exception e) {
      LOGGER.error("com.wojustme.myframe.aop failure", e);
    }
  }

  private static Map<Class<?>, Set<Class<?>>> createProxyMap() throws Exception {

    Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<>();
    Set<Class<?>> proxyClassSet = ClassFactory.getClassSetBySuper(AspectProxy.class);

    for (Class<?> proxyClass : proxyClassSet) {
      if (proxyClass.isAnnotationPresent(Aspect.class)) {
        Aspect aspect = proxyClass.getAnnotation(Aspect.class);
        Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
        proxyMap.put(proxyClass, targetClassSet);
      }
    }

    return proxyMap;
  }

  private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception {
    Set<Class<?>> targetClassSet =new HashSet<>();
    Class<? extends Annotation> annotation = aspect.value();
    if (annotation != null && !annotation.equals(Aspect.class)) {
      Set<Class<?>> annotatedSetByAnnoClass = ClassFactory.getClassSetByAnnotation(annotation);
      targetClassSet.addAll(annotatedSetByAnnoClass);
    }
    return targetClassSet;
  }

  private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
    Map<Class<?>, List<Proxy>> targetMap = new HashMap<>();

    for (Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyMap.entrySet()) {
      Class<?> proxyClass = proxyEntry.getKey();
      Set<Class<?>> targetClassSet = proxyEntry.getValue();
      for (Class<?> targetClass : targetClassSet) {
        Proxy proxy = (Proxy) proxyClass.newInstance();
        if (targetMap.containsKey(targetClass)) {
          targetMap.get(targetClass).add(proxy);
        } else {
          List<Proxy> proxyList = new ArrayList<>();
          proxyList.add(proxy);
          targetMap.put(targetClass, proxyList);
        }
      }
    }

    return targetMap;
  }
}
