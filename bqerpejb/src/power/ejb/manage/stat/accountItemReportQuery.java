package power.ejb.manage.stat;

import java.text.ParseException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

@Remote
public interface accountItemReportQuery {

	public PageObject findItemNameList(String accountCode,
			String enterpriseCode, final int... rowStartIdxAndCount);

	@SuppressWarnings("unchecked")
	public List findItemDataList(String accountCode, String itemType,
			String startDate, String endDate, String enterpriseCode)
			throws ParseException;

	// derek added 2009/06/24
	@SuppressWarnings("unchecked")
	public List getDailyReport(String reportCode, String reportDate,
			String enterpriseCode);
	//add by bjxu 100604
	public List<String[]> getStringDailyReportList(String reportCode,String reportDate);
}
