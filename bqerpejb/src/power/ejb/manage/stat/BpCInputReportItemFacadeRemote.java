package power.ejb.manage.stat;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 录入报表项目
 * 
 * @author drdu
 */
@Remote
public interface BpCInputReportItemFacadeRemote {

	/**
	 * 增加
	 * 
	 * @param entity
	 */
	public void save(BpCInputReportItem entity);

	public void save(List<BpCInputReportItem> addList);

	/**
	 * 删除
	 * 
	 * @param entity
	 */
	public Long isNew(String reportCode, String itemCode);

	public void deleteMuti(String itemCode, Long reportCode);

	/**
	 * 修改
	 * 
	 * @param entity
	 * @return
	 */
	public BpCInputReportItem update(BpCInputReportItem entity);

	public void update(List<BpCInputReportItem> updateList);

	/**
	 * 根据ID查找记录
	 * 
	 * @param id
	 * @return
	 */
	public BpCInputReportItem findById(BpCInputReportItemId id);

	/**
	 * 根据录入报表项目编码查找记录
	 * 
	 * @param reportCode
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String reportCode, String enterpriseCode,
			final int... rowStartIdxAndCount);
}