package power.ejb.manage.stat;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for BpCInputReportSetupFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpCInputReportSetupFacadeRemote {

	/**
	 * 批量保存所选择的时间类型信息
	 * 
	 * @param list
	 */
	public void save(List<BpCInputReportSetup> list);

	/**
	 * 判断是否更新时间类型
	 * 
	 * @param reportCode
	 * @param enterpriseCode
	 * @return 大于0 表示更新，不大于0 表示新增
	 */
	public Long ifUpdate(String reportCode, String enterpriseCode);

	/**
	 * 根据报表编码删除已有的时间类型数据
	 * 
	 * @param reportCode
	 */
	public void delete(String reportCode);

	/**
	 * 根据报表编码查询对应的报表时间类型信息
	 * 
	 * @param propertyName
	 *            对应的表的列名
	 * @param value
	 *            报表编码
	 * @return List 报表时间类型信息列表
	 */
	public List<BpCInputReportSetup> findByProperty(String propertyName,
			Object value);

}