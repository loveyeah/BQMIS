package power.ejb.manage.client;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 合作伙伴评价汇总
 * 
 * @author fyyang 090622
 */
@Remote
public interface ConJAppraiseFacadeRemote {
	
	public ConJAppraise save(ConJAppraise entity);
	
	public ConJAppraise update(ConJAppraise entity);

	public ConJAppraise findById(Long id);

	public ConJAppraise findModelByClientAndInterval(Long clientId,Long intervalId,String enterpriseCode);
	/**
	 * 根据合作伙伴ID，区间ID,企业编码查找列表
	 * @param clientId
	 * @param intervalId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 * add by drdu  090623
	 */
	public PageObject findAll(String clientId,String intervalId,String enterpriseCode, final int... rowStartIdxAndCount);
	
	/**
	 * 根据合作伙伴ID,企业编码查找列表
	 * @param clientId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findGatherAll(String clientId,String enterpriseCode, final int... rowStartIdxAndCount);
}