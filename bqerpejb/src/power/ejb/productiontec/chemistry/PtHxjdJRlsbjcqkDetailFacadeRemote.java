package power.ejb.productiontec.chemistry;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PtHxjdJRlsbjcqkDetailFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtHxjdJRlsbjcqkDetailFacadeRemote {
	/**
	 * 增加一条热力设备检查情况明细记录
	 * @return
	 */
	public void save(PtHxjdJRlsbjcqkDetail entity) ;
	
	/**
	 * 批量增加
	 * @param addList
	 */
	public void save(List<PtHxjdJRlsbjcqkDetail> addList);

	/**
	 * 批量删除数据
	 * @param ids 
	 * @return true 删除成功  false 删除不成功
	 */
	public boolean delete(String ids);
	
	/**
	 * 批量修改数据
	 * @param updateList
	 */
	public void update(List<PtHxjdJRlsbjcqkDetail> updateList);

	/**
	 * 修改一条热力设备检查情况明细记录
	 * @param entity
	 * @return
	 */
	public PtHxjdJRlsbjcqkDetail update(PtHxjdJRlsbjcqkDetail entity);

	/**
	 * 根据ID查找一条热力设备检查情况明细记录信息
	 * @param id
	 * @return
	 */
	public PtHxjdJRlsbjcqkDetail findById(Long id);

	/**
	 * 根据ID，企业编码显示列表
	 * @param rlsbjcId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findEquCheckDetailList(String rlsbjcId, String enterpriseCode,final int... rowStartIdxAndCount);
}