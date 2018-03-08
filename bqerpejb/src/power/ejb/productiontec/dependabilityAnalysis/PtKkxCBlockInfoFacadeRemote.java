package power.ejb.productiontec.dependabilityAnalysis;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PtKkxCBlockInfoFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtKkxCBlockInfoFacadeRemote {
	/**
	 *机组信息保存 
	 */
	public void save(PtKkxCBlockInfo entity);
	
	public void delete(PtKkxCBlockInfo entity);

	public PtKkxCBlockInfo update(PtKkxCBlockInfo entity);
/**
 * 取对应信息
 * @param id
 * @return
 */
	public PtKkxCBlockInfo findById(Long id);
/**
 * 机组信息
 * @param enterpriseCode 
 * @param rowStartIdxAndCount
 * @return
 */
	public PageObject findAll(String enterpriseCode,int... rowStartIdxAndCount);
	/**
	 * 机组信息
	 * @param addList 新增机组
	 * @param updateList 修改机组
	 * @param deleteId 删除机组
	 */
	public void save(List<PtKkxCBlockInfo> addList, List<PtKkxCBlockInfo> updateList,
			String deleteId);
}