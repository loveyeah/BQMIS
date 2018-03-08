package power.ejb.manage.stat;

import java.text.ParseException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.stat.form.InputReprotItemForm;

/**
 * Remote interface for BpCInputReportFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpCInputReportFacadeRemote {

	/**
	 * 批量增加录入报表数据
	 * 
	 * @param addList
	 *            报表数据
	 */
	public void save(List<BpCInputReport> addList);

	/**
	 * 批量删除录入报表数据
	 * 
	 * @param ids
	 * @return true 删除成功 false 删除不成功
	 */
	public boolean delete(String ids);

	/**
	 * 批量更新报表数据
	 * 
	 * @param updateList
	 *            报表数据
	 */
	public void update(List<BpCInputReport> updateList);

	/**
	 * 通过报表编码查询对应的报表信息
	 * 
	 * @param id
	 *            报表编码
	 * @return BpCInputReport 报表信息
	 */
	public BpCInputReport findById(Long id);

	/**
	 * 查询录入报表名称记录
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param rowStartIdxAndCount
	 *            查询最大行数
	 * @return
	 */
	public PageObject findAll(String enterpriseCode,String type,String workerCode, int... rowStartIdxAndCount);

	/**
	 * 判断报表名称是否重复
	 * 
	 * @param reportName
	 *            报表名称
	 * @return Long类型 大于0 表示名称重复 不大于0 表示名称不重复
	 */
	public Long checkReportName(String reportName);
	
	/**
	 * 班组录入报表查询
	 * @param date
	 * @param reportCode
	 * @return
	 */
	public PageObject findGroupReportItem(String date,String reportCode,int... rowStartIdxAndCount);
	
	/**
	 * 录入保存至分组指标表
	 * @param updateList
	 */
	public void saveGroupReportValue(List<InputReprotItemForm> updateList) throws ParseException;
}