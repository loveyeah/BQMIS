/**
　* Copyright ustcsoft.com
　* All right reserved.
*/
package power.web.administration.carpasssearch.action;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.business.CarPassSearchFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;


/**
 * 进出车辆查询Action .
 * 
 * @author 赵明建
 */
@SuppressWarnings("serial")
public class CarPassSearchAction extends AbstractAction {
	
	/** 读取位置 */
    private int start = 0;
    /** 读取记录数 */
    private int limit = 0;
    /** 开始日期 */
    private String strStartDate = "";
    /** 截止日期 */
    private String strEndDate = "";
    /** 通行证号 */
    private String strPasscode = "";
    /** 所在单位 */
    private String strFirm = "";
	/**进出车辆查询接口**/
	private CarPassSearchFacadeRemote remote = null;
	
	/**构造函数**/
	public CarPassSearchAction(){
		//远程接口
		remote = (CarPassSearchFacadeRemote)factory.getFacadeRemote("CarPassSearchFacade");
	}
	/**
	 * 进行车辆进出查询
	 * @throws JSONException
	 */
	public void getCarsList()throws JSONException{
		//log开始
		LogUtil.log("Action:进出车辆查询开始", Level.INFO, null);
		//共通类型对象
		PageObject  obj = new PageObject();
		//取得企业编码
		String strEnterpriseCode = employee.getEnterpriseCode();
        obj = remote.getCarPassInfoDetails(strStartDate,strEndDate,strPasscode,strFirm,strEnterpriseCode,start,limit);
        if (null == obj.getList()) {
            List list = new ArrayList();
            obj.setList(list);
            obj.setTotalCount(0l);
        };
        String str ="";
        // 如果查询返回结果为空，则替换为如下返回结果
        if (obj.getTotalCount()<=0) {
            str = "{\"list\":[],\"totalCount\":null}";
        }else{
        	//序列化PageObject类型对象,转化为字符串类型
             str = JSONUtil.serialize(obj);
            	
        }
        //Action log结束
        LogUtil.log("Action:进出车辆查询正常结束", Level.INFO, null);
        
        write(str);
	}

	/**
	 * 
	 * @return strStartDate
	 */
	public String getStrStartDate() {
		return strStartDate;
	}
	/**
	 * 
	 * @param strStartDate 开始时间
	 */
	public void setStrStartDate(String strStartDate) {
		this.strStartDate = strStartDate;
	}
	/**
	 * 
	 * @return  strEndDate
	 */
	public String getStrEndDate() {
		return strEndDate;
	}
	/**
	 * 
	 * @param strEndDate 截止时间
	 */
	public void setStrEndDate(String strEndDate) {
		this.strEndDate = strEndDate;
	}
	/**
	 * 
	 * @return strPasscode
	 */
	public String getStrPasscode() {
		return strPasscode;
	}
	/**
	 * 
	 * @param strPasscode 通行证号
	 */
	public void setStrPasscode(String strPasscode) {
		this.strPasscode = strPasscode;
	}
	/**
	 * 
	 * @return strFirm
	 */
	public String getStrFirm() {
		return strFirm;
	}
	/**
	 * 
	 * @param strFirm 所在单位
	 */
	public void setStrFirm(String strFirm) {
		this.strFirm = strFirm;
	}
	/**
	 * 
	 * @return intStart 
	 */
	public int getStart() {
		return start;
	}
	/**
	 * 
	 * @param intStart 开始行
	 */
	public void setStart(int start) {
		this.start = start;
	}
	/**
	 * 
	 * @return intLimit
	 */
	public int getLimit() {
		return limit;
	}
	/**
	 * 
	 * @param intLimit  限制行数
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

}
