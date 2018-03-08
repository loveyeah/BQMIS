package power.ejb.manage.stat;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.stat.form.StatItemFrom;

/**
 * Remote interface for BpCStatItemRealtimeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpCStatItemRealtimeFacadeRemote {

	/**
	 * 添加指标编码与节点相互对应的信息
	 * 
	 * @param entity
	 * @return
	 */
	public BpCStatItemRealtime save(BpCStatItemRealtime entity);

	/**
	 * 删除指标编码与节点相互对应的信息
	 * 
	 * @param entity
	 * @return
	 */
	public BpCStatItemRealtime delete(BpCStatItemRealtime entity);

	/**
	 * 删除指标编码与节点相互对应的信息
	 * 
	 * @param itemCode
	 * @return
	 */
	public Boolean deleteByItemCode(String itemCode);

	public BpCStatItemRealtime update(BpCStatItemRealtime entity);

	public BpCStatItemRealtime findById(String id);

	public List<BpCStatItemRealtime> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	public List<BpCStatItemRealtime> findByNodeCode(Object nodeCode,
			int... rowStartIdxAndCount);

	public List<BpCStatItemRealtime> findByApartCode(Object apartCode,
			int... rowStartIdxAndCount);

	public List<BpCStatItemRealtime> findByUsedefault(Object usedefault,
			int... rowStartIdxAndCount);

	public List<BpCStatItemRealtime> findByDefaultValue(Object defaultValue,
			int... rowStartIdxAndCount);

	public List<BpCStatItemRealtime> findByEnterpriseCode(
			Object enterpriseCode, int... rowStartIdxAndCount);

	public List<BpCStatItemRealtime> findAll(int... rowStartIdxAndCount);

	/**
	 * 查询指标对应数据
	 * 
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findStatItemForCorrespond(String argFuzzy,
			int... rowStartIdxAndCount);

	// /**
	// * 查询节点对应数据
	// * @param rowStartIdxAndCount
	// * @return
	// */
	// public PageObject findDcsNodeForCorrespond(int... rowStartIdxAndCount);
	/**
	 * 查询指标对应的采集点信息
	 * 
	 * @param itemCode
	 * @return
	 */
	public StatItemFrom findItemCorrespondInfo(String itemCode);

}