package power.ejb.run.runlog;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunCRunlogParmFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCRunlogParmFacadeRemote {

	/**
	 * 增加一条运行参数信息
	 * @param entity
	 */
	public Long save(RunCRunlogParm entity);

    /**
     * 删除一条运行参数信息
     * @param id
     */
	public void delete(Long id);
	
	/**
	 * 修改一条运行参数信息
	 * @param entity
	 * @return
	 */
	public RunCRunlogParm update(RunCRunlogParm entity);

	/**
	 * 通过id查找一条运行参数信息
	 * @param id
	 * @return
	 */
	public RunCRunlogParm findById(Long id);

   /**
    * 查询所有的运行参数信息列表
    * @param rowStartIdxAndCount
    * @return
    */
	public List<RunCRunlogParm> findAll(int... rowStartIdxAndCount);

	/**
	 * 根据指标名称或编码及专业编码查询运行参数信息列表
	 * @param itemCodeOrName
	 * @param specialCode
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findParmList(String itemCodeOrName,String specialCode, String enterpriseCode,final int... rowStartIdxAndCount);
	/**
	 * 根据专业查询运行参数列表
	 * @author fish
	 * @param specialcode
	 * @param enterprisecode
	 * @return
	 */
	public List<RunCRunlogParm> findListBySpecial(String specialcode,String enterprisecode);
}