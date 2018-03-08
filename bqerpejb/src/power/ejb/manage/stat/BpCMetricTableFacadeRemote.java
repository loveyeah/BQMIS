package power.ejb.manage.stat;

import java.util.List;
import javax.ejb.Remote;
import power.ear.comm.ejb.PageObject;

/**
 * 倍率管理
 * 
 * @author wzhyan
 */
@Remote
public interface BpCMetricTableFacadeRemote {

	/**
	 * 增加或者保存灵气
	 * 
	 * @param entity
	 */
	public void saveOrUpdate(BpCMetricTable entity);

	public void save(BpCMetricTable entity);

	public void save(List<BpCMetricTable> addList);

	public void delete(BpCMetricTable entity);

	public boolean delete(String ids);

	public boolean deleteMetric(String itemCode, String enterpriseCode);

	public BpCMetricTable update(BpCMetricTable entity);

	public void update(List<BpCMetricTableAdd> updateList);

	public BpCMetricTable findById(BpCMetricTableId id);

	public List<BpCMetricTable> findByProperty(String propertyName, Object value);

	public List<BpCStatItem> getItemListToUse(String enterpriseCode);

	public PageObject findAll(String enterpriseCode, int... rowStartIdxAndCount);
}