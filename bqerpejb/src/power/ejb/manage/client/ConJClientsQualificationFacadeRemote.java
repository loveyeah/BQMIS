package power.ejb.manage.client;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.client.form.ConJClientsQualificationForm;

/**
 * 合作伙伴资质登记
 * 
 * @author drdu
 */
@Remote
public interface ConJClientsQualificationFacadeRemote {
	
	/**
	 * 增加合作伙伴资质登记记录
	 * @param entity
	 * @return
	 * @throws CodeRepeatException
	 */
	public ConJClientsQualification save(ConJClientsQualification entity) throws CodeRepeatException;

	/**
	 * 删除一条或多条合作伙伴资质登记记录
	 * @param ids
	 */
	public void deleteMulti(String ids);

	
	/**
	 * 修改合作伙伴资质登记记录
	 * @param entity
	 * @return
	 * @throws CodeRepeatException
	 */
	public ConJClientsQualification update(ConJClientsQualification entity) throws CodeRepeatException;

	/**
	 * 根据ID查找合作伙伴资质登记信息
	 * @param id
	 * @return
	 */
	public ConJClientsQualification findById(Long id);

	/**
	 * 根据资质名称，合作伙伴名称,合作伙伴ID查询列表
	 * @param enterpriseCode
	 * @param fuzzy
	 * @param clientId
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findClientsQualificationList(String enterpriseCode,
			String fuzzy,String clientId, final int... rowStartIdxAndCount);
	
	/**
	 * 根据ID，企业编码查找记录
	 * @param qualificationId
	 * @param enterpriseCode
	 * @return
	 */
	public ConJClientsQualificationForm findQualificationById(Long qualificationId,String enterpriseCode);
}