package web.controller;

import com.wojustme.myframe.restful.annotation.Action;
import com.wojustme.myframe.restful.annotation.Controller;
import com.wojustme.myframe.restful.bean.JSONData;
import com.wojustme.myframe.restful.bean.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.bean.Person;
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
 * 用于生成JSON的POJO对象
 */
@Controller("/test")
public class TestController {
  private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

  @Action(
      url = "/testJson"
  )
	public JSONData index(Param param) {
    TestController.LOGGER.info("hehe");
		Person person = new Person("xu", 21);
		return new JSONData(person);
	}
  @Action(
      url = "/testJson2"
  )
	public JSONData index1(Param param) {

		Person person = new Person("xuewewe", 21);
		return new JSONData(person);
	}
}
