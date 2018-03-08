package power.ejb.manage.stat;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * 统计报表名称维护
 * 
 * @author fyyang 090608
 */
@Remote
public interface BpCStatReportFacadeRemote {

	/**
	 * 增加、删除及修改统计报表名称信息
	 * 
	 * @param list
	 *            增加或修改的记录
	 * @param delIds
	 *            要删除的记录的id
	 * @throws CodeRepeatException
	 */
	public void modifyRecords(List<BpCStatReport> list, String delIds)
			throws CodeRepeatException;

	/**
	 * 查找统计报表名称信息列表
	 * 
	 * @param reportName
	 *            报表名称
	 * @param enterpriseCode
	 *            企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String reportName, String enterpriseCode,String workerCode,
			String type, final int... rowStartIdxAndCount);
}