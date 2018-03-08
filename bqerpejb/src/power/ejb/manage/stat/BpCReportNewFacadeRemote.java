package power.ejb.manage.stat;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for BpCReportNewFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpCReportNewFacadeRemote {

	/**
	 * 批量增加录入报表数据
	 * 
	 * @param addList
	 *            报表数据
	 */
	public void save(List<BpCReportNew> addList);

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
	public void update(List<BpCReportNew> updateList);

	/**
	 * 通过报表编码查询对应的报表信息
	 * 
	 * @param id
	 *            报表编码
	 * @return BpCReportNew 报表信息
	 */
	public BpCReportNew findById(Long id);

	/**
	 * 查询录入报表名称记录
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param rowStartIdxAndCount
	 *            查询最大行数
	 * @return
	 */
	public PageObject findAll(String reportType,String enterpriseCode,String workerCode, int... rowStartIdxAndCount);

	
	
	/**查询报表名称为36,37,38,39 的报表名称
	 * @param reportType
	 * @param reportCode
	 * @param enterpriseCode
	 * @param workerCode
	 * @return
	 */
	public PageObject findreportName(String reportType,String reportCode,String enterpriseCode,String workerCode,int... rowStartIdxAndCount);//add by wpzhu 20100830
	
	/**
	 * 判断报表名称是否重复
	 * 
	 * @param reportName
	 *            报表名称
	 * @return Long类型 大于0 表示名称重复 不大于0 表示名称不重复
	 */
	public Long checkReportName(String reportName);
	/**
	 * 燃料运行班上煤量汇总查询 
	 * @param date 月度
	 * @param enterpriseCode
	 * @return PageObject
	 * add by kzhang 20100906
	 */
	public PageObject finReportListByMon(String date,String enterpriseCode);
}