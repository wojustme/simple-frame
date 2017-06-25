package com.wojustme.myframe.ioc;

import com.wojustme.myframe.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
 * wojustme于2017/6/24祈祷...
 */
public class BeanFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(BeanFactory.class);

  // 定义Bean映射（用于存放Bean类与Bean实例的映射关系）
  private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<>();

  static {
    Map<Class, Set<Class>> beanClassMap = ClassFactory.getBeanClassMap();
    Collection<Set<Class>> values = beanClassMap.values();
    for (Set<Class> clsSet : values) {
      for (Class beanClass : clsSet) {
        if (!BEAN_MAP.containsKey(beanClass)) {
          Object obj = ReflectionUtil.newInstance(beanClass);
          BEAN_MAP.put(beanClass, obj);
        }
      }
    }
  }

  /**
   * 获取Bean映射
   * @return
   */
  public static Map<Class<?>, Object> getBeanMap() {
    return BEAN_MAP;
  }

  /**
   * 获取关于clazz对应的Bean实例
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T getBean(Class<T> clazz) {
    if (!BEAN_MAP.containsKey(clazz)) {
      String errorMsg = "can not get bean by class: " + clazz;
      LOGGER.error(errorMsg);
      throw new RuntimeException(errorMsg);
    }
    return (T) BEAN_MAP.get(clazz);
  }

}
