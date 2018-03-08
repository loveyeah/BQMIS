package power.ejb.equ.base;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquCBlockFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCBlockFacadeRemote {
	/**
	 * 保存机组信息
	 * @param entity       
	 */
	public int save(EquCBlock entity);

	/**
	 * 删除机组信息
	 * @param entity       
	 */
	public void delete(EquCBlock entity);

	/**
	 * 修改机组信息
	 * @param entity   
	 * @return EquCBlock   
	 */
	public EquCBlock update(EquCBlock entity);

	
	/**
	 * 由主键查找
	 * @param id
	 * @return EquCBlock
	 */
	public EquCBlock findById(Long id);



	/**
	 * 获得所有的机组信息列表
	 * @param rowStartIdxAndCount
	 * @return List<EquCBlock> all EquCBlock entities
	 */
	public List<EquCBlock> findAll(int... rowStartIdxAndCount);
	
	
	/**
	 * 根据编码或名称查询机组信息列表
	 * @param fuzzy  编码或名称
	 * @param enterpriseCode  企业编码
	 * @param rowStartIdxAndCount 动态参数（开始行数和查询行数）
	 * @return
	 */
	public PageObject findEquList(String fuzzy,String enterpriseCode,final int... rowStartIdxAndCount);
	
	/**
	 * 判断机组编码是否重复
	 * @param enterpriseCode
	 * @param equCode
	 * @param equid
	 * @return
	 */
	public boolean CheckBlockCodeSame(String enterpriseCode,String equCode,Long... equid);
	}