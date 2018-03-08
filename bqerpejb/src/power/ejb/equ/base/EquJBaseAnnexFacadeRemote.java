package power.ejb.equ.base;

import javax.ejb.Remote;
import power.ear.comm.ejb.PageObject;

@Remote
public interface EquJBaseAnnexFacadeRemote {
	/**
	 * 保存设备基础信息附件信息
	 * @param entity
	 * @return EquJBaseAnnex
	 */
	public EquJBaseAnnex save(EquJBaseAnnex entity);
	/**
	 * 删除设备基础信息附件信息
	 * 
	 * @param ids
	 */
	public void delete(String ids);
	/**
	 * 更新设备基础信息附件信息
	 * @param entity
	 * @return EquJBaseAnnex
	 */
	public EquJBaseAnnex update(EquJBaseAnnex entity);
	/**
	 * 查找设备基础信息附件信息
	 * @param id
	 * @return EquJBaseAnnex
	 */
	public EquJBaseAnnex findById(Long id);
	/**
	 * 查找设备基础信息附件信息
	 * By 设备基础信息Id
	 * @param EquBaseId 设备基础信息Id
	 * @param enterprisecode 企业编码
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 */
	public PageObject findByBaseId(String EquBaseId,
			String enterprisecode, int... rowStartIdxAndCount);
}