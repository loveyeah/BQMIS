package power.ejb.equ.base;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquCBugreasonFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCBugreasonFacadeRemote {

	/**
	 * 增加一条故障信息
	 * @param entity
	 * @return
	 */
	public int save(EquCBugreason entity);

    /**
     * 通过id删除一条故障原因信息
     * @param solutinId
     */
	public void delete(Long solutinId);
    /**
     * 修改一条故障原因信息
     * @param entity
     * @return
     */
	public boolean update(EquCBugreason entity);
   
	/**
	 * 通过id查找一条故障原因信息
	 * @param id
	 * @return
	 */
	public EquCBugreason findById(Long id);
    /**
     * 获得所有故障原因信息
     * @param rowStartIdxAndCount
     * @return
     */
	public List<EquCBugreason> findAll(int... rowStartIdxAndCount);
	/**
	 * 查询某故障的故障原因信息
	 * @param reasonDesc 故障原因（模糊查询）
	 * @param bugCode 故障编码
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findBugReasonList(String reasonDesc,String bugCode,String enterpriseCode,final int... rowStartIdxAndCount);
	/**
	 * 通过故障编码删除对应的所有故障原因
	 * @param bugCode
	 * @param enterpriseCode
	 */
	public void deleteReasonByBugCode(String bugCode,String enterpriseCode);
}