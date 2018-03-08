package power.ejb.productiontec.insulation;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PtJyjdJSgdjFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtJyjdJSgdjFacadeRemote {

	/**
	 * 增加一条绝缘事故信息
	 * @param entity
	 * @return
	 */
	public PtJyjdJSgdj save(PtJyjdJSgdj entity);

	/**
	 * 删除一条或多条绝缘事故记录
	 * @param ids
	 */
	public void deleteMulti(String ids);

	/**
	 * 修改一条绝缘事故信息
	 * @param entity
	 * @return
	 */
	public PtJyjdJSgdj update(PtJyjdJSgdj entity);

	public PtJyjdJSgdj findById(Long id);
	/**
	 * 查找绝缘事故列表信息
	 * @param enterpriseCode
	 * @param equName
	 * @param sDate
	 * @param eDate
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAllList(String enterpriseCode,String equName,String sDate,String eDate,final int... rowStartIdxAndCount);
}