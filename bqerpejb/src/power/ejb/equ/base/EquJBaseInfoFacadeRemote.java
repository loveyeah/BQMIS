package power.ejb.equ.base;

import java.util.List;
import javax.ejb.Remote;

@Remote
public interface EquJBaseInfoFacadeRemote {
	/**
	 * 保存设备基础信息
	 * @param entity
	 * @return EquJBaseAnnex
	 */
	public EquJBaseInfo save(EquJBaseInfo entity);
	/**
	 * 删除设备基础信息
	 * 
	 * @param ids
	 */
	public void delete(String ids);
	/**
	 * 更新设备基础信息
	 * @param entity
	 * @return EquJBaseInfo
	 */
	public EquJBaseInfo update(EquJBaseInfo entity);
	/**
	 * 查找设备基础信息附件信息
	 * @param id
	 * @return EquJBaseInfo
	 */
	public EquJBaseInfo findById(Long id);

	/**
	 * 查找设备基础信息
	 * By 设备编码
	 * @param AttributeCode
	 * @param enterprisecode
	 * @param rowStartIdxAndCount
	 * @return List
	 */
	public List findByAttributeCode(String AttributeCode, String enterprisecode);
}