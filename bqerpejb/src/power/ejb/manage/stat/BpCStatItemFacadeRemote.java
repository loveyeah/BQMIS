package power.ejb.manage.stat;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.TreeNode;

/**
 * Remote interface for BpCStatItemFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpCStatItemFacadeRemote {

	public BpCStatItem save(BpCStatItem entity);

	public void delete(BpCStatItem entity);

	// /**
	// * 保存实体
	// * @param entity
	// * @return
	// */
	// public BpCStatItem update(BpCStatItem entity,Boolean isChange);

	public BpCStatItem update(BpCStatItem entity);

	// /**
	// *
	// * @param entity
	// * @param itemCode
	// * @param datetypeChange
	// * @param collectWayChange
	// * @param itemTypeChange
	// * @return
	// */
	// public BpCStatItem update(BpCStatItem entity, String itemCode,
	// boolean datetypeChange);

	public BpCStatItem findById(String id);

	public List<TreeNode> findStatTreeList(String itemCode,
			String enterpriseCode,String searchKey);

	public boolean isleaf(String itemCode);

	public String getItemcode(String name);

	public BpCStatItem setAccountOrder(BpCStatItem entity);

	public List<Object[]> getAllReferItem(String itemCode);

}