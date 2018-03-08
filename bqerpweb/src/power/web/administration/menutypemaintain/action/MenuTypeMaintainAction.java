/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.menutypemaintain.action;

import java.sql.SQLException;
import java.util.logging.Level;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdCMenuType;
import power.ejb.administration.AdCMenuTypeFacadeRemote;
import power.ejb.administration.comm.CodeCommonFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 菜谱类型维护
 * @author zhaomingjian
 *
 */
public class MenuTypeMaintainAction extends AbstractAction {
    
	/**
	 *  serialVersionUID 序列化
	 */
	private static final long serialVersionUID = 1L; 
	/**
	 * 起始查询行 
	 */
	private int start = 0;
	/**
	 * 限制查询行数
	 */
	private int limit = 0;
	/**
	 * 类型名称
	 */
	private String  strAddTypeName="";
	/**
	 * 检索码
	 */
	private String  strAddRetrieveCode="";
	/**
	 * 修改类型名
	 */
    private String strUpdateTypeName = "";
    /**
     * 修改类型编码
     */
    private String strUpdateTypeCode ="";
    /**
     * 修改检索码
     */
	private String strUpdateRetrieveCode = "";
    /**
	 * 菜谱类型维护操作接口
	 */
	protected  AdCMenuTypeFacadeRemote remote = null ;
	/**
	 * 菜谱编码生成操作接口
	 */
	protected CodeCommonFacadeRemote cremote = null;
	/**
	 *  无参构造函数
	 */
	public MenuTypeMaintainAction(){
		//取得EJB端AdCMenuTypeFacade实例
		remote = (AdCMenuTypeFacadeRemote)factory.getFacadeRemote("AdCMenuTypeFacade");
		//取得EJB端CodeCommonFacade实例
	   cremote = (CodeCommonFacadeRemote)factory.getFacadeRemote("CodeCommonFacade");
	}
	
	/**
	 *  取得菜谱类型表信息
	 *  @throws JSONException  
	 */
	public void getMenuTypeListInfo() throws JSONException{
		
		try{
			LogUtil.log("Action:取得菜谱类型开始", Level.INFO,null);
			//取得用户企业编码
			String strEnterpriseCode = employee.getEnterpriseCode();
			//申明PageObject对象,并从EJB端获得它的实例对象
			PageObject pObj = null ;
			pObj = remote.findMenutype(strEnterpriseCode,start, limit);
			
			// 转化PageObject类型对象为字符串形式
			String strPageObject =null;
		     if (pObj.getTotalCount() <= 0) {
		      	strPageObject = "{\"list\":[],\"totalCount\":null}";
		     }else{
		    	 strPageObject  = JSONUtil.serialize(pObj);
		     }
		     //打log
			LogUtil.log("Action:取得菜谱类型结束", Level.INFO,null);
			//返回客户端数据
			
		   write(strPageObject);
		
		}catch(RuntimeException e){
			LogUtil.log("Action:取得菜谱类型失败。",Level.SEVERE, e);
		}
		
	}
	/**
	 *  添加菜谱类型
	 * @throws JSONException
	 */
    public void addMenuTypeInfo() throws JSONException{
    	try{
    		//log开始
    		LogUtil.log("Action:增加菜谱类型开始", Level.INFO, null);
    		//生成菜谱类别实体对象
    		AdCMenuType entity = new AdCMenuType();
    		//当前用户ID
   	        String workerCode  = employee.getWorkerCode();
   	        //添加企业编码
   	        
   	        //设置实体对象
    		entity.setEnterpriseCode(employee.getEnterpriseCode());
   	        entity.setIsUse("Y");
    		entity.setMenutypeName(strAddTypeName);
    		entity.setRetrieveCode(strAddRetrieveCode);
    		
    		entity.setUpdateUser(workerCode);
    		//执行菜谱类型维护接口
    		remote.save(entity);
    		write(Constants.ADD_SUCCESS);
    		//action log 结束
    		LogUtil.log("Action:增加菜谱类型结束", Level.INFO, null);
    	}catch(RuntimeException e){
    		write(Constants.ADD_FAILURE);
    		LogUtil.log("Action:增加菜谱类型失败", Level.SEVERE, null);
    	}
    }
    /**
     * 菜谱类型删
     * @throws JSONException
     */
    public void deleteMenuTypeInfo()throws JSONException{
    	LogUtil.log("Action:菜谱类型删除正常开始", Level.INFO, null);
    	try{
    		 //当前用户ID
    	     String workerCode  = employee.getWorkerCode();
    	     //取得序号
    	     Long lngId = Long.parseLong(request.getParameter("id").toString());
    	    //生成菜谱类别实体对象
     		AdCMenuType entity = new AdCMenuType();
     	    entity.setId(lngId);
     	    entity.setUpdateUser(workerCode);
     	    //执行菜谱类型维护接口
     	    remote.logicDelete(entity);
     	    write(Constants.DELETE_SUCCESS);
    		LogUtil.log("Action:菜谱类型删除正常开始", Level.INFO, null);
    	}catch(SQLException e){
    		write(Constants.SQL_FAILURE);
			LogUtil.log("Action:菜谱类型删除失败", Level.SEVERE, null);
    	}
    }
    /**
     * 菜谱类型修改
     * @throws JSONException
     */
    public void updateMenuTypeInfo()throws JSONException{
    	LogUtil.log("Action:菜谱类型修改正常开始", Level.INFO, null);
    	try{
    	 //当前用户ID
   	     String workerCode  = employee.getWorkerCode();
   	     //取得序号
   	     Long lngId = Long.parseLong(request.getParameter("id").toString());
   	     //生成菜谱类别实体对象
    	AdCMenuType entity = new AdCMenuType();
        entity.setId(lngId);
        entity.setMenutypeName(strUpdateTypeName);
        entity.setRetrieveCode(strUpdateRetrieveCode);
        entity.setIsUse("Y");
        entity.setEnterpriseCode(employee.getEnterpriseCode());
        entity.setMenutypeCode(strUpdateTypeCode);
    	entity.setUpdateUser(workerCode);
    	//执行菜谱类型维护接口
    	remote.update(entity);
    	
    	}catch(RuntimeException e){
    		write(Constants.DATA_USING);
    		LogUtil.log("Action:菜谱类型修改异常结束", Level.SEVERE, null);
    	}
    }
    
	/**
	 * 
	 * @return start 开始查询行
	 */
	public int getStart() {
		return start;
	}
   /**
    * 
    * @param start 开始查询行
    */
	public void setStart(int start) {
		this.start = start;
	}
    /**
     * 
     * @return 限制查询行数
     */
	public int getLimit() {
		return limit;
	}
    /**
     * 
     * @param limit 限制查询行数
     */
	public void setLimit(int limit) {
		this.limit = limit;
	}


    /**
     * 
     * @return  修改类型名
     */
	public String getStrUpdateTypeName() {
		return strUpdateTypeName;
	}
    /**
     * 
     * @param strUpdateTypeName  修改类型名
     */
	public void setStrUpdateTypeName(String strUpdateTypeName) {
		this.strUpdateTypeName = strUpdateTypeName;
	}
    /**
     * 
     * @return  修改检索码
     */
	public String getStrUpdateRetrieveCode() {
		return strUpdateRetrieveCode;
	}
    /**
     * 
     * @param strUpdateRetrieveCode  修改检索码
     */
	public void setStrUpdateRetrieveCode(String strUpdateRetrieveCode) {
		this.strUpdateRetrieveCode = strUpdateRetrieveCode;
	}
    /**
     * 
     * @return  类型名
     */
	public String getStrAddTypeName() {
		return strAddTypeName;
	}
    /**
     * 
     * @param strAddTypeName  类型名
     */
	public void setStrAddTypeName(String strAddTypeName) {
		this.strAddTypeName = strAddTypeName;
	}
    /**
     * 
     * @return  检索码
     */
	public String getStrAddRetrieveCode() {
		return strAddRetrieveCode;
	}
    /**
     * 
     * @param strAddRetrieveCode 检索码
     */
	public void setStrAddRetrieveCode(String strAddRetrieveCode) {
		this.strAddRetrieveCode = strAddRetrieveCode;
	}
   /**
    * 
    * @return  修改类型编码
    */
	public String getStrUpdateTypeCode() {
		return strUpdateTypeCode;
	}
   /**
    * 
    * @param strUpdateTypeCode  修改类型编码
    */
	public void setStrUpdateTypeCode(String strUpdateTypeCode) {
		this.strUpdateTypeCode = strUpdateTypeCode;
	}


}