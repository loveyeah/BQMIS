package power.ejb.manage.client;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 合作伙伴评价记录
 * 
 * @author fyyang 090622
 */
@Remote
public interface ConJAppraiseRecordFacadeRemote {
	/**
	 * 增加或修改合作伙伴评价记录信息
	 * @param list
	 */
	public void saveRecord(List<ConJAppraiseRecord> list);
	
	/**
	 * 查找一条合作伙伴评价记录信息
	 * @param id
	 * @return
	 */
	public ConJAppraiseRecord findById(Long id);
	/**
	 * 获得评价总分
	 * @param clientId 合作伙伴ID
	 * @param intervalId 评价区间ID
	 * @param enterpriseCode
	 * @return
	 */
	public Double getTotalRecordScore(Long clientId,Long intervalId,String enterpriseCode);
	/**
	 * 查找合作伙伴评价记录列表
	 * @param clientId
	 * @param intervalId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String clientId,String intervalId,String enterpriseCode,final int... rowStartIdxAndCount);
}