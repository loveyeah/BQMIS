package power.ejb.hr.archives;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCSocialRelationsFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCSocialRelationsFacadeRemote {

	/**
	 * 增加一条人员社会关系记录
	 * @param entity
	 * @return
	 */
	public HrCSocialRelations save(HrCSocialRelations entity);

	/**
	 * 删除一条或多条人员社会关系记录
	 * @param ids
	 */
	public void deleteMulti(String ids);

	/**
	 * 修改人员社会关系记录
	 * @param entity
	 * @return
	 */
	public HrCSocialRelations update(HrCSocialRelations entity);

	/**
	 * 根据ID查找一条人员社会关系详细
	 * @param id
	 * @return
	 */
	public HrCSocialRelations findById(Long id);

	/**
	 * 根据人员ID，企业编码查询列表
	 * @param empId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findSocialRelationList(String empId,String enterpriseCode,final int... rowStartIdxAndCount);
	
	/**
	 * 导入
	 * @param empList
	 */
	public void importSocialRelationFilesInfo(List<HrCSocialRelations> empList);
}