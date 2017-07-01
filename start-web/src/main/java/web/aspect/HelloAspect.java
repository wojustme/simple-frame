package web.aspect;

import com.wojustme.myframe.aop.annotation.Aspect;
import com.wojustme.myframe.aop.proxy.AspectProxy;
import com.wojustme.myframe.restful.annotation.Controller;
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
@Aspect(Hello.class)
public class HelloAspect extends AspectProxy {

  private static final Logger LOGGER = LoggerFactory.getLogger(HelloAspect.class);

  private long begin;

  @Override
  public void before(Class<?> cls, Method method, Object[] params) {
    LOGGER.debug("-------hello begin-------");
    begin = System.currentTimeMillis();
  }

  @Override
  public void after(Class<?> cls, Method method, Object[] params) {
    LOGGER.debug(String.format("time: %dms", System.currentTimeMillis() - begin));
    LOGGER.debug("-------hello after-------");
  }

  @Override
  public boolean intercept(Class<?> cls, Method method, Object[] params) {
    return true;
  }
}
