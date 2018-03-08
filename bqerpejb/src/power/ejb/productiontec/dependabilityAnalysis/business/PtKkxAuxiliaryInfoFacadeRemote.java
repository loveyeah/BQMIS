package power.ejb.productiontec.dependabilityAnalysis.business;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 辅机基本信息
 * @author liuyi 091020
 */
@Remote
public interface PtKkxAuxiliaryInfoFacadeRemote {
	/**
	 * 新增一条辅机基本信息
	 */
	public void save(PtKkxAuxiliaryInfo entity);

	/**
	 * 删除一条辅机基本信息
	 */
	public void delete(PtKkxAuxiliaryInfo entity);
	public void delete(String ids);

	/**
	 * 更新一条辅机基本信息
	 */
	public PtKkxAuxiliaryInfo update(PtKkxAuxiliaryInfo entity);

	public PtKkxAuxiliaryInfo findById(Long id);

	
	public List<PtKkxAuxiliaryInfo> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	
	public List<PtKkxAuxiliaryInfo> findAll(int... rowStartIdxAndCount);
	
	public PageObject getAllAuxiliaryRec(Long blockId,Long typeId,String name,String enterpriseCode,
			int... rowStartIdxAndCount);
}