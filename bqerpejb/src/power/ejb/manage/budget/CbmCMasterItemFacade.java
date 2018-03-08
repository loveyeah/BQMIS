package power.ejb.manage.budget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.budget.form.DeptControlForm;

/**
 * 预算控制维护
 */
@Stateless
public class CbmCMasterItemFacade implements CbmCMasterItemFacadeRemote {
	// property constants
	public static final String CENTER_ID = "centerId";
	public static final String ITEM_ID = "itemId";
	public static final String ITEM_ALIAS = "itemAlias";
	public static final String MASTER_MODE = "masterMode";
	public static final String DATA_TYPE = "dataType";
	public static final String SYS_SOURCE = "sysSource";
	public static final String COME_FROM = "comeFrom";
	public static final String DISPLAY_NO = "displayNo";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 新增一条预算控制维护记录
	 */
	public void save(CbmCMasterItem entity) {
		LogUtil.log("saving CbmCMasterItem instance", Level.INFO, null);
		try {
			entity.setMasterId(bll.getMaxId("CBM_C_MASTER_ITEM", "MASTER_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条预算控制维护记录
	 */
	public void delete(CbmCMasterItem entity) {
		LogUtil.log("deleting CbmCMasterItem instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(CbmCMasterItem.class, entity
					.getMasterId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条或多条预算控制维护记录
	 */
	public void delete(String ids) {
		String sql = "update CBM_C_MASTER_ITEM a set a.is_use='N' "
				+ " where a.MASTER_ID in (" + ids + ")";
		bll.exeNativeSQL(sql);
	}

	/**
	 * 更新一条预算控制维护记录
	 */
	public CbmCMasterItem update(CbmCMasterItem entity) {
		LogUtil.log("updating CbmCMasterItem instance", Level.INFO, null);
		try {
			CbmCMasterItem result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过id查找一条预算控制维护记录
	 */
	public CbmCMasterItem findById(Long id) {
		LogUtil.log("finding CbmCMasterItem instance with id: " + id,
				Level.INFO, null);
		try {
			CbmCMasterItem instance = entityManager.find(CbmCMasterItem.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<CbmCMasterItem> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all CbmCMasterItem instances", Level.INFO, null);
		try {
			final String queryString = "select model from CbmCMasterItem model";
			Query query = entityManager.createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PageObject findAll(String centerId, String enterpriseCode,
			int... rowStartIdxAndCount) {
		try {
			PageObject pg = new PageObject();
			String sql = "select a.master_id, \n" + "a.center_id, \n"
					+ "a.item_id, \n" + "a.item_alias, \n"
					+ "a.master_mode, \n" + "a.data_type, \n"
					+ "a.sys_source, \n" + "a.come_from, \n"
					+ "a.display_no, \n" + "a.is_use, \n"
					+ "a.enterprise_code, \n" + "b.dep_code, \n"
					+ "b.dep_name, \n" + "c.item_code, \n" + "c.item_name \n"
					+ "from CBM_C_MASTER_ITEM a,CBM_C_CENTER b,CBM_C_ITEM c \n"
					+ "where a.center_id=b.center_id \n"
					+ "and a.item_id=c.item_id \n" + "and a.is_use='Y' \n"
					+ "and b.is_use='Y' \n" + "and c.is_use='Y' \n"
					+ "and a.enterprise_code='" + enterpriseCode + "' \n"
					+ "and b.enterprise_code='" + enterpriseCode + "' \n"
					+ "and c.enterprise_code='" + enterpriseCode + "' \n"
					+ "and b.if_duty='Y' \n";
			String sqlCount = "select count(*) \n"
					+ "from CBM_C_MASTER_ITEM a,CBM_C_CENTER b,CBM_C_ITEM c \n"
					+ "where a.center_id=b.center_id \n"
					+ "and a.item_id=c.item_id \n" + "and a.is_use='Y' \n"
					+ "and b.is_use='Y' \n" + "and c.is_use='Y' \n"
					+ "and a.enterprise_code='" + enterpriseCode + "' \n"
					+ "and b.enterprise_code='" + enterpriseCode + "' \n"
					+ "and c.enterprise_code='" + enterpriseCode + "' \n"
					+ "and b.if_duty='Y' \n";
			if (centerId != null && !(centerId.equals(""))) {
				sql = sql + "and a.center_id='" + centerId + "' \n";
				sqlCount = sqlCount + "and a.center_id='" + centerId + "' \n";
			}
			sql = sql + "order by a.display_no \n";

			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			if (list != null && list.size() > 0) {
				while (it.hasNext()) {
					CbmCMasterItem ccm = new CbmCMasterItem();
					DeptControlForm form = new DeptControlForm();

					Object[] data = (Object[]) it.next();
					ccm.setMasterId(Long.parseLong(data[0].toString()));
					if (data[1] != null)
						ccm.setCenterId(Long.parseLong(data[1].toString()));
					if (data[2] != null)
						ccm.setItemId(Long.parseLong(data[2].toString()));
					if (data[3] != null)
						ccm.setItemAlias(data[3].toString());
					if (data[4] != null)
						ccm.setMasterMode(data[4].toString());
					if (data[5] != null)
						ccm.setDataType(data[5].toString());
					if (data[6] != null)
						ccm.setSysSource(data[6].toString());
					if (data[7] != null)
						ccm.setComeFrom(data[7].toString());
					if (data[8] != null)
						ccm.setDisplayNo(Long.parseLong(data[8].toString()));
					if (data[9] != null)
						ccm.setIsUse(data[9].toString());
					if (data[10] != null)
						ccm.setEnterpriseCode(data[10].toString());
					if (data[11] != null)
						form.setDepCode(data[11].toString());
					if (data[12] != null)
						form.setDepName(data[12].toString());
					if (data[13] != null)
						form.setItemCode(data[13].toString());
					if (data[14] != null)
						form.setItemName(data[14].toString());
					form.setCcm(ccm);
					arrlist.add(form);
				}
			}
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			pg.setList(arrlist);
			pg.setTotalCount(totalCount);
			return pg;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void saveDeptControlInput(List<CbmCMasterItem> addList,
			List<CbmCMasterItem> updaList, String deleteIds) {
		if (addList.size() > 0) {
			for (CbmCMasterItem entity : addList) {
				this.save(entity);
				entityManager.flush();
			}
		}
		if (updaList.size() > 0) {
			for (CbmCMasterItem entity : updaList) {
				this.update(entity);
			}
		}
		if (deleteIds.length() > 0) {
			this.delete(deleteIds);
		}
	}

}