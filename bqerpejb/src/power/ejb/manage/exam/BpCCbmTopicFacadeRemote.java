package power.ejb.manage.exam;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 考核指标主题
 * @author liuyi 091207
 */
@Remote
public interface BpCCbmTopicFacadeRemote {
	/**
	 * 新增一条考核指标主题数据
	 */
	public void save(BpCCbmTopic entity);

	/**
	 * 删除一条考核指标主题
	 */
	public void delete(BpCCbmTopic entity);

	/**
	 * 更新一条考核指标主题
	 */
	public BpCCbmTopic update(BpCCbmTopic entity);

	public BpCCbmTopic findById(Long id);

	public List<BpCCbmTopic> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	public List<BpCCbmTopic> findAll(int... rowStartIdxAndCount);
	
	public PageObject findAllTopic(String enterpriseCode,int... rowStartIdxAndCount);
	
	public void saveModifiedRec(List<BpCCbmTopic> addList,List<BpCCbmTopic> updateList,String ids);
}