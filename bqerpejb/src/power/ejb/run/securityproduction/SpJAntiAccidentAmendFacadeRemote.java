package power.ejb.run.securityproduction;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 25项反措整改计划表
 * @author liuyi 090917
 */
@Remote
public interface SpJAntiAccidentAmendFacadeRemote {
	/**
	 * 新增一条25项反措整改计划表记录
	 */
	public void save(SpJAntiAccidentAmend entity);

	/**
	 * 删除一条25项反措整改计划表记录
	 */
	public void delete(SpJAntiAccidentAmend entity);
	

	/**
	 * 删除一条或多条25项反措整改计划表记录
	 */
	public void delete(String ids);
	
	/**
	 * 更新一条25项反措整改计划表记录
	 */
	public SpJAntiAccidentAmend update(SpJAntiAccidentAmend entity);

	public SpJAntiAccidentAmend findById(Long id);

	

	
	public PageObject findAllCheckupPlan(Long checkupId,String enterpriseCode,int... rowStartIdxAndCount);
}