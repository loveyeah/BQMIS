package power.ejb.manage.client;

import java.text.ParseException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * 评价周期设置
 * 
 * @author fyyang 090615
 */
@Remote
public interface ConCIntervalFacadeRemote {

	/**
	 * 增加一条评价周期设置信息
	 * @param entity
	 * @return
	 * @throws ParseException
	 * @throws CodeRepeatException
	 */
	public ConCInterval save(ConCInterval entity) throws ParseException, CodeRepeatException;
	
	/**
	 * 修改一条评价周期设置信息
	 * @param entity
	 * @return
	 * @throws ParseException
	 * @throws CodeRepeatException
	 */
	public ConCInterval update(ConCInterval entity) throws ParseException, CodeRepeatException;

	/**
	 * 删除一条或多条评价周期设置信息
	 * @param ids
	 */
	public void deleteMulti(String ids);
	
	/**
	 * 查找一条评价周期设置信息
	 * @param id
	 * @return
	 */
	public ConCInterval findById(Long id);
	
	/**
	 * 查询评价周期设置信息列表
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String enterpriseCode,int... rowStartIdxAndCount);
	
}