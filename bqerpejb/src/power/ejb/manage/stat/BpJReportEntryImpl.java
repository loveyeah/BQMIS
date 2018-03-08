package power.ejb.manage.stat;

import java.text.ParseException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.stat.form.StatItemComputeForm;
import power.ejb.manage.stat.form.StatItemEntry;

@Remote
public interface BpJReportEntryImpl {
	// 查询运行报表录入列表
	public List<StatItemEntry> findEntryItemListByReport(String dateType,
			String reportCode, String date, String delayTime, String delayUnit,
			String enterpriseCode) throws ParseException;

	// 保存录入数据
	public void saveReportEntryItemValue(List<StatItemEntry> list,
			String timeType);

	// 运行报表数据查询
	public PageObject reportQuery(String reportCode, String startDate,
			String timeType, String enterpriseCode);

	// 查询运行报表数据补充录入列表
	public List<StatItemEntry> findAddEntryByReport(String dateType,
			String reportCode, String date, String delayTime, String delayUnit,
			String enterpriseCode) throws ParseException;

	// 运行报表录入计算
	public void collectCompute(String type) throws Exception;

	// 统计指标采集计算
	public void statItemCollectCompute(StatItemComputeForm model,
			String enterpriseCode) throws Exception;

	// 跟据报表类型查询报表对应的指标列表
	public List<StatItemEntry> findReportItemListForEntry(String reportCode,
			String enterpriseCode);

	// 跟据台帐类型查询报表对应的指标列表
	public List<StatItemEntry> findAccountItemListForEntry(String reportCode,
			String enterpriseCode);

	// 查询报表指标值
	// modified by liuyi 20100513 增加班组指标 班组描述 desc
//	public List<StatItemEntry> findReportItemValueListForEntry(
//			String reportCode, int timedot, String date, String tableName,
//			String enterpriseCode);
	public List<StatItemEntry> findReportItemValueListForEntry(
			String reportCode, int timedot, String date, String tableName,
			String enterpriseCode,String desc);

	// 查询台帐指标值
	public List<StatItemEntry> findAccountItemValueListForEntry(
			String reportCode, int timedot, String date, String tableName,
			String enterpriseCode);

	// 查询计算列表
	public PageObject findComputeStatItemList(String dataTimeType, int start,
			int limit);
}
