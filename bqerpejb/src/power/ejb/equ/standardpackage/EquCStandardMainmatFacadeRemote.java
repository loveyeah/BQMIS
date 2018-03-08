package power.ejb.equ.standardpackage;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 
 */
@Remote
public interface EquCStandardMainmatFacadeRemote {
	/**
	 * 增加
	 */
	public void save(EquCStandardMainmat entity);

	/**
	 * 批量增加
	 */
	public void saveEquCStandardMainmat(List<EquCStandardMainmat> addList);

	/**
	 * 删除
	 */
	public void delete(EquCStandardMainmat entity);
	
	public boolean deleteMainmat(String ids);

	/**
	 * 修改
	 */
	public EquCStandardMainmat update(EquCStandardMainmat entity);
	
	public void update(List<EquCStandardMainmat> updateList);
	/**
	 * 
	 * @param id
	 * @return
	 */
	public EquCStandardMainmat findById(Long id);

	/**
	 * 
	 */
	public List<EquCStandardMainmat> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	
	
	public PageObject findAllmainmat(String enterpriseCode, String woCode,
			String operationStep, int... rowStartIdxAndCount);
}