/**
 * Copyright ustcsoft.com
 * All right reserved.
 */

package power.web.administration.carpassregister.action;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJCarpassFacadeRemote;
import power.ejb.administration.AdJCarpass;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
/**
 * 进出车辆登记
 * @author li chensheng
 *  
 */
public class CarPassRegisterAction extends AbstractAction{
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/** 查询，保存，修改数据远程对象 */
	private AdJCarpassFacadeRemote carRemote;
	/** 分页START */
	private int start;
	/** 分页LIMIT */
	private int limit;
	/** 进出车辆登记ejb */
	private AdJCarpass objADJCarpass;		
	/** 车辆流水号 */
	private static final String CAR_ID_VALUE = "carIdValue";
	/** 通行证号 */
	private static final String PASS_CODE_VALUE = "passcodeValue";
	/** 证件类别编码 */
	private static final String PAPER_TYPE_CD_VALUE = "papertypeCdValue";
	/** 前台传值更新时间 */
	private static final String UPDATE_TIME = "updateTime";
	/**
	 * 构造函数
	 */
	public CarPassRegisterAction() {
		carRemote = (AdJCarpassFacadeRemote) factory.getFacadeRemote("AdJCarpassFacade");
	}
	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}	
	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}
	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
	/**
	 * @return 进出车辆登记表
	 */
	public AdJCarpass getObjADJCarpass() {
		return objADJCarpass;
	}
	/**
	 * @param 进出车辆登记表
	 */
	public void setObjADJCarpass(AdJCarpass objADJCarpass) {
		this.objADJCarpass = objADJCarpass;
	}
	/**
	 *  查询所有进出车辆信息 
	 * @throws JSONException
	 * 
	 */
	public void searchCar() throws JSONException {
		LogUtil.log("Action:查询所有进出车辆信息开始。", Level.INFO, null);
		try{
			PageObject obj = null;
			obj = carRemote.findAllCar(employee.getEnterpriseCode(),start,limit);
			String str = JSONUtil.serialize(obj);
			write(str);
			LogUtil.log("Action:查询所有进出车辆信息结束。", Level.INFO, null);
		}catch (SQLException e){
			//显示失败
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:查询所有进出车辆信息失败。", Level.SEVERE, null);
		}catch (JSONException je){
			//显示失败
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询所有进出车辆信息失败。", Level.SEVERE, null);
		}
		
	}
	/**
	 *  删除进出车辆信息 
	 * @throws JSONException
	 * 
	 */
	public void deleteCar() throws JSONException, Exception {
		LogUtil.log("Action:删除进出车辆信息开始。", Level.INFO, null);
		try{
			// 取得车辆流水号
			Long id = Long.parseLong(request.getParameter(CAR_ID_VALUE).toString());
			String strEmployee = employee.getWorkerCode().toString();
			String lastmodifiedDate = request.getParameter(UPDATE_TIME).substring(
					0, 10)
					+ " " + request.getParameter(UPDATE_TIME).substring(11);
			// 修改一条记录
			carRemote.delete(id,strEmployee,lastmodifiedDate);
			LogUtil.log("Action:删除进出车辆信息结束。", Level.INFO, null);
			write(Constants.DELETE_SUCCESS);
		}catch (SQLException sqle) {
			LogUtil.log("Action:删除进出车辆信息失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
		} catch (DataChangeException de) {
			LogUtil.log("Action:删除进出车辆信息失败。", Level.SEVERE, de);
			write(Constants.DATA_USING);
		}

	}
	/**
	 *  修改进出车辆信息
	 * @throws JSONException
	 * 
	 */
	public void updateCar() throws Exception{
		LogUtil.log("Action:更新进出车辆信息开始。", Level.INFO, null);
		try{
			//取得通行证号
			String carPasscode = request.getParameter(PASS_CODE_VALUE);
			// 取得证件类型编码
			String carPapertypeCd = request.getParameter(PAPER_TYPE_CD_VALUE);
			AdJCarpass entity = carRemote.findById(objADJCarpass.getId());
			//更新通行证号
			entity.setPasscode(carPasscode);
			// 通行时间
			entity.setPasstime(objADJCarpass.getPasstime());
			// 车牌号
			entity.setCarNo(objADJCarpass.getCarNo());
			// 更新证件类别
			entity.setPapertypeCd(carPapertypeCd);
			// 更新证件号
			entity.setPaperId(objADJCarpass.getPaperId());
			// 更新单位
			entity.setFirm(objADJCarpass.getFirm());
			// 更新前经办人
			entity.setPreman(objADJCarpass.getPreman());
			// 更新退证时间
			entity.setGiveDate(objADJCarpass.getGiveDate());
			// 更新后经办人
			entity.setPostman(objADJCarpass.getPostman());
			//设置修改者
			entity.setUpdateUser(employee.getWorkerCode());
			String lastmodifiedDate = request.getParameter("updateTime").substring(
					0, 10)
					+ " " + request.getParameter("updateTime").substring(11);
			
			carRemote.update(entity, lastmodifiedDate);
			write(Constants.MODIFY_SUCCESS);
			
		}catch (SQLException sqle) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:保存操作失败", Level.SEVERE, sqle);
		} catch (DataChangeException de) {
			write(Constants.DATA_USING);
			LogUtil.log("Action:保存操作失败", Level.SEVERE, de);
	    } catch (Exception e) {
		   LogUtil.log("Action:保存操作失败", Level.SEVERE, e);
	    }
	    LogUtil.log("Action:更新进出车辆信息结束。", Level.INFO, null);

		
		
	}
	/**
	 *  增加进出车辆信息
	 * @throws JSONException
	 * 
	 */
	public void addCar() throws JSONException, Exception {
		LogUtil.log("Action:增加进出车辆信息开始。", Level.INFO, null);
		// 取得通行证号
		String carPasscode = request.getParameter(PASS_CODE_VALUE);
		// 取得证件类型编码
		String carPapertypeCd = request.getParameter(PAPER_TYPE_CD_VALUE);
		// 取得企业编码
		String enterpriceCode = employee.getEnterpriseCode();
		// 设置企业编码 
		objADJCarpass.setEnterpriseCode(enterpriceCode);
		//设置增加者
		objADJCarpass.setUpdateUser(employee.getWorkerCode());
		//设置使用
		objADJCarpass.setIsUse("Y");
		//设置通行证号
		objADJCarpass.setPasscode(carPasscode);
		//设置证件类型编码
		objADJCarpass.setPapertypeCd(carPapertypeCd);
		try {
			// 增加一条记录
			carRemote.save(objADJCarpass);
			// 显示成功信息
			write(Constants.ADD_SUCCESS);
		} catch (Exception ce) {
			// 显示错误信息
			LogUtil.log("Action:增加进出车辆信息失败。", Level.SEVERE, ce);
		}
		LogUtil.log("Action:增加进出车辆信息结束。", Level.INFO, null);
	}

	
}
