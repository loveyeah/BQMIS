package power.ejb.equ.base;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquCBugFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCBugFacadeRemote {
	
	/**
	 * 增加一条故障信息
	 * @param entity
	 * @return
	 */
	public int save(EquCBug entity);

	/**
	 * 删除一条故障信息
	 * @param bugId
	 * @return
	 */
	public boolean delete(Long bugId);

	/**
	 * 修改一条故障信息
	 * @param entity
	 * @return
	 */
	public boolean update(EquCBug entity);
	
	/**
	 * 通过id查找故障信息
	 * @param id
	 * @return
	 */
	public EquCBug findById(Long id);

    /**
     * 查询所有的故障信息
     * @param rowStartIdxAndCount
     * @return
     */
	public List<EquCBug> findAll(int... rowStartIdxAndCount);
	
	/**
	 * 通过父id查询故障信息列表
	 * @param bugId
	 * @param enterpriseCode
	 * @return
	 */
	public List<EquCBug> getListByParent(Long bugId,String enterpriseCode);
	
	/**
	 * 通过编码获得一条故障信息
	 * @param bugCode
	 * @param enterpriseCode
	 * @return
	 */
	public EquCBug findByCode(String bugCode,String enterpriseCode);
	
	/**
	 * 通过故障名称模糊查询故障列表
	 * @param enterpriseCode
	 * @param bugName
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findListByName(String enterpriseCode,String bugName,final int... rowStartIdxAndCount);
}