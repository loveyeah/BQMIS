package power.web.birt.action.bqmis;

import java.util.List;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.manage.budget.CbmCReportItemFacadeRemote;
import power.ejb.manage.budget.form.ReportItemForm;

public class ReportItemReport {
	
public static Ejb3Factory factory;
	
	public CbmCReportItemFacadeRemote reportItemRemote;

	static {
		factory = Ejb3Factory.getInstance();
	}
	
	public ReportItemReport() {
		reportItemRemote = (CbmCReportItemFacadeRemote) factory.getFacadeRemote("CbmCReportItemFacade");
	}
	
	public List<ReportItemForm> getReportItemListByYear(String dateTime,String reportType) {
		List<ReportItemForm> bItemLsit = reportItemRemote.getReportItemListByYear(dateTime,reportType);
		return bItemLsit;
	}
	
	public List<ReportItemForm> getReportItemListByMonthRoll(String dateTime,String reportType) {
		List<ReportItemForm> bItemLsit = reportItemRemote.getReportItemListByMonthRoll(dateTime,reportType);
		return bItemLsit;
	}

}
