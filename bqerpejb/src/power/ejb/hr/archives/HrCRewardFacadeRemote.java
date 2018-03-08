package power.ejb.hr.archives;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCRewardFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
/**
 * @author code_cj
 *
 */
@Remote
public interface HrCRewardFacadeRemote {
	
	/**查询所有奖励情况信息
	 * @param enterPriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public  PageObject  getRewardList(String empID,String enterPriseCode,int...rowStartIdxAndCount);
	
	/**保存奖励情况记录
	 * @param entity
	 */
	public void save(HrCReward entity); 
	
	/**删除一条记录
	 * @param entity
	 */
	public void delete(HrCReward entity);

	
	/**修改奖励记录
	 * @param entity
	 * @return
	 */
	public HrCReward update(HrCReward entity);

	/**通过id找到一条奖励情况记录
	 * @param id
	 * @return
	 */
	public HrCReward findById(Long id);
	
	/**批量导入excel表格中的记录到奖励情况表中
	 * @param addOrUpdateList
	 */
	public  void saveOrUpdateReward(List<HrCReward> addOrUpdateList);
	
	/**删除多条奖励情况记录
	 * @param ids
	 */
	public void  delReward(String ids);
	public Long getEmpIdByNewCode(String empNewCode, String enterpriseCode);

	
}