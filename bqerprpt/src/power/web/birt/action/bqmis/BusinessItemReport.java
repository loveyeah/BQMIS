package power.web.birt.action.bqmis;

import java.util.List;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.manage.budget.CbmCBusinessItemFacadeRemote;
import power.ejb.manage.budget.form.BusinessItemForm;

public class BusinessItemReport {
	
	public static Ejb3Factory factory;
	
	public CbmCBusinessItemFacadeRemote businessItemRemote;

	static {
		factory = Ejb3Factory.getInstance();
	}
	
	public BusinessItemReport() {
		businessItemRemote = (CbmCBusinessItemFacadeRemote) factory.getFacadeRemote("CbmCBusinessItemFacade");
	}
	
	public List<BusinessItemForm> getBusinessItemList(String dateTime) {
		List<BusinessItemForm> bItemLsit = businessItemRemote.getBusinessItemReportPrint(dateTime);
		return bItemLsit;
	}
}
