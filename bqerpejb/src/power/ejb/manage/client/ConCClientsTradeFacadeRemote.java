package power.ejb.manage.client;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * 行业字典维护
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface ConCClientsTradeFacadeRemote {
	
	/**
	 * 增加一条行业字典信息
	 * @param entity
	 * @return
	 * @throws CodeRepeatException
	 */
	public ConCClientsTrade save(ConCClientsTrade entity) throws CodeRepeatException;
	
	/**
	 * 修改一条行业字典信息
	 * @param entity
	 * @return
	 * @throws CodeRepeatException
	 */
	public ConCClientsTrade update(ConCClientsTrade entity) throws CodeRepeatException;
	/**
	 * 删除一条或多条行业字典信息
	 * @param ids
	 */
	public void deleteMulti(String ids);

	/**
	 * 根据id查找一条行业字典信息
	 * @param id
	 * @return
	 */
	public ConCClientsTrade findById(Long id);
	
	/**
	 * 查询行业字典信息列表
	 * @param tradeName 行业名称
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String tradeName,String enterpriseCode,int... rowStartIdxAndCount);
}