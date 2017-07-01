package com.wojustme.myframe;

import com.wojustme.myframe.ioc.BeanComponet;
import com.wojustme.myframe.ioc.ClassFactory;
import org.junit.Test;
import web.MyServerHandler;

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
public class LoadClsTest {

  @Test
  public void testOutput() {

    System.out.println(ClassFactory.getClassSetByAnnotation(MyServer.class));

  }

  @Test
  public void testAnn() {
    System.out.println(MyServerHandler.class.isAnnotationPresent(MyServer.class));
  }

  @Test
  public void testAnn1() {
    System.out.println(MyServer.class.isAnnotationPresent(BeanComponet.class));
  }
}
