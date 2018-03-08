package power.ejb.productiontec.retrenchenergy;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 节能快报
 * 
 * @author fyyang 090707
 */
@Remote
public interface PtJnjdJJnkbFacadeRemote {
	
	/**
	 * 增加一条节能快报信息
	 * @param entity
	 * @return
	 */
	public PtJnjdJJnkb save(PtJnjdJJnkb entity);
	
	/**
	 * 修改一条节能快报信息
	 * @param entity
	 * @return
	 */
	public PtJnjdJJnkb update(PtJnjdJJnkb entity);
	
	/**
	 * 删除一条或多条节能快报信息
	 * @param ids
	 */
	public void deleteMulti(String ids);

	/**
	 * 查找一条节能快报信息
	 * @param id
	 * @return
	 */
	public PtJnjdJJnkb findById(Long id);
    /**
     * 查询节能快报信息列表
     * @param mainTopic
     * @param enterpriseCode
     * @param rowStartIdxAndCount
     * @return
     */
	public PageObject findAll(String mainTopic,String enterpriseCode,final int... rowStartIdxAndCount);
}