package power.ejb.resource;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for InvCTempMaterialFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface InvCTempMaterialFacadeRemote {
	
	/** 
	 * 增加
	 * @param entity
	 * @return
	 */
	public InvCTempMaterial save(InvCTempMaterial entity);

	/**
	 * 删除
	 * @param entity
	 */
	public void delete(Long id);

	/**
	 * 删除多条记录
	 * @param ids
	 */
	public void deleteMulti(String ids);
	/**
	 * 修改
	 * @param entity
	 * @return
	 */
	public InvCTempMaterial update(InvCTempMaterial entity);

	public InvCTempMaterial findById(Long id);
	
	public List<InvCTempMaterial> findAll(int... rowStartIdxAndCount);
	
	
	/**
	 * 查找所有列表信息
	 * @param strWhere
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findALLList(String strWhere,final int... rowStartIdxAndCount);
	
	/**
	 * modify by liuyi 091102 workerCode 登录人编码，过滤数据
	 * 根据物料名称、状态查找列表
	 * @param enterpriseCode
	 * @param materialName
	 * @param status
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findTempMaterialList(String workerCode,String enterpriseCode,String materialName,String status,final int... rowStartIdxAndCount);
	
	/**
	 * 已审核的列表
	 * @param enterpriseCode
	 * @param materialName
	 * status = 2
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findApprovelList(String enterpriseCode,String materialName,final int... rowStartIdxAndCount);
}