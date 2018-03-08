package power.ejb.manage.stat;

import java.util.List;
import javax.ejb.Remote;

/**
 * 指标时间类型设置
 * 
 * @author ywliu
 * 
 */
@Remote
public interface BpCItemCollectSetupFacadeRemote {

	/**
	 * 批量保存所设置的时间
	 * 
	 * @param entity
	 */
	public void save(List<BpCItemCollectSetup> list);

	/**
	 * 变更时间类型时清空原时间类型数据
	 * 
	 * @param entity
	 * 
	 */
	public Boolean delete(String itemCode);

	/**
	 * 根据报表编码查询对应的报表时间类型信息
	 * 
	 * @param propertyName
	 *            对应的表的列名
	 * @param value
	 *            报表编码
	 * @return List 报表时间类型信息列表
	 */
	public List<BpCItemCollectSetup> findByProperty(String propertyName,
			Object valuet);

	/**
	 * 判断是否更新
	 * 
	 * @param itemCode
	 * @param enterpriseCode
	 * @return Long 大于0 表示更新，小于0表示新增
	 */
	public Long ifUpdate(String itemCode, String enterpriseCode);

}