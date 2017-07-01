package com.wojustme.myframe.restful.helper;

import com.wojustme.myframe.ioc.ClassFactory;
import com.wojustme.myframe.restful.annotation.Action;
import com.wojustme.myframe.restful.annotation.Controller;
import com.wojustme.myframe.restful.bean.Handler;
import com.wojustme.myframe.restful.bean.Request;
import com.wojustme.myframe.restful.protocol.ReqMethod;
import com.wojustme.myframe.util.ArrayUtil;
import com.wojustme.myframe.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
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
 * wojustme于2017/6/23祈祷...
 */
/**
 * Controller层类的帮助类
 */
public final class ControllerHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(ControllerHelper.class);
	// 每一个action请求对应一个hanlder处理器，Action容器
	private static final Map<Request, Handler> ACTION_MAP = new HashMap<>();

	static {
		ControllerHelper.handlerController();
	}

	/**
	 * 处理Controller类的方法
	 */
	private static void handlerController() {
		// 获取所有的Controller类
		// 从类工厂中获得到带有Controller注解的类
		Set<Class<?>> controllerClassSet = ClassFactory.getClassSetByAnnotation(Controller.class);
		if (CollectionUtil.isNotEmpty(controllerClassSet)) {
			// 非空，遍历Controller类
			for (Class<?> constrollerClass : controllerClassSet) {
				Controller controller = constrollerClass.getAnnotation(Controller.class);
				String basePath = controller.value();
				Method[] methods = constrollerClass.getDeclaredMethods();
				if (ArrayUtil.isNotEmpty(methods)) {
					// 非空，遍历每个Controller类的方法
					for (Method method : methods) {
						// 判断是否包含Action注解
						if (method.isAnnotationPresent(Action.class)) {
							Action action = method.getAnnotation(Action.class);
							String subPath = action.url();
							ReqMethod reqMethod = action.method();
							// 组合动作枚举和完成的URL路径
							String reqPath = basePath + subPath;
              Request request = new Request(reqMethod, reqPath);
              Handler handler = new Handler(constrollerClass, method);
              // 放入Action容器
              ACTION_MAP.put(request, handler);
						}
					}
				}
			}
		}
	}


	/**
	 * 从Action容器中获取处理器Handler
	 * @param requestMethod
	 * @param requestPath
	 * @return
	 */
  public static Handler getHandler(String requestMethod, String requestPath) {
    return getHandler(ReqMethod.getReqMethod(requestMethod), requestPath);
  }
  public static Handler getHandler(ReqMethod requestMethod, String requestPath) {
    Request request = new Request(requestMethod, requestPath);
    return ACTION_MAP.get(request);
  }
}
