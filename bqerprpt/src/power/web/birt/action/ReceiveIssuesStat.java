package power.web.birt.action;

import java.util.List;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.resource.business.MonthBalance;

public class ReceiveIssuesStat {

	private MonthBalance remote;

	public static Ejb3Factory factory;

	static {
		factory = Ejb3Factory.getInstance();
	}

	public ReceiveIssuesStat() {
		remote = (MonthBalance) factory
				.getFacadeRemote("MonthBalanceImp");
	}

	public List findWarehouseThisMonthInfo(String dateMonth) {
		//modify by fyyang 090811
		List list = remote.getWarehouseThisMonthInfo(dateMonth);
		return list;
	}
	public List getWarehouseThisMonthDetailInfo(String dateMonth,String whsNo) {
		//modify by fyyang 091202
		List list = remote.getWarehouseThisMonthDetailInfo(dateMonth,whsNo);
		return list;
	}

	// add by liuyi 20100319
	public List findWareOfMonthInfo(String dateMonth) {
		List list = remote.findWareOfMonthInfo(dateMonth);
		return list;
	}
	
	// add by liuyi 20100319
	public List getWarehousOfMonthDetailInfo(String dateMonth,String whsNo) {
		List list = remote.getWarehousOfMonthDetailInfo(dateMonth,whsNo);
		return list;
	}
}
