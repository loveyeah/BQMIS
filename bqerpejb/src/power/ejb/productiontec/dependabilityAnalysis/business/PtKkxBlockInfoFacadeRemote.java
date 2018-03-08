package power.ejb.productiontec.dependabilityAnalysis.business;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PtKkxBlockInfoFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtKkxBlockInfoFacadeRemote {

	/**
	 * 增加一条机组记录
	 * @param entity
	 * @return
	 */
	public PtKkxBlockInfo save(PtKkxBlockInfo entity) throws CodeRepeatException;

	/**
	 * 删除一条或多条机组记录
	 * @param ids
	 */
	 public void deleteMulti(String ids);

	 /**
	  * 修改一条机组记录信息
	  * @param entity
	  * @return
	  */
	public PtKkxBlockInfo update(PtKkxBlockInfo entity) throws CodeRepeatException;

	/**
	 * 通过ID查找一条机组记录详细信息
	 * @param id
	 * @return
	 */
	public PtKkxBlockInfo findById(Long id);

	/**
	 * 根据企业编码，机组名称查找列表记录
	 * @param enterpriseCode
	 * @param blockName
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findBlockList(String enterpriseCode,String blockName,final int... rowStartIdxAndCount) ;
}