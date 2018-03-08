package power.ejb.productiontec.powertest;

import java.util.ArrayList;
import java.util.Date;
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
import power.ejb.productiontec.powertest.form.PtJYqybtzForm;

/**
 * Facade for entity PtJYqybtz.
 * 
 * @see power.ejb.productiontec.powertest.PtJYqybtz
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJYqybtzFacade implements PtJYqybtzFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved PtJYqybtz entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJYqybtz entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJYqybtz entity) {
		LogUtil.log("saving PtJYqybtz instance", Level.INFO, null);
		try {
			entity.setRegulatorId(bll.getMaxId("PT_J_YQYBTZ", "regulator_id"));

			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PtJYqybtz entity.
	 * 
	 * @param entity
	 *            PtJYqybtz entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJYqybtz entity) {
		LogUtil.log("deleting PtJYqybtz instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJYqybtz.class, entity
					.getRegulatorId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(String ids) {

		String sqlString = "delete from PT_J_YQYBTZ s "
				+ " where s.regulator_id in (" + ids + ") ";
		bll.exeNativeSQL(sqlString);
	}

	/**
	 * Persist a previously saved PtJYqybtz entity and return it or a copy of it
	 * to the sender. A copy of the PtJYqybtz entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtJYqybtz entity to update
	 * @return PtJYqybtz the persisted PtJYqybtz entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJYqybtz update(PtJYqybtz entity) {
		LogUtil.log("updating PtJYqybtz instance", Level.INFO, null);
		try {
			PtJYqybtz result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJYqybtz findById(Long id) {
		LogUtil.log("finding PtJYqybtz instance with id: " + id, Level.INFO,
				null);
		try {
			PtJYqybtz instance = entityManager.find(PtJYqybtz.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PtJYqybtz entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJYqybtz property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<PtJYqybtz> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PtJYqybtz> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PtJYqybtz instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJYqybtz model where model."
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

	/**
	 * Find all PtJYqybtz entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJYqybtz> all PtJYqybtz entities
	 */
	@SuppressWarnings("unchecked")
	public List<PtJYqybtz> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all PtJYqybtz instances", Level.INFO, null);
		try {
			final String queryString = "select model from PtJYqybtz model";
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

	@SuppressWarnings("unchecked")
	public PageObject getYaybtzlist(String fuzzy, Long jdzyId,
			String enterpriseCode, int... rowStartIdxAndCount) {

		if (fuzzy == null || fuzzy.equals("")) {
			fuzzy = "%";
		}
		// modified by liuyi 20100512 
		String sqlString = 
//			"   select  t.*,s.yqyblb_name,"
//				+ " getworkername(t.charger),getdeptname(t.dep_code) "

			"select t.regulator_id,\n" +
			"       t.names,\n" + 
			"       t.yqyblb_id,\n" + 
			"       t.yqybdj_id,\n" + 
			"       t.yqybjd_id,\n" + 
			"       t.user_range,\n" + 
			"       t.sizes,\n" + 
			"       t.test_cycle,\n" + 
			"       t.factory,\n" + 
			"       t.out_factory_date,\n" + 
			"       t.out_factory_no,\n" + 
			"       t.buy_date,\n" + 
			"       t.use_date,\n" + 
			"       t.dep_code,\n" + 
			"       t.charger,\n" + 
			"       t.if_used,\n" + 
			"       t.memo,\n" + 
			"       t.last_check_date,\n" + 
			"       t.next_check_date,\n" + 
			"       t.jdzy_id,\n" + 
			"       t.enterprise_code,\n" + 
			"       s.yqyblb_name,\n" + 
			"       getworkername(t.charger),\n" + 
			"       getdeptname(t.dep_code),\n" + 
			"       t.grade,\n" + 
			"       t.precision,\n" + 
			"       t.check_dept_code,\n" + 
			"       t.main_param\n" 
			+ " ,t.CHECK_RESULT \n"
				+ " from PT_J_YQYBTZ t ,pt_c_yqyblb s"
				+ " where t.yqyblb_id=s.yqyblb_id" + " and t.jdzy_id=" + jdzyId
				+ "   and t.enterprise_code='" + enterpriseCode + "'"
				+ "and t.names like '%" + fuzzy + "%'";
		String sqlcount = "select count(* )"
				+ " from PT_J_YQYBTZ t ,pt_c_yqyblb s"
				+ " where t.yqyblb_id=s.yqyblb_id" + " and t.jdzy_id=" + jdzyId
				+ "   and t.enterprise_code='" + enterpriseCode + "'"
				+ "and t.names like '%" + fuzzy + "%'";
		List list = bll.queryByNativeSQL(sqlString, rowStartIdxAndCount);
		List<PtJYqybtzForm> arrayList = new ArrayList<PtJYqybtzForm>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] dataObjects = (Object[]) it.next();
			PtJYqybtz model = new PtJYqybtz();
			PtJYqybtzForm form = new PtJYqybtzForm();
			if (dataObjects[0] != null)
				model.setRegulatorId(Long.parseLong(dataObjects[0].toString()));
			if (dataObjects[1] != null)
				model.setNames(dataObjects[1].toString());
			if (dataObjects[2] != null)
				model.setYqyblbId(Long.parseLong(dataObjects[2].toString()));
			if (dataObjects[3] != null)
				model.setYqybdjId(Long.parseLong(dataObjects[3].toString()));
			if (dataObjects[4] != null)
				model.setYqybjdId(Long.parseLong(dataObjects[4].toString()));
			if (dataObjects[5] != null)
				model.setUserRange(dataObjects[5].toString());
			if (dataObjects[6] != null)
				model.setSizes(dataObjects[6].toString());
			if (dataObjects[7] != null)
				model.setTestCycle(Long.parseLong(dataObjects[7].toString()));
			if (dataObjects[8] != null)
				model.setFactory(dataObjects[8].toString());
			if (dataObjects[9] != null)
				model.setOutFactoryDate((Date) dataObjects[9]);
			if (dataObjects[10] != null)
				model.setOutFactoryNo(dataObjects[10].toString());
			if (dataObjects[11] != null)
				model.setBuyDate((Date) dataObjects[11]);
			if (dataObjects[12] != null)
				model.setUseDate((Date) dataObjects[12]);
			if (dataObjects[13] != null)
				model.setDepCode(dataObjects[13].toString());
			if (dataObjects[14] != null)
				model.setCharger(dataObjects[14].toString());
			if (dataObjects[15] != null)
				model.setIfUsed(dataObjects[15].toString());
			if (dataObjects[16] != null)
				model.setMemo(dataObjects[16].toString());
			if(dataObjects[17] != null)
				model.setLastCheckDate((Date)dataObjects[17]);
			if(dataObjects[18] != null)
				model.setNextCheckDate((Date)dataObjects[18]);
			if (dataObjects[19] != null)
				model.setJdzyId(Long.parseLong(dataObjects[19].toString()));
			if (dataObjects[21] != null)
				form.setNameOfSort(dataObjects[21].toString());
			if (dataObjects[22] != null)
				form.setChargerName(dataObjects[22].toString());
			if (dataObjects[23] != null)
				form.setDepName(dataObjects[23].toString());
			
			// add by liuyi 20100512 
			if(dataObjects[24] != null)
				model.setGrade(dataObjects[24].toString());
			if(dataObjects[25] != null)
				model.setPrecision(dataObjects[25].toString());
			if(dataObjects[26] != null)
				model.setCheckDeptCode(dataObjects[26].toString());
			if(dataObjects[27] != null)
				model.setMainParam(dataObjects[27].toString());
			if(dataObjects[28] != null)
				model.setCheckResult(dataObjects[28].toString());
			form.setModel(model);
			arrayList.add(form);

		}
		Long countLong = Long.parseLong(bll.getSingal(sqlcount).toString());
		PageObject object = new PageObject();
		object.setList(arrayList);
		object.setTotalCount(countLong);
		return object;

	}

	@SuppressWarnings("unchecked")
	public PageObject getInstrumentTestRecord(String fuzzy, Long jdzyId,
			String enterpriseCode, int... rowStartIdxAndCount) {

		String sqlString = 
			"select t.regulator_id,\n" +
			"       t.names,\n" + 
			"       t.yqyblb_id,\n" + 
			"       t.yqybdj_id,\n" + 
			"       t.yqybjd_id,\n" + 
			"       t.user_range,\n" + 
			"       t.sizes,\n" + 
			"       t.test_cycle,\n" + 
			"       t.factory,\n" + 
			"       t.out_factory_date,\n" + 
			"       t.out_factory_no,\n" + 
			"       t.buy_date,\n" + 
			"       t.use_date,\n" + 
			"       t.dep_code,\n" + 
			"       t.charger,\n" + 
			"       t.if_used,\n" + 
			"       t.memo,\n" + 
			"       t.last_check_date,\n" + 
			"       t.next_check_date,\n" + 
			"       t.jdzy_id,\n" + 
			"       t.enterprise_code,\n" + 
			"       s.yqyblb_name,\n" + 
			"       getworkername(t.charger),\n" + 
			"       getdeptname(t.dep_code),\n" + 
			"       t.grade,\n" + 
			"       t.precision,\n" + 
			"       t.check_dept_code,\n" + 
			"       t.main_param,\n" + 
			"       t.CHECK_RESULT \n" + 
			"  from PT_J_YQYBTZ t, pt_c_yqyblb s\n" + 
			" where t.yqyblb_id = s.yqyblb_id "
//			"   select  t.*,s.yqyblb_name,"
//				+ " getworkername(t.charger),getdeptname(t.dep_code),m.yqybdj_name,n.yqybjd_name "
//				+ " from PT_J_YQYBTZ t ,pt_c_yqyblb s ,pt_c_yqybdj m ,pt_c_yqybjd n"
//				+ " where t.yqyblb_id=s.yqyblb_id"
//				+ " and t.yqybjd_id=n.yqybjd_id"
//				+ "  and t.yqybdj_id=m.yqybdj_id" 
				+ " and t.jdzy_id=" + jdzyId
				+ "   and t.enterprise_code='" + enterpriseCode + "'";
		String sqlcount = "select count(* )"
				+ " from PT_J_YQYBTZ t ,pt_c_yqyblb s ,pt_c_yqybdj m ,pt_c_yqybjd n"
				+ " where t.yqyblb_id=s.yqyblb_id"
				+ " and t.yqybjd_id=n.yqybjd_id"
				+ "  and t.yqybdj_id=m.yqybdj_id" + " and t.jdzy_id=" + jdzyId
				+ "   and t.enterprise_code='" + enterpriseCode + "'";
		if (!fuzzy.equals("") && fuzzy != null) {
			sqlString += " and t.next_check_date >= to_date('" + fuzzy
					+ "' || '01', 'yyyy-MM-dd')" + "  AND t.next_check_date <"
					+ "    add_months(to_date('" + fuzzy
					+ "' || '01', 'yyyy-MM-dd'),1)";
			sqlcount += " and t.next_check_date >= to_date('" + fuzzy
					+ "' || '01', 'yyyy-MM-dd')" + "  AND t.next_check_date <"
					+ "    add_months(to_date('" + fuzzy
					+ "' || '01', 'yyyy-MM-dd'),1)";
		}
		sqlString += " order by t.next_check_date desc";

		List list = bll.queryByNativeSQL(sqlString, rowStartIdxAndCount);
		List<PtJYqybtzForm> arrayList = new ArrayList<PtJYqybtzForm>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] dataObjects = (Object[]) it.next();
			PtJYqybtz model = new PtJYqybtz();
			PtJYqybtzForm form = new PtJYqybtzForm();
			if (dataObjects[0] != null)
				model.setRegulatorId(Long.parseLong(dataObjects[0].toString()));
			if (dataObjects[1] != null)
				model.setNames(dataObjects[1].toString());
			if (dataObjects[2] != null)
				model.setYqyblbId(Long.parseLong(dataObjects[2].toString()));
			if (dataObjects[3] != null)
				model.setYqybdjId(Long.parseLong(dataObjects[3].toString()));
			if (dataObjects[4] != null)
				model.setYqybjdId(Long.parseLong(dataObjects[4].toString()));
			if (dataObjects[5] != null)
				model.setUserRange(dataObjects[5].toString());
			if (dataObjects[6] != null)
				model.setSizes(dataObjects[6].toString());
			if (dataObjects[7] != null)
				model.setTestCycle(Long.parseLong(dataObjects[7].toString()));
			if (dataObjects[8] != null)
				model.setFactory(dataObjects[8].toString());
			if (dataObjects[9] != null)
				model.setOutFactoryDate((Date) dataObjects[9]);
			if (dataObjects[10] != null)
				model.setOutFactoryNo(dataObjects[10].toString());
			if (dataObjects[11] != null)
				model.setBuyDate((Date) dataObjects[11]);
			if (dataObjects[12] != null)
				model.setUseDate((Date) dataObjects[12]);
			if (dataObjects[13] != null)
				model.setDepCode(dataObjects[13].toString());
			if (dataObjects[14] != null)
				model.setCharger(dataObjects[14].toString());
			if (dataObjects[15] != null)
				model.setIfUsed(dataObjects[15].toString());
			if (dataObjects[16] != null)
				model.setMemo(dataObjects[16].toString());
			if (dataObjects[17] != null)
				model.setLastCheckDate((Date) dataObjects[17]);
			if (dataObjects[18] != null)
				model.setNextCheckDate((Date) dataObjects[18]);
			if (dataObjects[19] != null)
				model.setJdzyId(Long.parseLong(dataObjects[19].toString()));
			if (dataObjects[21] != null)
				form.setNameOfSort(dataObjects[21].toString());
			if (dataObjects[22] != null)
				form.setChargerName(dataObjects[22].toString());
			if (dataObjects[23] != null)
				form.setDepName(dataObjects[23].toString());
//			if (dataObjects[24] != null)
//				form.setYqybdjName(dataObjects[24].toString());
//			if (dataObjects[25] != null)
//				form.setYqybjdName(dataObjects[25].toString());
			if(dataObjects[24] != null)
				model.setGrade(dataObjects[24].toString());
			if(dataObjects[25] != null)
				model.setPrecision(dataObjects[25].toString());
			if(dataObjects[26] != null)
				model.setCheckDeptCode(dataObjects[26].toString());
			if(dataObjects[27] != null)
				model.setMainParam(dataObjects[27].toString());
			if(dataObjects[28] != null)
				model.setCheckResult(dataObjects[28].toString());
			form.setModel(model);
			arrayList.add(form);

		}
		Long countLong = Long.parseLong(bll.getSingal(sqlcount).toString());
		PageObject object = new PageObject();
		object.setList(arrayList);
		object.setTotalCount(countLong);
		return object;

	}

	@SuppressWarnings("unchecked")
	public PageObject getOverdueInstrumentTestRecord(String date, String names,
			Long jdzyId, String enterpriseCode, int... rowStartIdxAndCount) {
		if (names.equals("") || names == null)
			names = "%";
		// modified by liuyi 20100512 
		String sqlString = 
			"select t.regulator_id,\n" +
			"       t.names,\n" + 
			"       t.yqyblb_id,\n" + 
			"       t.yqybdj_id,\n" + 
			"       t.yqybjd_id,\n" + 
			"       t.user_range,\n" + 
			"       t.sizes,\n" + 
			"       t.test_cycle,\n" + 
			"       t.factory,\n" + 
			"       t.out_factory_date,\n" + 
			"       t.out_factory_no,\n" + 
			"       t.buy_date,\n" + 
			"       t.use_date,\n" + 
			"       t.dep_code,\n" + 
			"       t.charger,\n" + 
			"       t.if_used,\n" + 
			"       t.memo,\n" + 
			"       t.last_check_date,\n" + 
			"       t.next_check_date,\n" + 
			"       t.jdzy_id,\n" + 
			"       t.enterprise_code,\n" + 
			"       s.yqyblb_name,\n" + 
			"       getworkername(t.charger),\n" + 
			"       getdeptname(t.dep_code),\n" + 
			"       t.grade,\n" + 
			"       t.precision,\n" + 
			"       t.check_dept_code,\n" + 
			"       t.main_param,\n" + 
			"       t.CHECK_RESULT\n" + 
			"  from PT_J_YQYBTZ t, pt_c_yqyblb s\n" + 
			" where t.yqyblb_id = s.yqyblb_id \n"
//			"   select  t.*,s.yqyblb_name,"
//				+ " getworkername(t.charger),getdeptname(t.dep_code),m.yqybdj_name,n.yqybjd_name "
//				+ " from PT_J_YQYBTZ t ,pt_c_yqyblb s ,pt_c_yqybdj m ,pt_c_yqybjd n"
//				+ " where t.yqyblb_id=s.yqyblb_id"
//				+ " and t.yqybjd_id=n.yqybjd_id"
//				+ "  and t.yqybdj_id=m.yqybdj_id" 
				+ " and t.jdzy_id=" + jdzyId
				+ "   and t.enterprise_code='" + enterpriseCode + "'"
				+ " and t.names like '%" + names + "%'";
		String sqlcount = "select count(* )"
				+ " from PT_J_YQYBTZ t ,pt_c_yqyblb s ,pt_c_yqybdj m ,pt_c_yqybjd n"
				+ " where t.yqyblb_id=s.yqyblb_id"
				+ " and t.yqybjd_id=n.yqybjd_id"
				+ "  and t.yqybdj_id=m.yqybdj_id" + " and t.jdzy_id=" + jdzyId
				+ "   and t.enterprise_code='" + enterpriseCode + "'"
				+ " and t.names like '%" + names + "%'";

		sqlString += " and t.next_check_date <to_date('" + date
				+ "', 'yyyy-MM-dd')";
		sqlcount += " and t.next_check_date <to_date('" + date
				+ "', 'yyyy-MM-dd')";

		sqlString += " order by t.next_check_date ";

		List list = bll.queryByNativeSQL(sqlString, rowStartIdxAndCount);
		List<PtJYqybtzForm> arrayList = new ArrayList<PtJYqybtzForm>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] dataObjects = (Object[]) it.next();
			PtJYqybtz model = new PtJYqybtz();
			PtJYqybtzForm form = new PtJYqybtzForm();
			if (dataObjects[0] != null)
				model.setRegulatorId(Long.parseLong(dataObjects[0].toString()));
			if (dataObjects[1] != null)
				model.setNames(dataObjects[1].toString());
			if (dataObjects[2] != null)
				model.setYqyblbId(Long.parseLong(dataObjects[2].toString()));
			if (dataObjects[3] != null)
				model.setYqybdjId(Long.parseLong(dataObjects[3].toString()));
			if (dataObjects[4] != null)
				model.setYqybjdId(Long.parseLong(dataObjects[4].toString()));
			if (dataObjects[5] != null)
				model.setUserRange(dataObjects[5].toString());
			if (dataObjects[6] != null)
				model.setSizes(dataObjects[6].toString());
			if (dataObjects[7] != null)
				model.setTestCycle(Long.parseLong(dataObjects[7].toString()));
			if (dataObjects[8] != null)
				model.setFactory(dataObjects[8].toString());
			if (dataObjects[9] != null)
				model.setOutFactoryDate((Date) dataObjects[9]);
			if (dataObjects[10] != null)
				model.setOutFactoryNo(dataObjects[10].toString());
			if (dataObjects[11] != null)
				model.setBuyDate((Date) dataObjects[11]);
			if (dataObjects[12] != null)
				model.setUseDate((Date) dataObjects[12]);
			if (dataObjects[13] != null)
				model.setDepCode(dataObjects[13].toString());
			if (dataObjects[14] != null)
				model.setCharger(dataObjects[14].toString());
			if (dataObjects[15] != null)
				model.setIfUsed(dataObjects[15].toString());
			if (dataObjects[16] != null)
				model.setMemo(dataObjects[16].toString());
			if (dataObjects[17] != null)
				model.setLastCheckDate((Date) dataObjects[17]);
			if (dataObjects[18] != null)
				model.setNextCheckDate((Date) dataObjects[18]);
			if (dataObjects[19] != null)
				model.setJdzyId(Long.parseLong(dataObjects[19].toString()));
			if (dataObjects[21] != null)
				form.setNameOfSort(dataObjects[21].toString());
			if (dataObjects[22] != null)
				form.setChargerName(dataObjects[22].toString());
			if (dataObjects[23] != null)
				form.setDepName(dataObjects[23].toString());
//			if (dataObjects[24] != null)
//				form.setYqybdjName(dataObjects[24].toString());
//			if (dataObjects[25] != null)
//				form.setYqybjdName(dataObjects[25].toString());
			if(dataObjects[24] != null)
				model.setGrade(dataObjects[24].toString());
			if(dataObjects[25] != null)
				model.setPrecision(dataObjects[25].toString());
			if(dataObjects[26] != null)
				model.setCheckDeptCode(dataObjects[26].toString());
			if(dataObjects[27] != null)
				model.setMainParam(dataObjects[27].toString());
			if(dataObjects[28] != null)
				model.setCheckResult(dataObjects[28].toString());
			form.setModel(model);
			arrayList.add(form);

		}
		Long countLong = Long.parseLong(bll.getSingal(sqlcount).toString());
		PageObject object = new PageObject();
		object.setList(arrayList);
		object.setTotalCount(countLong);
		return object;

	}

	public Long checkTheSame(String tableName, String property, String value,
			String enterpriseCode, String jdzyId) {
		String sqlString = " selete count(*)" + " from " + tableName + " t"
				+ " where t." + property + "='" + value + "'"
				+ " and t.enterprise_code='" + enterpriseCode + "'"
				+ " and t.jdzy_Id=" + jdzyId;
		Long count = Long.parseLong(bll.getSingal(sqlString).toString());
		return count;
	}
}