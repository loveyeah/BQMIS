package power.ejb.run.securityproduction;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 25项反措动态检查表
 * @author liuyi 090917
 */
@Remote
public interface SpJAntiAccidentCheckupFacadeRemote {
	/**
	 * 新增一条25项反措动态检查表记录
	 */
	public SpJAntiAccidentCheckup save(SpJAntiAccidentCheckup entity);

	/**
	 * 删除一条25项反措动态检查表记录
	 */
	public void delete(SpJAntiAccidentCheckup entity);
	
	/**
	 * 删除一条或多条25项反措动态检查表记录
	 */
	public void delete(String ids);

	/**
	 * 更新一条25项反措动态检查表记录
	 */
	public SpJAntiAccidentCheckup update(SpJAntiAccidentCheckup entity);

	public SpJAntiAccidentCheckup findById(Long id);


	/**
	 * Find all SpJAntiAccidentCheckup entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<SpJAntiAccidentCheckup> all SpJAntiAccidentCheckup entities
	 */
	public List<SpJAntiAccidentCheckup> findAll(int... rowStartIdxAndCount);
	
	public PageObject getCheckupList(String status,String time,String measureCode,String specialCode,String checkBy,String enterpriseCode,int... rowStartIdxAndCount);
	
}