package power.ejb.productiontec.insulation;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PtJyjdJQxdjFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtJyjdJQxdjFacadeRemote {

	/**
	 *  增加一条绝缘缺陷信息
	 * @param entity
	 */
	public PtJyjdJQxdj save(PtJyjdJQxdj entity);

	/**
	 * 删除一条或多条绝缘缺陷记录
	 * @param ids
	 */
	public void deleteMulti(String ids);

	/**
	 * 修改一条绝缘缺陷记录信息
	 * @param entity
	 * @return
	 */
	public PtJyjdJQxdj update(PtJyjdJQxdj entity);

	/**
	 * 通过ID查找一条绝缘缺陷信息
	 * @param id
	 * @return
	 */
	public PtJyjdJQxdj findById(Long id);

	/**
	 * 查找绝缘缺陷列表
	 * @param enterpriseCode
	 * @param equName
	 * @param sDate
	 * @param eDate
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findQxdjList(String enterpriseCode, String equName,
			String sDate, String eDate, final int... rowStartIdxAndCount) ;
}