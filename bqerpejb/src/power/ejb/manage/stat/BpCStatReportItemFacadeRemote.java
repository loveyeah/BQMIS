package power.ejb.manage.stat;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for BpCStatReportItemFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpCStatReportItemFacadeRemote {

	/**
	 * 增加
	 * 
	 * @param entity
	 */
	public void save(BpCStatReportItem entity);

	public void save(List<BpCStatReportItem> addList,
			List<BpCStatReportItem> updateList, String itemCode, Long reportCode);

	/**
	 * 判断选择的指标是否已存在
	 * 
	 * @param reportCode
	 * @param itemCode
	 * @return
	 */
	public Long isNew(String reportCode, String itemCode);

	/**
	 * 修改
	 * 
	 * @param entity
	 * @return
	 */
	public BpCStatReportItem update(BpCStatReportItem entity);

	public void update(List<BpCStatReportItem> updateList);

	/**
	 * 删除
	 * 
	 * @param itemCode
	 * @param reportCode
	 */
	public void deleteMuti(String itemCode, Long reportCode);

	/**
	 * 根据ID查找记录
	 * 
	 * @param id
	 * @return
	 */
	public BpCStatReportItem findById(BpCStatReportItemId id);

	/**
	 * 根据统计报表名称编码查找记录
	 * 
	 * @param enterpriseCode
	 * @param reportCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String enterpriseCode, String reportCode,
			final int... rowStartIdxAndCount);
}