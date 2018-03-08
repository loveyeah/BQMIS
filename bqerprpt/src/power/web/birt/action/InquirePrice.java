package power.web.birt.action;

import java.util.List;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.resource.MrpJPlanInquireDetailFacadeRemote;

public class InquirePrice {
	
	private MrpJPlanInquireDetailFacadeRemote remote;
	
	public static Ejb3Factory factory;
	
	static{
		factory = Ejb3Factory.getInstance();
	}

	public InquirePrice() {
		remote = (MrpJPlanInquireDetailFacadeRemote)factory.getFacadeRemote("MrpJPlanInquireDetailFacade");
	}
	
	public List getInquirePriceList(String gatherIds,String inquireDetailId) {
		List list = remote.getPrintModel(gatherIds, inquireDetailId);
		return list;
	}
}
