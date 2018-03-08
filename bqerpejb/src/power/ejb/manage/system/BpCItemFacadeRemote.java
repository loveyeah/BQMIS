package power.ejb.manage.system;

import java.util.List;

import javax.ejb.Remote;

import power.ejb.comm.TreeNode;

/**
 * Remote interface for BpCItemFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpCItemFacadeRemote {

	/**
	 * 增加
	 * 
	 * @param entity
	 * @return
	 */
	public String save(BpCItem entity);

	/**
	 * 删除
	 * 
	 * @param entity
	 */
	public void delete(BpCItem entity);

	/**
	 * 修改
	 * 
	 * @param entity
	 * @return
	 */
	public BpCItem update(BpCItem entity);

	public BpCItem findById(String id);

	public List<BpCItem> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	public List<BpCItem> findAll(int... rowStartIdxAndCount);

	/**
	 * 查询指标列表
	 * 
	 * @param node
	 * @param enterpriseCode
	 * @return
	 */
	public List<TreeNode> findItemTreeList(String node, String enterpriseCode,String searchKey);

	/**
	 * 判断是否叶子节点
	 * 
	 * @param node
	 * @return
	 */
	public boolean isLeaf(String node);

	/**
	 * 根据编码查询指标详细信息
	 * 
	 * @param node
	 * @return
	 */
	public Object findItemInfo(String node);

	/**
	 * 
	 * @param name
	 * @return
	 */
	public String findRetrieveCode(String name);

	/**
	 * 判断指标时候存在 add bjxu
	 * 
	 * @param itemCode
	 * @return
	 */
	public boolean checkItem(String itemCode);

}