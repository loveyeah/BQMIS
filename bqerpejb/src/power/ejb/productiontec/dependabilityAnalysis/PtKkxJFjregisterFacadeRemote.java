package power.ejb.productiontec.dependabilityAnalysis;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 辅机数据录入
 * @author liuyi 091015
 */
@Remote
public interface PtKkxJFjregisterFacadeRemote {
	/**
	 * 新增一条辅机数据录入书数据
	 */
	public void save(PtKkxJFjregister entity);

	/**
	 * 删除一条辅机数据录入书数据
	 */
	public void delete(PtKkxJFjregister entity);

	/**
	 * 更新一条辅机数据录入书数据
	 */
	public PtKkxJFjregister update(PtKkxJFjregister entity);

	public PtKkxJFjregister findById(Long id);

	public List<PtKkxJFjregister> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);
	
	public List<PtKkxJFjregister> findAll(int... rowStartIdxAndCount);
	
	public PageObject getRecordList(String month,String enterpriseCode,int... rowStartIdxAndCount);
	
	public void modifyRec(List<PtKkxJFjregister> addList,List<PtKkxJFjregister> updateList,String ids);
}