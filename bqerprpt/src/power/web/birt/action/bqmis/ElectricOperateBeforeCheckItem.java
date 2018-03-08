/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.birt.action.bqmis;

import java.util.List;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.opticket.OpticketApprove;
import power.ejb.opticket.bussiness.RunJOpMeasures;
import power.ejb.opticket.form.DangerousBaseForPrint;
import power.ejb.opticket.form.WorkBaseForPrint;

/**
 * 电气倒闸操作前应完成的工作表
 * @author LiuYingwen 
 */
public class ElectricOperateBeforeCheckItem {
	
    public static Ejb3Factory factory;
	
	static{
		factory = Ejb3Factory.getInstance();
	}

	/** 操作票EJB接口 */
	private OpticketApprove remote;

	/**
	 * 构造函数
	 */
	public ElectricOperateBeforeCheckItem() {
		remote = (OpticketApprove) factory
		    .getFacadeRemote("OpticketApproveImpl");
	}
	
	/**
	 * 获取票面所需基本数据数据和操作列表方法
	 * @param opticketCode
	 * @return WorkBaseForPrint 票面所需基本数据和操作列表
	 */
	public WorkBaseForPrint setOperateTicketBean(String opticketCode) {
		WorkBaseForPrint model = remote.getBefWorkData(opticketCode);
		
		return model;
	}
	
	/**
	 * 获取票面所需危险点列表方法
	 * @param opticketCode
	 * @return List<RunJOpMeasures> 票面所需危险点列表
	 */
	public  List<RunJOpMeasures> setDangerListBean(String opticketCode) {
		DangerousBaseForPrint danger = remote.getDangeroursData(opticketCode);
		List<RunJOpMeasures> dangerList = danger.getList();
		return dangerList;
	}

}
