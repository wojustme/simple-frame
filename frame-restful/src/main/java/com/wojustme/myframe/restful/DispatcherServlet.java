package com.wojustme.myframe.restful;

import com.wojustme.myframe.ioc.BeanFactory;
import com.wojustme.myframe.restful.bean.Handler;
import com.wojustme.myframe.restful.bean.Param;
import com.wojustme.myframe.restful.helper.ControllerHelper;
import com.wojustme.myframe.restful.helper.HelperLoader;
import com.wojustme.myframe.restful.bean.JSONData;
import com.wojustme.myframe.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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

@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

	/**
	 * 用于初始化整个web框架
	 * @param servletConfig
	 * @throws ServletException
	 */
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		HelperLoader.init();
	}

	/**
	 * 拦截每个请求
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 获得请求的方法和路径
		String requestMethod = req.getMethod().toLowerCase();
		String requestPath = req.getPathInfo();
		// 根据请求的方法和路径，从控制器容器获得处理方法
		Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
		if (handler != null) {
			// 非空，获得该Controller类
			Class<?> controllerClass = handler.getControllerClass();
			// 获得对应的Bean实例
//			Object controllerBean = BeanHelper.getBean(controllerClass);
			Object controllerBean = BeanFactory.getBean(controllerClass);

			// 封装请求，生成KV对，eg：GET:http://...?username=*&password=*
			Map<String, Object> paramMap = new HashMap<>();
			// 得到请求的一些参数
			Enumeration<String> paramNames = req.getParameterNames();
			while (paramNames.hasMoreElements()) {
				String paramName = paramNames.nextElement();
				String paramValue = req.getParameter(paramName);
				paramMap.put(paramName, paramValue);
			}
			String body = CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
			if (StringUtil.isNotEmpty(body)) {
				String[] params = StringUtil.splitString(body, "&");
				if (ArrayUtil.isNotEmpty(params)) {
					for (String param : params) {
						String[] array = StringUtil.splitString(param, "=");
						if (ArrayUtil.isNotEmpty(array) && array.length ==2) {
							String paramName = array[0];
							String paramValue = array[1];
							paramMap.put(paramName, paramValue);
						}
					}
				}
			}
			// 封装请求来的参数
			Param param = new Param(paramMap);
			// 获得action处理方法
			Method actionMethod = handler.getActionMethod();
			// 进行方法反转处理，生成运行结果result
			Object result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
			if (result instanceof JSONData) {
				// 如果是JSONData类型，返回JSON字符串
				JSONData jd = (JSONData) result;
				Object model = jd.getModel();
				if (model != null) {
					resp.setContentType("application/json");
					resp.setCharacterEncoding("UTF-8");
					PrintWriter writer = resp.getWriter();
					String json = JsonUtil.toJson(model);
					writer.write(json);
					writer.flush();
					writer.close();
				}
			}
		}
	}
}
