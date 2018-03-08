package power.ejb.equ.base;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquRBugFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquRBugFacadeRemote {
	
	/**
	 * 增加一条故障对应设备信息
	 * @param entity
	 */
	public void save(EquRBug entity);

	/**
	 * 删除一条故障对应设备信息
	 * @param id
	 */
	public void delete(Long id);
	
    /**
     * 修改一条故障对应设备信息
     * @param entity
     * @return
     */
	public EquRBug update(EquRBug entity);

	/**
	 * 根据id查询一条故障对应设备信息
	 * @param id
	 * @return
	 */
	public EquRBug findById(Long id);
	
	/**
	 * 查询所有的故障对应设备信息
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public List<EquRBug> findAll(int... rowStartIdxAndCount);
	
	/**
	 * 查询某个故障对应的设备
	 * @param bugCode
	 * @param enterprisecode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findEquRBugList(String bugCode,String enterprisecode,final int... rowStartIdxAndCount);
}