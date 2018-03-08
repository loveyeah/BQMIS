/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.birt.action.bqmis;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.opticket.OpticketApprove;
import power.ejb.opticket.form.DangerousBaseForPrint;

/**
 *  热力机械危险点控制票数据填充类
 * @author LiuYingwen
 *
 */
public class EnergMachineDangerControl {
	
	 public static Ejb3Factory factory;
		
		static{
			factory = Ejb3Factory.getInstance();
		}

		/** 操作票EJB接口 */
		private OpticketApprove remote;

		/**
		 * 构造函数
		 */
		public EnergMachineDangerControl() {
			remote = (OpticketApprove) factory
			    .getFacadeRemote("OpticketApproveImpl");
		}
		
		/**
		 * 调用EJB接口获得票面需要的数据和措施列表
		 * @param opticketCode
		 * @return DangerousBaseForPrint 包含基本数据和措施列表类
		 */
		public  DangerousBaseForPrint setDangerBean(String opticketCode) {
			DangerousBaseForPrint danger = remote.getDangeroursData(opticketCode);
			return danger;
		}

}
