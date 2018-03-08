package power.web.birt.action;

import java.util.List;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.resource.business.MonthBalance;

public class ReceiveIssuesDetailsStat {

	private MonthBalance remote;

	public static Ejb3Factory factory;

	static {
		factory = Ejb3Factory.getInstance();
	}

	public ReceiveIssuesDetailsStat() {
		remote = (MonthBalance) factory.getFacadeRemote("MonthBalanceImp");
	}

	@SuppressWarnings("unchecked")
	public List findThisMonthInfoByWarehouse(String whsNo,String dateMonth) {
		//modify by fyyang  090811
		List list = remote.getThisMonthInfoByWarehouse(whsNo,dateMonth);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List getMonthInfoOfWareByCodeAndMonth(String whsNo,String dateMonth) {
		//modify by fyyang  090811
		List list = remote.getMonthInfoOfWareByCodeAndMonth(whsNo,dateMonth);
		return list;
	}

}
