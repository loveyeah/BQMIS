/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.birt.action.bqmis;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.opticket.OpticketApprove;
import power.ejb.opticket.form.WorkBaseForPrint;

/**
 * 电气倒闸操作后应完成的工作表
 * @author LiuYingwen 
 */
public class ElectricOperateAfterCheckItem {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
    public static Ejb3Factory factory;
	
	static{
		factory = Ejb3Factory.getInstance();
	}

	/** 操作票EJB接口 */
	private OpticketApprove remote;

	/**
	 * 构造函数
	 */
	public ElectricOperateAfterCheckItem() {
		remote = (OpticketApprove) factory
		    .getFacadeRemote("OpticketApproveImpl");
	}
	
	/**
	 * 获取票面所需基本数据数据和操作列表方法
	 * @return OperateTicketBean 票面所需基本数据和操作列表
	 */
	public WorkBaseForPrint setOperateTicketBean(String opticketCode) {
		WorkBaseForPrint model = remote.getAftWorkData(opticketCode);
		return model;
	}
	

}
