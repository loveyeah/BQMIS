package power.ejb.equ.base;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquCClassFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCClassFacadeRemote {
	/**
	 * 增加编码信息
	 * @param entity
	 * @throws RuntimeException
	 */
	public int save(EquCClass entity);

	/**
	 * 删除编码信息
	 * @param classId
	 */
	public void delete(Long classId);

	/**
	 * 修改编码信息
	 * @param entity
	 * @return true 成功 false:编码重复
	 */
	public boolean update(EquCClass entity);

	/**
	 * 根据id查找编码记录
	 * @param id
	 * @return EquCClass
	 */
	public EquCClass findById(Long id);


	/**
	 * 根据编码或名称查询所有信息列表
	 * @param fuzzy 编码或名称
	 * @param classLevel 编码标识 1：系统,2:设备,3:部件
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态参数（开始行数和查询行数）
	 * @return
	 */
	public PageObject findClassList(String fuzzy,String classLevel, String enterpriseCode,final int... rowStartIdxAndCount);
	
	
	/**
	 * 查询所有信息列表
	 * @param fuzzy 编码或名称
	 * @param code 编码的前面部分
	 * @param lenght 编码长度
	 * @param classLevel 编码标识 1：系统,2:设备,3:部件
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount 动态参数（开始行数和查询行数）
	 * @return
	 */
	public PageObject getClassListForSelect(String fuzzy,String code,int length,String classLevel,String enterpriseCode,final int... rowStartIdxAndCount);

	/**
	 * 获得所有编码信息列表
	 * 
	 * @param rowStartIdxAndCount
	 * @return List<EquCClass> 
	 */
	public List<EquCClass> findAll(int... rowStartIdxAndCount);
	}