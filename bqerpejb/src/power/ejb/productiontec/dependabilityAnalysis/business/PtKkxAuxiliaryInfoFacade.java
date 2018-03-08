package power.ejb.productiontec.dependabilityAnalysis.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.productiontec.dependabilityAnalysis.business.form.AuxiliaryForm;

/**
 * 辅机基本信息
 * @author liuyi 091020
 */
@Stateless
public class PtKkxAuxiliaryInfoFacade implements PtKkxAuxiliaryInfoFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;

	/**
	 * 新增一条辅机基本信息
	 */
	public void save(PtKkxAuxiliaryInfo entity) {
		LogUtil.log("saving PtKkxAuxiliaryInfo instance", Level.INFO, null);
		try {
			entity.setAuxiliaryId(bll.getMaxId("PT_KKX_AUXILIARY_INFO", "AUXILIARY_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条辅机基本信息
	 */
	public void delete(PtKkxAuxiliaryInfo entity) {
		LogUtil.log("deleting PtKkxAuxiliaryInfo instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtKkxAuxiliaryInfo.class,
					entity.getAuxiliaryId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(String ids)
	{
		String sql = "update  PT_KKX_AUXILIARY_INFO a set a.is_use='N' \n"
			+ "where a.AUXILIARY_ID in (" + ids + ") \n";
		bll.exeNativeSQL(sql);
	}
	/**
	 * 更新一条辅机基本信息
	 */
	public PtKkxAuxiliaryInfo update(PtKkxAuxiliaryInfo entity) {
		LogUtil.log("updating PtKkxAuxiliaryInfo instance", Level.INFO, null);
		try {
			PtKkxAuxiliaryInfo result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtKkxAuxiliaryInfo findById(Long id) {
		LogUtil.log("finding PtKkxAuxiliaryInfo instance with id: " + id,
				Level.INFO, null);
		try {
			PtKkxAuxiliaryInfo instance = entityManager.find(
					PtKkxAuxiliaryInfo.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<PtKkxAuxiliaryInfo> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PtKkxAuxiliaryInfo instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtKkxAuxiliaryInfo model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
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
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<PtKkxAuxiliaryInfo> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all PtKkxAuxiliaryInfo instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from PtKkxAuxiliaryInfo model";
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

	public PageObject getAllAuxiliaryRec(Long blockId, Long tpyeId,
			String name, String enterpriseCode, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.auxiliary_id, \n"
			+ "a.block_id, \n"
			+ "a.auxiliary_type_id, \n"
			+ "a.auxiliary_code, \n"
			+ "a.auxiliary_name, \n"
			+ "a.auxiliary_model, \n"
			+ "a.update_no, \n"
			+ "a.leave_factory_no, \n"
			+ "a.factory_code, \n"
			+ "a.produce_factory, \n"
			+ "b.block_name, \n"
			+ "c.auxiliary_type_name, \n"
			+ "to_char(a.start_pro_date,'yyyy-MM-dd'), \n"
			+ "to_char(a.stop_stat_date,'yyyy-MM-dd'), \n"
			+ "to_char(a.leave_factory_date,'yyyy-MM-dd'), \n"
			+ "to_char(a.stat_date,'yyyy-MM-dd'), \n"
			+ "to_char(a.stop_use_date,'yyyy-MM-dd'), \n"
			+ "b.nameplate_capability \n"
			+ "from PT_KKX_AUXILIARY_INFO a,PT_KKX_BLOCK_INFO b,PT_KKX_AUXILIARY_TYPE c \n"
			+ "where a.is_use='Y' \n"
			+ "and b.is_use='Y' \n"
			+ "and c.is_use='Y' \n"
			+ "and a.enterprise_code='" + enterpriseCode + "' \n"
			+ "and b.enterprise_code='" + enterpriseCode + "' \n"
			+ "and c.enterprise_code='" + enterpriseCode + "' \n"
			+ "and a.block_id=b.block_id \n"
			+ "and a.auxiliary_type_id=c.auxiliary_type_id \n";
		if(blockId != null)
			sql += "and a.block_id=" + blockId + " \n";
		if(tpyeId != null)
			sql += "and a.auxiliary_type_id=" + tpyeId + " \n";
		if(name != null && !name.equals(""))
			sql += "and a.auxiliary_name like '%" + name + "%' \n";
		String sqlCount = "select count(*) from (" + sql + ") \n";
		sql += "order by a.auxiliary_id \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List<AuxiliaryForm> arrlist = new ArrayList<AuxiliaryForm>();
		if(list != null && list.size() > 0)
		{
			Iterator it = list.iterator();
			while(it.hasNext())
			{
				AuxiliaryForm form = new AuxiliaryForm();
				Object[] da = (Object[])it.next();
				if(da[0] != null)
					form.setAuxiliaryId(Long.parseLong(da[0].toString()));
				if(da[1] != null)
					form.setBlockId(Long.parseLong(da[1].toString()));
				if(da[2] != null)
					form.setAuxiliaryTypeId(Long.parseLong(da[2].toString()));
				if(da[3] != null)
					form.setAuxiliaryCode(da[3].toString());
				if(da[4] != null)
					form.setAuxiliaryName(da[4].toString());
				if(da[5] != null)
					form.setAuxiliaryModel(da[5].toString());
				if(da[6] != null)
					form.setUpdateNo(da[6].toString());
				if(da[7] != null)
					form.setLeaveFactoryNo(da[7].toString());
				if(da[8] != null)
					form.setFactoryCode(da[8].toString());
				if(da[9] != null)
					form.setProduceFactory(da[9].toString());
				if(da[10] != null)
					form.setBlockName(da[10].toString());
				if(da[11] != null)
					form.setTypeName(da[11].toString());
				if(da[12] != null)
					form.setStartProDateString(da[12].toString());
				if(da[13] != null)
					form.setStopStatDateString(da[13].toString());
				if(da[14] != null)
					form.setLeaveFactoryDateString(da[14].toString());
				if(da[15] != null)
					form.setStatDateString(da[15].toString());
				if(da[16] != null)
					form.setStopUseDateString(da[16].toString());
				if(da[17] != null)
					form.setNameplateCapability(Double.parseDouble(da[17].toString()));
				else
					form.setNameplateCapability(0.0);
				arrlist.add(form);
			}
		}
		pg.setList(arrlist);
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setTotalCount(totalCount);
		return pg;
	}

}