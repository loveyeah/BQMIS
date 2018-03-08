package power.ejb.manage.exam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.exam.form.BpCCbmDepForm;
import power.ejb.manage.exam.form.BpJCbmDepSeasonValue;
import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;

/**
 * Facade for entity BpJCbmDepSeason.
 * 
 * @see power.ejb.manage.exam.BpJCbmDepSeason
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpJCbmDepSeasonFacade implements BpJCbmDepSeasonFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "BpCMeasureUnitFacade")
	protected BpCMeasureUnitFacadeRemote unitRemote;

	 
	public void save(BpJCbmDepSeason entity) {
		try {
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	} 
	public void delete(BpJCbmDepSeason entity) {
		LogUtil.log("deleting BpJCbmDepSeason instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpJCbmDepSeason.class, entity
					.getSeasonId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	 
	public BpJCbmDepSeason update(BpJCbmDepSeason entity) {
		try {
			BpJCbmDepSeason result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpJCbmDepSeason findById(Long id) {
		LogUtil.log("finding BpJCbmDepSeason instance with id: " + id,
				Level.INFO, null);
		try {
			BpJCbmDepSeason instance = entityManager.find(
					BpJCbmDepSeason.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

 

	@SuppressWarnings("unchecked")
	public boolean saveDataInputList(List<BpJCbmDepSeason> addlist,
			List<BpJCbmDepSeason> updatelist, String enterpriseCode) {
		try {
			long id = bll.getMaxId("bp_j_cbm_dep_season", "SEASON_ID");
			long i = -1;
			for (BpJCbmDepSeason model : addlist) {
				i++;
				model.setSeasonId(id + i);
				model.setEnterpriseCode(enterpriseCode);
				this.save(model);
			}
			for (BpJCbmDepSeason model : updatelist) {
				this.update(model);
			}
			return true;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public BpJCbmDepSeasonValue getDataInputList(String deptId,
			String overheadId, String yearStr, String enterpriseCode) {
		BpJCbmDepSeasonValue model = new BpJCbmDepSeasonValue();
		String sql = "SELECT (SELECT b.season_value\n"
				+ "          FROM bp_j_cbm_dep_season b\n"
				+ "         WHERE b.dep_id = '"
				+ deptId
				+ "'\n"
				+ "           AND b.overhead_item_id = '"
				+ overheadId
				+ "'\n"
				+ "			  AND b.year_season = t.datetime\n"
				+ "			  AND b.enterprise_code = 'hfdc'"
				+ "           AND rownum = 1) VALUE,\n"
				+ "       (SELECT b.season_id\n"
				+ "          FROM bp_j_cbm_dep_season b\n"
				+ "         WHERE b.dep_id = '"
				+ deptId
				+ "'\n"
				+ "           AND b.overhead_item_id = '"
				+ overheadId
				+ "'\n"
				+ "			  AND b.year_season = t.datetime\n"
				+ "			  AND b.enterprise_code = 'hfdc'"
				+ "           AND rownum = 1) ABSENCE\n"
				+ "FROM (SELECT '"
				+ yearStr
				+ "01' AS datetime\n"
				+ "        FROM dual\n"
				+ "      UNION ALL\n"
				+ "      SELECT '"
				+ yearStr
				+ "02' AS datetime\n"
				+ "        FROM dual\n"
				+ "      UNION ALL\n"
				+ "      SELECT '"
				+ yearStr
				+ "03' AS datetime\n"
				+ "        FROM dual\n"
				+ "      UNION ALL\n"
				+ "      SELECT '"
				+ yearStr
				+ "04' AS datetime\n"
				+ "        FROM dual\n"
				+ "      UNION ALL\n"
				+ "      SELECT '"
				+ yearStr
				+ "' AS datetime\n" + "        FROM dual) t";
		List list = bll.queryByNativeSQL(sql);
		Iterator it = list.iterator();
		String str = "";
		String strid = "";
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			if (data[0] != null)
				str += data[0].toString();
			else
				str += " ";
			if (data[1] != null)
				strid += data[1].toString();
			else
				strid += "null";
			if (it.hasNext()) {
				str += ",";
				strid += ",";
			}
		}
		model.setIdList(strid);
		model.setValueList(str);
		return model;

	}

	 
	
	/**
	 * 取得职能部门列表
	 * @param enterpriseCode
	 * @return
	 */ 
	public List<BpCCbmDepForm> getBpDeptList(String enterpriseCode) {
		String sql = "SELECT t.dep_id,\n" + "       t.dep_code,\n"
				+ "       (SELECT a.dept_name\n"
				+ "          FROM hr_c_dept a\n"
				+ "         WHERE a.dept_code = t.dep_code) deptname,\n"
				+ "       t.memo\n" + "  FROM bp_c_cbm_dep t\n"
				+ " WHERE t.is_use = 'Y'\n" + "   AND t.enterprise_code = '"
				+ enterpriseCode + "'";
		List list = bll.queryByNativeSQL(sql);
		List<BpCCbmDepForm> arraylist = new ArrayList<BpCCbmDepForm>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			BpCCbmDep model = new BpCCbmDep();
			BpCCbmDepForm fmodel = new BpCCbmDepForm();
			Object[] data = (Object[]) it.next();
			if (data[0] != null)
				model.setDepId(Long.parseLong(data[0].toString()));
			if (data[1] != null)
				model.setDepCode(data[1].toString());
			if (data[2] != null)
				fmodel.setDeptname(data[2].toString());
			if (data[3] != null)
				model.setMemo(data[3].toString());
			fmodel.setCdInfo(model);
			arraylist.add(fmodel);
		} 
		return arraylist;
	}
	/**
	 * 根据id删除职能部门
	 * @param ids
	 * @return
	 */
	public boolean deleteBpCCbmDep(String ids) {
		try {
			String sql = "UPDATE bp_c_cbm_dep t\n" + "   SET t.is_use = 'N'\n"
					+ " WHERE t.DEP_ID IN (" + ids + ")";
			bll.exeNativeSQL(sql);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 批量保存职能部门
	 * @param addList
	 * @param updateList
	 * @param delIds
	 * @return
	 */
	public boolean saveBpDeptList(List<BpCCbmDep> addList,
			List<BpCCbmDep> updateList, String delIds) {
		try {
			if (addList != null && addList.size() > 0) {
				Long id = bll.getMaxId("bp_c_cbm_dep", "DEP_ID");
				int i = 0;
				for (BpCCbmDep entity : addList) {
					entity.setDepId(id + (i++));
					entityManager.persist(entity);
				}
			}
			for (BpCCbmDep entity : updateList) {
				entityManager.merge(entity);
			}
			if (delIds != null && !delIds.trim().equals("")) {
				this.deleteBpCCbmDep(delIds);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
   @SuppressWarnings("unchecked")
   public void saveBpCCbmOverheadItem(List<Map> list,String delIds,String enterpriseCode)
   { 
		if(list!= null && list.size()>0) 
		{
			Long maxId = bll.getMaxId("Bp_c_Cbm_Overhead_Item",
					"Overhead_Item_Id");
			for (Map data : list) {
				BpCCbmOverheadItem model = new BpCCbmOverheadItem();
				if (data.get("itemCode") != null)
					model.setItemCode(data.get("itemCode").toString());
				if (data.get("itemName") != null)
					model.setItemName(data.get("itemName").toString());
				if (data.get("unit.unitId") != null) {
					BpCMeasureUnit unit = unitRemote.findById(Long
							.parseLong(data.get("unit.unitId").toString()));
					model.setUnit(unit);
				}
				model.setEnterpriseCode(enterpriseCode);
				model.setIsUse("Y");
				if (model.getOverheadItemId() == null) {
					model.setOverheadItemId(maxId++);
					entityManager.persist(model);
				} else {
					entityManager.merge(model);
				}

			}
		}
	   if(delIds!=null && !delIds.trim().equals(""))
	   {
		   String sql = "update Bp_c_Cbm_Overhead_Item  t set t.is_use='N' where t.overhead_item_id in (?)";
		   bll.exeNativeSQL(sql,new Object[]{delIds});
	   }
   } 
	public BpCCbmOverheadItem findBpCCbmOverheadItemById(Long id) {
		try {
			BpCCbmOverheadItem instance = entityManager.find(
					BpCCbmOverheadItem.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	@SuppressWarnings("unchecked")
	public List<BpCCbmOverheadItem> getOverheadList(String enterpriseCode) {
		final String queryString = "select model from BpCCbmOverheadItem model where model.enterpriseCode='"+enterpriseCode+"' and model.isUse='Y' order by model.overheadItemId";
		Query query = entityManager.createQuery(queryString);
		return query.getResultList(); 
	}

}