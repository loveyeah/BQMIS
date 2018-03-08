/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.carrepairunitmaintenance.action;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.zip.DataFormatException;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdCCarmendWh;
import power.ejb.administration.AdCCarmendWhFacadeRemote;
import power.ejb.administration.comm.CodeCommonFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.CodeConstants;
import power.web.comm.Constants;
/**
 * 车辆维修单位维护Action
 * 
 * @author liugonglei
 * 
 */
public class CarRepairUnitMaintenanceAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	/** 车辆维修单位维护ejb远程对象  */
	protected AdCCarmendWhFacadeRemote remote;
	protected CodeCommonFacadeRemote codeRemote;
	/** 车辆维修单位维护entity  */
	AdCCarmendWh adcCarWhentity;
    /**开始页**/
    public Long start;
    /**页容量**/
    public Long limit;
    /**数据项 **/
    String strRec;
    /**标志常量*/
    private String FLAG1= "{success:true,msg:'1'}";
	/**
	 * 构造函数
	 */
	public CarRepairUnitMaintenanceAction() {
		/** 车辆维修单位维护ejb远程对象 */
		remote = (AdCCarmendWhFacadeRemote) factory
				.getFacadeRemote("AdCCarmendWhFacade");
		codeRemote = (CodeCommonFacadeRemote) factory
				.getFacadeRemote("CodeCommonFacade");
	}
	/**
	 * 车辆维修单位维护画面初始化
	 * @throws JSONException
	 */
	public void getCarRepairUnitQueryList()throws JSONException{
		LogUtil.log("Action：车辆维修单位维护检索开始", Level.INFO, null);
		int intStart = Integer.parseInt(start.toString());
		int intLimit = Integer.parseInt(limit.toString());
		PageObject pbj = new PageObject();
		String strEnterPriseCode = employee.getEnterpriseCode();
		try {
			pbj = remote.findByIsUse(strEnterPriseCode,intStart, intLimit);
		} catch (SQLException e) {
			LogUtil.log("Action：车辆维修单位维护检索异常结束", Level.SEVERE, null);	
			write(Constants.SQL_FAILURE);
		}
		LogUtil.log("Action：车辆维修单位维护检索正常结束", Level.INFO, null);	
		write(JSONUtil.serialize(pbj));
	}
	/**
	 * 删除车辆维修单位数据
	 */
	@SuppressWarnings("unchecked")
	public void deleteCarRepairUnit(){
		  LogUtil.log("Action：删除车辆维修单位数据开始", Level.INFO, null);
	  try {
			Map map = (Map) JSONUtil.deserialize(strRec);
			Long lngId = Long.parseLong(map.get("id").toString());
			// 取得新的entity
			AdCCarmendWh adcCarentity = remote.findById(lngId);
			// 设定是否使用
			adcCarentity.setIsUse(CodeConstants.USE_FLG_N);
			// 设定更新时间
			adcCarentity.setUpdateTime(new Date());
			// 设定更新者
			adcCarentity.setUpdateUser(employee.getWorkerCode());
			try {
				remote.update(adcCarentity);
			} catch (DataFormatException e) {
				LogUtil.log("Action：删除车辆维修单位数据异常结束", Level.SEVERE, null);
				write(Constants.DATA_USING);
			} catch (SQLException e) {
				LogUtil.log("Action：删除车辆维修单位数据异常结束", Level.SEVERE, null);
				write(Constants.SQL_FAILURE);
			}
			LogUtil.log("Action：删除车辆维修单位数据正常结束", Level.INFO, null);
			write(FLAG1);
		} catch (JSONException e) {
			LogUtil.log("Action：删除车辆维修单位数据异常结束", Level.SEVERE, null);
			write(Constants.DATA_USING);
	}
	}
	/**
	 * 添加车辆维修单位数据
	 * @throws JSONException
	 */
	public void addCarRepairUnit()throws JSONException{
		LogUtil.log("Action：插入定期工作维护数据开始", Level.INFO, null);
	    AdCCarmendWh adcCarentity = new AdCCarmendWh();
		try {
			// 取得并设定公司code
			adcCarentity.setCpCode(codeRemote.getCarWHCode());
		} catch (SQLException e) {
			LogUtil.log("Action：插入定期工作维护数据异常结束", Level.SEVERE, null);
			write(Constants.SQL_FAILURE);
		}
		// 设定公司名
		adcCarentity.setCpName(adcCarWhentity.getCpName());
		// 业务范围
		adcCarentity.setBsnRanger(adcCarWhentity.getBsnRanger());
		// 联系人
		adcCarentity.setConnman(adcCarWhentity.getConnman());
		// 联系电话
		adcCarentity.setConTel(adcCarWhentity.getConTel());
		// 单位地址 
		adcCarentity.setCpAddress(adcCarWhentity.getCpAddress());
		// 检索码
		adcCarentity.setRetrieveCode(adcCarWhentity.getRetrieveCode());
		// 是否使用
		adcCarentity.setIsUse(CodeConstants.USE_FLG_Y);
		// 更新时间
		adcCarentity.setUpdateTime(new Date());
		// 更新人员
		adcCarentity.setUpdateUser(employee.getWorkerCode());
		adcCarentity.setEnterPriseCode(employee.getEnterpriseCode());
		try {
			remote.save(adcCarentity);
		} catch (SQLException e) {
			LogUtil.log("Action：插入定期工作维护数据异常结束", Level.SEVERE, null);
			write(Constants.SQL_FAILURE);
		}
		write(FLAG1);
	}
	@SuppressWarnings("unchecked")
	public void updateCarRepairUnit()throws JSONException{
		Map map =(Map) JSONUtil.deserialize(strRec);
		// 设定id
		Long lngId = Long.parseLong(map.get("id").toString());
		AdCCarmendWh adcCarentity = remote.findById(lngId);
		// 设定公司code
		adcCarentity.setCpCode(map.get("cpCode").toString());
		// 设定公司名
		adcCarentity.setCpName(adcCarWhentity.getCpName());
		// 业务范围
		adcCarentity.setBsnRanger(adcCarWhentity.getBsnRanger());
		// 联系人
		adcCarentity.setConnman(adcCarWhentity.getConnman());
		// 联系电话
		adcCarentity.setConTel(adcCarWhentity.getConTel());
		// 单位地址 
		adcCarentity.setCpAddress(adcCarWhentity.getCpAddress());
		// 检索码
		adcCarentity.setRetrieveCode(adcCarWhentity.getRetrieveCode());
		// 设定更新时间
		adcCarentity.setUpdateTime(new Date());
		// 更新人员
		adcCarentity.setUpdateUser(employee.getWorkerCode());
		try {
			remote.update(adcCarentity);
		} catch (DataFormatException e) {
			LogUtil.log("Action：删除车辆维修单位数据异常结束", Level.SEVERE, null);
			write(Constants.DATA_USING);
		} catch (SQLException e) {
			LogUtil.log("Action：插入定期工作维护数据异常结束", Level.SEVERE, null);
			write(Constants.SQL_FAILURE);
		}
		write(FLAG1);
	}
	/**
	 * @return the start
	 */
	public final Long getStart() {
		return start;
	}
	/**
	 * @param start the start to set
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
	 * @return the adcCarWhentity
	 */
	public AdCCarmendWh getAdcCarWhentity() {
		return adcCarWhentity;
	}
	/**
	 * @param adcCarWhentity the adcCarWhentity to set
	 */
	public void setAdcCarWhentity(AdCCarmendWh adcCarWhentity) {
		this.adcCarWhentity = adcCarWhentity;
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
}
