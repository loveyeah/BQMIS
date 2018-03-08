package power.ejb.manage.stat;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for BpCItemReportNewFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpCItemReportNewFacadeRemote {

	/**
	 * 增加
	 * 
	 * @param entity
	 */
	public void save(BpCItemReportNew entity);

	public void save(List<BpCItemReportNew> addList);

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
	public BpCItemReportNew update(BpCItemReportNew entity);

	public void update(List<BpCItemReportNew> updateList);

	/**
	 * 根据ID查找记录
	 * 
	 * @param id
	 * @return
	 */
	public BpCItemReportNew findById(Long id);

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