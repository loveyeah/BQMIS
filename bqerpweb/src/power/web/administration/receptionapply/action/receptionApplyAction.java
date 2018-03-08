/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.receptionapply.action;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.web.comm.AbstractAction;
import power.web.comm.CodeConstants;
import power.web.comm.Constants;
import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJReception;
import power.ejb.administration.AdJReceptionFacadeRemote;
import power.ejb.administration.business.ReceptionApplyFacadeRemote;
import power.ejb.administration.comm.CodeCommonFacadeRemote;
import power.ejb.hr.LogUtil;
/**
 * 接待申请上报
 * 
 * @author liugonglei
 * @version 1.0
 */
public class receptionApplyAction extends AbstractAction {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/** 定义接口 */
    /**
     * 接待申请上报接口
     */
	ReceptionApplyFacadeRemote receptAppRemote;
	// 共通code接口
	protected CodeCommonFacadeRemote codeRemote;
	/**
	 * 来宾接待审批单
	 */
	AdJReceptionFacadeRemote adjReceptRemote;
    /**开始页**/
    public Long start;
    /**页容量**/
    public Long limit;
    /**记录**/
    public String strRec;
    /** entity**/
    AdJReception entity;
    /**标志常量*/
    private String FLAG= "{success:true,msg:'1'}";
	/**
	 * 构造函数
	 */
	public receptionApplyAction() {
		// 接待申请上报接口
		receptAppRemote = (ReceptionApplyFacadeRemote) factory
				.getFacadeRemote("ReceptionApplyFacade");
		// 来宾接待审批单
		adjReceptRemote = (AdJReceptionFacadeRemote) factory
				.getFacadeRemote("AdJReceptionFacade");
		// 共通code接口
		codeRemote = (CodeCommonFacadeRemote) factory
		.getFacadeRemote("CodeCommonFacade");
	}
    /**
     * employee 获取
     * 
     * @throws JSONException
     */
    public void getSession() throws JSONException {
    	LogUtil.log("Action：employee 获取开始", Level.INFO, null);
    	try {
			LogUtil.log("Action：employee 获取正常结束", Level.INFO, null);
			
			write(JSONUtil.serialize(employee));
		}catch (JSONException e){
    		LogUtil.log("Action：employee 获取异常结束", Level.SEVERE, e);
			write(Constants.DATA_FAILURE);
    	}
    }
	/**
	 * 接待申请一览检索
	 * @throws JSONException
	 */
	public void findRecepAppData() throws JSONException {
		LogUtil.log("Action：接待申请一览检索开始", Level.INFO, null);
		try {
			// 开始页
			int intStart = Integer.parseInt(start.toString());
			// 页容量
			int intLimit = Integer.parseInt(limit.toString());
			// 员工id
			String strWorkCode = employee.getWorkerCode();
			String strEnterPriseCode = employee.getEnterpriseCode();
			PageObject pbj = receptAppRemote.findReceptApply(strWorkCode,
					strEnterPriseCode, intStart, intLimit);
			LogUtil.log("Action：接待申请一览检索正常结束", Level.INFO, null);
			write(JSONUtil.serialize(pbj));
		} catch (SQLException e) {
			LogUtil.log("Action：接待申请一览检索异常结束", Level.SEVERE, null);
			write(Constants.SQL_FAILURE);
		} catch (JSONException e) {
			LogUtil.log("Action：接待申请一览检索异常结束", Level.SEVERE, null);
			write(Constants.DATA_FAILURE);
		}
	}
	/**
	 * 删除接待申请数据
	 *  
	 */
	@SuppressWarnings("unchecked")
	public void deleteReceptionApply() {
		LogUtil.log("Action：删除接待申请数据开始", Level.INFO, null);
		try {
			Map map = (Map) JSONUtil.deserialize(strRec);
			Long lngId = Long.parseLong(map.get("id").toString());
			// 取得要删除的数据
			AdJReception adjReceptEntity = adjReceptRemote.findById(lngId);
		    // 设定是否使用
			adjReceptEntity.setIsUse(CodeConstants.USE_FLG_N);
		    // 设定更新者
			adjReceptEntity.setUpdateUser(employee.getWorkerCode());
			// 取得更新时间
			Long lngUpdateTime = new Long(map.get("updateTime").toString());
			receptAppRemote.updateReceptApply(adjReceptEntity, lngUpdateTime);
			LogUtil.log("Action：删除接待申请数据正常结束", Level.INFO, null);
			write(FLAG);
		} catch (JSONException e) {
			LogUtil.log("Action：删除接待申请数据异常结束", Level.SEVERE, e);
			write(Constants.DATA_FAILURE);
		} catch (SQLException e){
			LogUtil.log("Action：删除接待申请数据异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
		} catch (DataChangeException e){
			LogUtil.log("Action：删除接待申请数据异常结束", Level.SEVERE, e);
			write(Constants.DATA_USING);
		}
	}
	/**
	 * 上报接待申请数据
	 */
	@SuppressWarnings("unchecked")
	public void reportReceptionApply() {
		LogUtil.log("Action：上报接待申请数据开始", Level.INFO, null);
		try {
			Map map = (Map) JSONUtil.deserialize(strRec);
			Long lngId = Long.parseLong(map.get("id").toString());
			// 取得要上报的数据
			AdJReception adjReceptEntity = adjReceptRemote.findById(lngId);
		    // 设定上报状态
			adjReceptEntity.setDcmStatus(CodeConstants.FROM_STATUS_1);
		    // 设定更新者
			adjReceptEntity.setUpdateUser(employee.getWorkerCode());
			// 取得更新时间
			Long lngUpdateTime = new Long(map.get("updateTime").toString());
			receptAppRemote.updateReceptApply(adjReceptEntity, lngUpdateTime);
			LogUtil.log("Action：上报接待申请数据正常结束", Level.INFO, null);
			write(FLAG);
		} catch (JSONException e) {
			LogUtil.log("Action：上报接待申请数据异常结束", Level.SEVERE, e);
			write(Constants.DATA_FAILURE);
		} catch (SQLException e){
			LogUtil.log("Action：上报接待申请数据异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
		} catch (DataChangeException e){
			LogUtil.log("Action：上报接待申请数据异常结束", Level.SEVERE, e);
			write(Constants.DATA_USING);
		}
	}
	/**
	 * 插入上报接待申请数据
	 */
	public void addReceptionApply() {
		LogUtil.log("Action：插入上报接待申请数据开始", Level.INFO, null);
		try {
			AdJReception adjReceptEntity = new AdJReception();
			// 取得并设定id
			adjReceptEntity.setId(receptAppRemote.getReceptionId());
			// 取得并设定接待申请单号
			adjReceptEntity.setApplyId(codeRemote.getReciveAplNoCode(employee
					.getWorkerCode()));
			// 设定申请人
			adjReceptEntity.setApplyMan(employee.getWorkerCode());
			// 设定填表日期
			adjReceptEntity.setLogDate(new Date());
			// 设定接待日期
			adjReceptEntity.setMeetDate(entity.getMeetDate());
			// 设定就餐人数
			adjReceptEntity.setRepastNum(entity.getRepastNum());
			// 设定住宿人数
			adjReceptEntity.setRoomNum(entity.getRoomNum());
			// 接待说明 
			adjReceptEntity.setMeetNote(entity.getMeetNote());
			// 就餐标准
			adjReceptEntity.setRepastBz(entity.getRepastBz());
			// 住宿标准
			adjReceptEntity.setRoomBz(entity.getRoomBz());
			// 就餐安排
			adjReceptEntity.setRepastPlan(entity.getRepastPlan());
			// 住宿安排
			adjReceptEntity.setRoomPlan(entity.getRoomPlan());
			// 其他
			adjReceptEntity.setOther(entity.getOther());
			// 标准金额
			adjReceptEntity.setPayoutBz(entity.getPayoutBz());
			// 修改人
			adjReceptEntity.setUpdateUser(employee.getWorkerCode());
			// 差额
			adjReceptEntity.setBalance(entity.getBalance());
			// 修改时间
			adjReceptEntity.setUpdateTime(new Date());
			// 单据状态
			adjReceptEntity.setDcmStatus(CodeConstants.FROM_STATUS_0);
			// 是否使用
			adjReceptEntity.setIsUse(CodeConstants.USE_FLG_Y);
			adjReceptEntity.setEnterpriseCode(employee.getEnterpriseCode());
			// 数据上传
			adjReceptRemote.save(adjReceptEntity);
			LogUtil.log("Action：插入上报接待申请数据正常结束", Level.INFO, null);
			write(FLAG);
		}catch (SQLException e){
			LogUtil.log("Action：插入上报接待申请数据异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
		}
	}
	/**
	 * 更新上报接待申请数据
	 */
	@SuppressWarnings("unchecked")
	public void updateReceptionApply(){
		LogUtil.log("Action：更新上报接待申请数据开始", Level.INFO, null);
		try{
			Map map = (Map) JSONUtil.deserialize(strRec);
			Long lngId = Long.parseLong(map.get("id").toString());
			// 取得要上报的数据
			AdJReception adjReceptEntity = adjReceptRemote.findById(lngId);
			// 设定接待日期
			adjReceptEntity.setMeetDate(entity.getMeetDate());
			// 设定就餐人数
			adjReceptEntity.setRepastNum(entity.getRepastNum());
			// 设定住宿人数
			adjReceptEntity.setRoomNum(entity.getRoomNum());
			// 接待说明 
			adjReceptEntity.setMeetNote(entity.getMeetNote());
			// 就餐标准
			adjReceptEntity.setRepastBz(entity.getRepastBz());
			// 住宿标准
			adjReceptEntity.setRoomBz(entity.getRoomBz());
			// 就餐安排
			adjReceptEntity.setRepastPlan(entity.getRepastPlan());
			// 住宿安排
			adjReceptEntity.setRoomPlan(entity.getRoomPlan());
			// 其他
			adjReceptEntity.setOther(entity.getOther());
			// 差额
			adjReceptEntity.setBalance(entity.getBalance());
			// 标准金额
			adjReceptEntity.setPayoutBz(entity.getPayoutBz());
			// 修改人
			adjReceptEntity.setUpdateUser(employee.getWorkerCode());
			// 取得更新时间
			Long lngUpdateTime = new Long(map.get("updateTime").toString());
			receptAppRemote.updateReceptApply(adjReceptEntity, lngUpdateTime);
			LogUtil.log("Action：更新接待申请数据正常结束", Level.INFO, null);
			write(FLAG);
		}catch(SQLException e){
			LogUtil.log("Action：更新接待申请数据异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
		}catch (DataChangeException e){
			LogUtil.log("Action：更新接待申请数据异常结束", Level.SEVERE, e);
			write(Constants.DATA_USING);
		} catch (JSONException e){
			LogUtil.log("Action：更新接待申请数据异常结束", Level.SEVERE, e);
			write(Constants.DATA_FAILURE);
		}
	}
	/**
	 * @return the start
	 */
	public final Long getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public final void setStart(Long start) {
		this.start = start;
	}
	/**
	 * @return the limit
	 */
	public final Long getLimit() {
		return limit;
	}
	/**
	 * @param limit the limit to set
	 */
	public final void setLimit(Long limit) {
		this.limit = limit;
	}

	/**
	 * @return the strRec
	 */
	public String getStrRec() {
		return strRec;
	}
	/**
	 * @param strRec the strRec to set
	 */
	public void setStrRec(String strRec) {
		this.strRec = strRec;
	}
	/**
	 * @return the entity
	 */
	public AdJReception getEntity() {
		return entity;
	}
	/**
	 * @param entity the entity to set
	 */
	public void setEntity(AdJReception entity) {
		this.entity = entity;
	}
}
