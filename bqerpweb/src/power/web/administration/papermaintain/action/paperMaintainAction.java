/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.papermaintain.action;


import java.sql.SQLException;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdCPaperFacadeRemote;
import power.ejb.administration.AdCPaper;
import power.ejb.hr.LogUtil;

/**
 * 证件类别维护
 * 
 * @author li chensheng
 * @version 1.0
 */
public class paperMaintainAction extends AbstractAction{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/** 查询，保存，修改数据远程对象 */
	private AdCPaperFacadeRemote paperRemote;
	/** 证件类别维护entity */
	private AdCPaper adCPaper;
	/** 分页start */
	private int start;
	/** 分页limit */
	private int limit;
	/** 证件类别流水号 */
	private static final String PAPER_ID_VALUE = "paperIdValue";
	/**
	 * 构造函数
	 */
	public paperMaintainAction(){
		paperRemote = (AdCPaperFacadeRemote) factory
		          .getFacadeRemote("AdCPaperFacade");
	}
	/**
	 * 取得分页start
	 */
	public int getStart() {
		return start;
	}
	/**
	 * 设置分页start
	 */
	public void setStart(int start) {
		this.start = start;
	}
	/**
	 * 取得分页limit
	 */
	public int getLimit() {
		return limit;
	}
	/**
	 * 设置分页limit
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
	/**
	 * 取得证件类别维护entity
	 */
	public AdCPaper getAdCPaper() {
		return adCPaper;
	}
	/**
	 * 设置证件类别维护entity
	 */
	public void setAdCPaper(AdCPaper adCPaper) {
		this.adCPaper = adCPaper;
	}
	/**
	 *  查询所有证件类别信息
	 * 
	 * @throws JSONException
	 * 
	 */
	public void searchPaper() throws JSONException, Exception {
        LogUtil.log("Action:查询所有证件类别信息开始。", Level.INFO, null);
        try{
        	//PageObject obj = new PageObject();
        	PageObject obj = paperRemote.findAllPaper(employee.getEnterpriseCode(),start,limit);
    		String str ="";
    		// 如果查询返回结果为空，则替换为如下返回结果
            if (obj.getTotalCount()<=0) {
                str = "{\"list\":[],\"totalCount\":null}";
            }else{
            	//序列化PageObject类型对象,转化为字符串类型
                 str = JSONUtil.serialize(obj);           	
            }
            write(str);
            LogUtil.log("Action:查询所有证件类别信息结束。", Level.INFO, null);
        }catch (JSONException jse){
        	write(Constants.DATA_FAILURE);
        	LogUtil.log("Action:查询所有证件类别信息失败。", Level.SEVERE, jse);
        }catch (SQLException se){
        	write(Constants.SQL_FAILURE);
        	LogUtil.log("Action:查询所有证件类别信息失败。", Level.SEVERE, se);
        }
		
	}
	/**
	 *  删除证件类别信息
	 * 
	 * @throws JSONException
	 * 
	 */
	public void deletePaper() throws JSONException, Exception {
		LogUtil.log("Action:删除证件类别信息开始。", Level.INFO, null);
		try{
   		 //当前用户ID
   	     String workerCode  = employee.getWorkerCode();
   	     //取得序号
   	     Long lngId = Long.parseLong(request.getParameter(PAPER_ID_VALUE).toString());
   	     //生成证件类别实体对象
   	     AdCPaper entity = new AdCPaper();
    	 entity.setId(lngId);
    	 entity.setUpdateUser(workerCode);
    	 //执行证件类型维护接口
    	 paperRemote.logicDelete(entity);
    	 write(Constants.DELETE_SUCCESS);
   		LogUtil.log("Action:删除证件类别信息结束", Level.INFO, null);
   	}catch(SQLException e){
   		write(Constants.SQL_FAILURE);
			LogUtil.log("Action:菜谱类型删除失败", Level.SEVERE, null);
   	}
   }
	/**
	 *  增加证件类别信息
	 * 
	 * 
	 * @throws JSONException
	 * 
	 */
	public void addPaper() throws JSONException, Exception {
		LogUtil.log("Action:增加证件类别信息开始。", Level.INFO, null);
		try {
		//获取证件类别编码
		String strPaperTypeCode = paperRemote.getPaperTypeCode();
		if(strPaperTypeCode.length() < 2){
			strPaperTypeCode = "0" + strPaperTypeCode;
		}
		//设置证件类别编码
		adCPaper.setPapertypeCode(strPaperTypeCode);
		//设置企业编码
		adCPaper.setEnterpriseCode(employee.getEnterpriseCode());
		//设置增加者
		adCPaper.setUpdateUser(employee.getWorkerCode());
		//设置使用
		adCPaper.setIsUse("Y");
			// 增加一条记录
			paperRemote.save(adCPaper);
			// 显示成功信息
			write(Constants.ADD_SUCCESS);
			LogUtil.log("Action:增加证件类别信息结束。", Level.INFO, null);	
		} catch (Exception ce) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:增加证件类别信息异常。", Level.SEVERE, ce);
		}
		
	}
	/**
	 *  修改证件类别信息
	 * @param
	 * 
	 * @throws JSONException
	 * 
	 */
	public void updatePaper() throws JSONException, Exception {
		LogUtil.log("Action:更新证件类别信息开始。", Level.INFO, null);
		try{
			//AdCPaper entity = new AdCPaper();
			AdCPaper entity = paperRemote.findById(adCPaper.getId());
		    // 设置证件类别名称
		    entity.setPapertypeName(adCPaper.getPapertypeName());
		    // 设置检索码
		    entity.setRetrieveCode(adCPaper.getRetrieveCode());
		    // 设置更新者
		    entity.setUpdateUser(employee.getWorkerCode());
		    // 更新操作
		    paperRemote.update(entity);
		    write(Constants.MODIFY_SUCCESS);
		}catch(Exception ce){
    		write(Constants.SQL_FAILURE);
    		LogUtil.log("Action:更新证件类别信息异常结束", Level.SEVERE,ce);
    	}	
	}
}
