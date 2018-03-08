package power.ejb.manage.client;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * 合作伙伴重要程度维护
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface ConCClientsImportanceFacadeRemote {
	
	/**
	 * 增加一条合作伙伴重要程度信息
	 * @param entity
	 * @return
	 * @throws CodeRepeatException
	 */
	public ConCClientsImportance save(ConCClientsImportance entity) throws CodeRepeatException;
	
	/**
	 * 删除一条或多条合作伙伴重要程度信息
	 * @param ids
	 */
	public void deleteMulti(String ids);
	
	/**
	 * 修改一条合作伙伴重要程度信息
	 * @param entity
	 * @return
	 * @throws CodeRepeatException
	 */
	public ConCClientsImportance update(ConCClientsImportance entity) throws CodeRepeatException;

	/**
	 * 通过id查找一条合作伙伴重要程度信息
	 * @param id
	 * @return
	 */
	public ConCClientsImportance findById(Long id);


/**
 * 查询合作伙伴重要程度信息列表
 * @param importanceName 重要程度描述
 * @param enterpriseCode 企业编码
 * @param rowStartIdxAndCount
 * @return
 */
	public PageObject findAll(String importanceName,String enterpriseCode,int... rowStartIdxAndCount);
}