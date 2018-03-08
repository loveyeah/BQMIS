/**
　* Copyright ustcsoft.com
　* All right reserved.
*/
package power.web.administration.dutymansearch.action;

import java.sql.SQLException;
import java.util.logging.Level;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.business.DutyManSearchFacadeRemote;
import power.ejb.administration.comm.ADCommonFacadeRemote;
import power.ejb.administration.comm.ComAdCRight;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * RegularWorkSearchAction action 查询值班人员.
 * 
 * @author 赵明建
 */
@SuppressWarnings("serial")
public class DutyManSearchAction extends AbstractAction {
	// 远程EJB接口
	DutyManSearchFacadeRemote remote = null;
	//通用EJB接口
	ADCommonFacadeRemote remoteComm = null;
	 
	/** 起始时间*/
	private String strStartDate = "";
	/**截止时间*/
	private String strEndDate = "";
	/**子工作类型编码*/
	private String  strSubWorkTypeCode = "";
	/**值类型编码*/
	private String strDutyTypeCode = "";
	/**起始查询行*/
	private int start = 0;
	/**查询限制行*/
	private int limit = 0;
	
	/**无参构造函数*/
	public DutyManSearchAction(){
	   //调用EJB远程接口获取DutyManSearchFacade实例
		remote = (DutyManSearchFacadeRemote)factory.getFacadeRemote("DutyManSearchFacade");
		//调用EJB远程接口获取ADCommonFacade实例
		remoteComm = (ADCommonFacadeRemote)factory.getFacadeRemote("ADCommonFacade");
		
    }
	/*
     * 取得值班人员
     * @throws JSONException
     */
	public void getOnDutyManInfo()throws JSONException{
		//Action log start
		LogUtil.log("Action:  DutyManSearchAction  开始", Level.INFO, null);
		try{
		//取得用户工作编码
		String strUserID = employee.getWorkerCode();
		//取得用户企业编码
		String strEnterPriseCode = employee.getEnterpriseCode();
		//根据用户工作编码和企业编码取得用户权限
		PageObject pobjWorkTypeCode = remoteComm.getUserRight(strUserID,strEnterPriseCode);
		//取得工作类别编码
		String strWorkCode =((ComAdCRight) (pobjWorkTypeCode.getList().get(0))).getWorktypeCode();
	   
     	 //调用远程方法获取PageObject实例
		PageObject result=(PageObject)remote.getOnDutyManInfo(strStartDate, strEndDate, strWorkCode,
				            strSubWorkTypeCode, strDutyTypeCode,strEnterPriseCode,start,limit);
		 //转换为字符串形式
		 String strPageObject =null;
	     if (result.getTotalCount() <= 0) {
	      	strPageObject = "{\"list\":[],\"totalCount\":null}";
	     }else{
	    	 strPageObject  = JSONUtil.serialize(result);
	     }
	     LogUtil.log("Action:  DutyManSearchAction   结束", Level.INFO, null);
	     write(strPageObject);
		}catch(RuntimeException e){
			LogUtil.log("Action:  DutyManSearchAction 失败", Level.SEVERE,e);
		}catch(SQLException e){
			LogUtil.log("SQL 异常抛出", Level.SEVERE,e);
		}
    }


	/**
	 * 
	 * @return strStartDate 起始时间 
	 */
	public String getStrStartDate() {
		return strStartDate;
	}
	/**
	 * 
	 * @param strStartDate 起始时间 
	 */
	public void setStrStartDate(String strStartDate) {
		this.strStartDate = strStartDate;
	}
	/**
	 * 
	 * @return strEndDate 截止时间
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
	 * @return strSubWorkTypeCode 子工作类型编码
	 */
	public String getStrSubWorkTypeCode() {
		return strSubWorkTypeCode;
	}
	/**
	 * 
	 * @param strSubWorkTypeCode 子工作类型编码
	 */
	public void setStrSubWorkTypeCode(String strSubWorkTypeCode) {
		this.strSubWorkTypeCode = strSubWorkTypeCode;
	}
	/**
	 *  
	 * @return strDutyTypeCode 值类型编码
	 */
	public String getStrDutyTypeCode() {
		return strDutyTypeCode;
	}
	/**
	 * 
	 * @param strDutyTypeCode 值类型编码
	 */
	public void setStrDutyTypeCode(String strDutyTypeCode) {
		this.strDutyTypeCode = strDutyTypeCode;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}

}
