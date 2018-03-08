package power.ejb.hr.salary;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

@Remote
public interface HrCBigAwardFacadeRemote {
	
	/**
	 * 验证同一月份下是否已存在两天大奖记录
	 * @param awardMonth
	 * @return
	 */
	public boolean checkAwardName(String awardMonth);
	
	/**
	 * 增加一条大奖维护信息
	 * @param entity
	 */
	public void save(HrCBigAward entity);
	
	/**
	 * 删除一条大奖维护信息
	 * @param ids
	 */
	public void delete(String ids,String awardMonth) ;
	/**
	 * 修改一条大奖维护信息
	 * @param entity
	 * @return
	 */
	public HrCBigAward update(HrCBigAward entity);
	
	/**
	 * 通过主键查询大奖维护详细信息
	 * @param id
	 * @return
	 */
	public HrCBigAward findById(Long id);
	
	/**
	 * 大奖维护查询列表信息
	 * @param awardMonth
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 * @throws Exception
	 */
	public PageObject findBigAwardList(String awardMonth,String enterpriseCode,
			int... rowStartIdxAndCount) throws Exception;
}
