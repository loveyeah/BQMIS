package power.ejb.workticket;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunCMarkcardTypeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCMarkcardTypeFacadeRemote {

	/**
	 * 增加一条标识牌记录
	 * @param entity
	 * @return RunCMarkcardType 标识牌对象
	 */
	public RunCMarkcardType save(RunCMarkcardType entity) throws CodeRepeatException;
	
	/**
	 * 删除一条标识牌记录
	 * @param markcardTypeId 标识牌id
	 */
	public void delete(Long markcardTypeId) throws CodeRepeatException;
	/**
	 * 批量删除标识牌记录
	 * @param markcardTypeIds 标识牌id e.g:(1,2,3,...)
	 */
	public void deleteMulti(String markcardTypeIds);
	/**
	 * 修改一条标识牌记录
	 * @param entity
	 * @return RunCMarkcardType 标识牌对象
	 */
	public RunCMarkcardType update(RunCMarkcardType entity) throws CodeRepeatException;
	/**
	 * 查找一条标识牌记录
	 * @param id 标识牌id
	 * @return  RunCMarkcardType 标识牌对象
	 */
	public RunCMarkcardType findById(Long id);
     /**
      * 查询标识牌信息列表
      * @param enterpriseCode 企业编码
      * @param markcardTypeName 标识牌名称（模糊查询）
      * @param rowStartIdxAndCount 动态参数（开始行数和查询行数）
      * @return
      */
	public PageObject findAll(String enterpriseCode,String markcardTypeName, final int... rowStartIdxAndCount);
}