package power.ejb.productiontec.dependabilityAnalysis;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * 辅机编码维护
 * 
 * @author fyyang 091015
 */
@Remote
public interface PtKkxCFjcodeFacadeRemote {
	
	/**
	 * 增加一条辅机编码信息
	 * @param entity
	 * @return
	 * @throws CodeRepeatException
	 */
	public PtKkxCFjcode save(PtKkxCFjcode entity) throws CodeRepeatException;
	/**
	 * 修改一条辅机编码信息
	 * @param entity
	 * @return
	 */
	public PtKkxCFjcode update(PtKkxCFjcode entity);
	/**
	 * 删除一条或多条辅机编码信息
	 * @param fjCodes
	 */
	public void delete(String fjCodes);
   /**
    * 查找一条辅机编码信息
    * @param id
    * @return
    */
	public PtKkxCFjcode findById(String id);
	/**
	 * 查询辅机编码信息列表
	 * @param fjName
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String fjName,String enterpriseCode, final int... rowStartIdxAndCount);
}