package power.web.birt.action;

import java.util.ArrayList;
import java.util.List;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.resource.business.PurchaseWarehouse;


public class ReceiptBill {
	
	private PurchaseWarehouse remote;

	public static Ejb3Factory factory;

	static {
		factory = Ejb3Factory.getInstance();
	}

	/**
	 * 构造函数
	 */
	public ReceiptBill() {
		remote = (PurchaseWarehouse) factory
				.getFacadeRemote("PurchaseWarehouseImpl");
	}

	@SuppressWarnings("unchecked")
	// modified by liuyi 20100430  metailIdNotIn 来源为固定资产类的物资
//	public List setWorkticketDangerForPrint(String purNo, String whsNo, String arrivalNo,String operateDate,String flag) {
	public List setWorkticketDangerForPrint(String purNo, String whsNo, String arrivalNo,String operateDate,String flag,String metailIdNotIn) {
		List list = new ArrayList();
//		for(int i=0;i<2;i++) {
//			Bean bean = new Bean();
//			bean.setName("oneName"+i);
//			bean.setPass("onePass"+i);
//			List listTwo = new ArrayList();
//			if(i==0) {
//				for(int j=0;j<3;j++) {
//					BeanTwo beanTwo = new BeanTwo();
//					beanTwo.setUserName("twoName"+j);
//					beanTwo.setPassWord("twoPass"+j);
//					listTwo.add(beanTwo);
//					
//				}
//			} else {
//				for(int j=0;j<2;j++) {
//					BeanTwo beanTwo = new BeanTwo();
//					beanTwo.setUserName("twoName"+j);
//					beanTwo.setPassWord("twoPass"+j);
//					listTwo.add(beanTwo);
//					
//				}
//			}
//			bean.setList(listTwo);
//			list.add(bean);
//		}
		
		//  modified by liuyi 20100430 
		//modify by drdu 091120
		//flag==1表示补打印，flag==""表示入库打印
//		if(flag == null||flag=="") 
//		{
//		 list = remote.getReceiptBillMaterialInfo(purNo, whsNo, arrivalNo);
//		}else if(flag.equals("1")){ //采购入库补打印报表 add by drdu 091120
//			 list = remote.getAfterReceiptBillMaterialInfo(operateDate, arrivalNo,whsNo);
//		}
		if(flag == null||flag=="") 
		{
		 list = remote.getReceiptBillMaterialInfo(purNo, whsNo, arrivalNo,metailIdNotIn);
		}else if(flag.equals("1")){ //采购入库补打印报表
			 list = remote.getAfterReceiptBillMaterialInfo(operateDate, arrivalNo,whsNo,metailIdNotIn);
		}
		return list;
	}
	
	
	
	/**
	 * 固定资产的物资入库打印
	 * add by liuyi 20100430 
	 * @param purNo
	 * @param whsNo
	 * @param arrivalNo
	 * @param operateDate
	 * @param flag
	 * @param metailIdNotIn
	 * @return
	 */
	public List getGdPlanListForPrint(String purNo, String whsNo, String arrivalNo,String operateDate,String flag,String materialId) {
		List list = new ArrayList();
		// 入库打印
		if(flag == null||flag=="") 
		{
		 list = remote.getReceiptGdMaterialInfo(purNo, whsNo, arrivalNo,materialId);
		}
		else if(flag.equals("1")){ //采购入库补打印报表 
			 list = remote.getAfterReceiptGdMaterialInfo(operateDate, arrivalNo,whsNo,materialId);
		}
		return list;
	}

}
