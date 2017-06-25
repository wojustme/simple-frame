package com.wojustme.myframe.ioc;

import com.wojustme.myframe.util.ClassUtil;
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
 * wojustme于2017/6/24祈祷...
 */
public final class ClassFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClassFactory.class);

  // 定义一个类集合（用于存放所加载的类）
  private static final Set<Class<?>> CLASS_SET;

  private static final List<String> COMPONENT_SCAN_PACKAGE_LIST;

  private static Map<Class, Set<Class>> beanClassMap;

  static {
    String basePackage = AppConfigHelper.getAppScanBasePackage();
    CLASS_SET = ClassUtil.getClassSet(basePackage);
    COMPONENT_SCAN_PACKAGE_LIST = new ArrayList<>();
    COMPONENT_SCAN_PACKAGE_LIST.add("com.wojustme.myframe");
  }

  /**
   * 获取应用包名下的所有类
   * @return
   */
  public static Set<Class<?>> getClassSet() {
    return CLASS_SET;
  }

  /**
   * 获取应用包名下的所有类被Component注解的注解类
   * @return
   */
  public static Set<Class> getComponentClassSet() {

    Set<Class> classSet = new HashSet<>();

    for (String eachScanPackage : COMPONENT_SCAN_PACKAGE_LIST) {
      for (Class<?> clazz : ClassUtil.getClassSet(eachScanPackage)) {
        if (clazz.isAnnotation()) {
          Annotation[] annotations = clazz.getAnnotations();
          if (annotations.length > 0) {
            for (Annotation ann : annotations) {
              if (ann instanceof BeanComponet) {
                if (!classSet.contains(ann)) {
                  classSet.add(clazz);
                }
              }
            }
          }
        }
      }
    }
    return classSet;

  }



  /**
   * 获取应用包名下所有Bean类
   * @return
   */
  public static Map<Class, Set<Class>> getBeanClassMap() {
    Map<Class, Set<Class>> beanClassSetMap = new HashMap<>();
    Set<Class> beanClassKeySet = getComponentClassSet();

    for (Class<?> clazz : CLASS_SET) {
      if (!clazz.isAnnotation()) {
        Annotation[] annotations = clazz.getAnnotations();
        if (annotations.length > 0) {
          for (Annotation ann : annotations) {
            for (Class cls : beanClassKeySet) {
              if (ann.annotationType().equals(cls)) {
                Set<Class> classes = beanClassSetMap.get(cls);
                if (classes == null) {
                  classes = new HashSet<>();
                }
                classes.add(clazz);
                beanClassSetMap.put(cls, classes);
              }
            }
          }
        }
      }
    }

    beanClassMap = beanClassSetMap;
    return beanClassSetMap;
  }



  public static Set getAnnotatedSetByAnnoClass(Class clazz) {
    if (beanClassMap == null) {
      beanClassMap = getBeanClassMap();
    }
    return beanClassMap.get(clazz);
  }
}
