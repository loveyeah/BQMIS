package power.ejb.equ.change;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquCChangesourceFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCChangesourceFacadeRemote {
	 
	/**
	 * 增加一条异动来源类型信息
	 * @param entity
	 * @return
	 */
	public int save(EquCChangesource entity);

	/**
	 * 删除一条异动来源类型信息
	 * @param sourceId
	 */
	public void delete(Long sourceId);

	/**
	 * 修改一条异动来源类型信息
	 * @param entity
	 * @return
	 */
	public boolean update(EquCChangesource entity);
    /**
     * 通过id查找一条异动来源类型信息
     * @param id
     * @return
     */
	public EquCChangesource findById(Long id);
	
	/**
	 * 通过编码或名称模糊查询异动来源类型信息列表
	 * @param fuzzy
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findChangeSourceList(String fuzzy,String enterpriseCode,final int... rowStartIdxAndCount);

	
	
}