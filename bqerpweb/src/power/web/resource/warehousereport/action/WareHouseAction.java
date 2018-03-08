/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.resource.warehousereport.action;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import power.ear.comm.Employee;
import power.ejb.resource.InvCMaterialFacadeRemote;
import power.ejb.resource.WareHouseBean;
import power.ejb.resource.WareHouseListBean;
import power.web.comm.AbstractAction;

import java.text.SimpleDateFormat;

/**
 * 库存再订货报表Action
 * @author zhujie
 *
 */
public class WareHouseAction extends AbstractAction{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/** 远程 */
	private InvCMaterialFacadeRemote remote;
	/**
	 * 构造函数
	 */
	public WareHouseAction() {
		remote = (InvCMaterialFacadeRemote) factory.getFacadeRemote("InvCMaterialFacade");
	}
	
	 /**
	  * 获取库存再订货报表信息
	  * @param enterpriseCode
	  * @return WareHouseBean
	  */
	public WareHouseBean getWareHouseBean(String enterpriseCode){
		WareHouseBean entity = new WareHouseBean();
		List<WareHouseListBean> wareHouseList = remote.findAllForWareHouse(enterpriseCode);
		// 设置当前日期
		SimpleDateFormat mFormatTimeOnly = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());   
		Date dDate = new Date();   
		String strTime = mFormatTimeOnly.format(dDate);
		entity.setNowDate(strTime);
		// 设置Table所需详细信息
		entity.setWareHouseList(wareHouseList);
		return entity;
	}
}
