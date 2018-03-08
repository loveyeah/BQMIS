package power.ejb.manage.system;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for BpCMeasureUnitFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpCMeasureUnitFacadeRemote {

	/**
	 * 增加一条计量单位信息
	 * 
	 * @param entity
	 * @return
	 */
	public int save(BpCMeasureUnit entity);

	/**
	 * 删除一条计量单位信息
	 * 
	 * @param unitId
	 */
	public void delete(Long unitId);

	/**
	 * 修改一条计量单位信息
	 * 
	 * @param entity
	 * @return
	 */
	public boolean update(BpCMeasureUnit entity);

	/**
	 * 根据主键查询一条计量单位信息
	 * 
	 * @param id
	 * @return
	 */
	public BpCMeasureUnit findById(Long id);

	/**
	 * 查询所有的计量单位信息列表
	 * 
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public List<BpCMeasureUnit> findAll(int... rowStartIdxAndCount);

	/**
	 * 根据计量单位名称或检索码模糊查询
	 * 
	 * @param fuzzy
	 *            计量单位名称或检索码
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findUnitList(String fuzzy, String enterpriseCode,
			final int... rowStartIdxAndCount);

}