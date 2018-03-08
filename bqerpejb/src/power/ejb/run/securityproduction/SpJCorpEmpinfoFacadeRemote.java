package power.ejb.run.securityproduction;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 发电企业人员表
 * @author liuyi 090917
 */
@Remote
public interface SpJCorpEmpinfoFacadeRemote {
	/**
	 *新增一条发电企业人员
	 */
	public void save(SpJCorpEmpinfo entity);

	/**
	 * 删除一条发电企业人员
	 */
	public void delete(SpJCorpEmpinfo entity);
	
	/**
	 * 删除一条或多条发电企业人员
	 * @param ids
	 */
	public void delete(String ids);

	/**
	 *更新一条发电企业人员
	 */
	public SpJCorpEmpinfo update(SpJCorpEmpinfo entity);

	public SpJCorpEmpinfo findById(Long id);

	

	
	
	public PageObject findEmpInfoList(String name,String enterpriseCode,int... rowStartIdxAndCount);
}