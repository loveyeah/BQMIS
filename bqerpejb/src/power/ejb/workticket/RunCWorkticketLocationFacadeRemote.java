package power.ejb.workticket;

import java.text.ParseException;
import javax.ejb.Remote;
import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorkticketLocation;

/**
 * 工作票区域维护
 */
@Remote
public interface RunCWorkticketLocationFacadeRemote {
	/**
	 * 增加一条工作票区域记录
	 * 
	 * @param entity
	 *            工作票区域对象
	 * @return RunCWorkticketLocation
	 * @throws CodeRepeatException;
	 */
	public RunCWorkticketLocation save(RunCWorkticketLocation entity)
			throws CodeRepeatException;

	/**
	 * 删除一条工作票区域记录
	 * 
	 * @param locationId
	 *            工作票区域Id
	 */
	public void delete(Long locationId) throws CodeRepeatException;

	/**
	 * 批量删除工作票区域记录
	 * 
	 * @param locationIds
	 *            安措id，e.g:1,2,...
	 */
	public void deleteMulti(String locationIds);

	/**
	 * 修改一条工作票区域记录
	 * 
	 * @param entity
	 *            工作票区域对象
	 * @return RunCWorkticketLocation
	 * @throws CodeRepeatException
	 */

	public RunCWorkticketLocation update(RunCWorkticketLocation entity)
			throws CodeRepeatException;

	/**
	 * 查找一条工作票区域记录
	 * 
	 * @param id
	 *            工作票id
	 * @return entity 工作票记录对象
	 */
	public RunCWorkticketLocation findById(Long id);

	/**
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param blockCode
	 *            所属系统
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String enterpriseCode, String blockCode,
			String fuzzy, final int... rowStartIdxAndCount)
			throws ParseException;
}