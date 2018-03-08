package power.ejb.equ.planrepair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.Iterator;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.equ.planrepair.form.EquPlanRepairForm;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity EquJPlanRepair.
 * 
 * @see power.ejb.equ.planrepair.EquJPlanRepair
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquJPlanRepairFacade implements EquJPlanRepairFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public void save(EquJPlanRepair entity) {
		try {
			entity.setRepairDetailId(bll.getMaxId("EQU_J_PLAN_REPAIR",
					"REPAIR_DETAIL_ID"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(EquJPlanRepair entity) {
		try {
			entity = entityManager.getReference(EquJPlanRepair.class, entity
					.getRepairDetailId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public EquJPlanRepair update(EquJPlanRepair entity) {
		try {
			EquJPlanRepair result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquJPlanRepair findById(Long id) {
		LogUtil.log("finding EquJPlanRepair instance with id: " + id,
				Level.INFO, null);
		try {
			EquJPlanRepair instance = entityManager.find(EquJPlanRepair.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<EquJPlanRepair> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding EquJPlanRepair instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from EquJPlanRepair model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<EquJPlanRepair> findAll() {
		LogUtil.log("finding all EquJPlanRepair instances", Level.INFO, null);
		try {
			final String queryString = "select model from EquJPlanRepair model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean judgeAddDetailNo(String detailNo) {
		String sql = "select count(1)\n" + "  from EQU_J_PLAN_REPAIR t\n"
				+ " where t.is_use = 'Y'\n" + "   and t.DETAIL_NO = '"
				+ detailNo + "'";
		Long count = Long.parseLong(bll.getSingal(sql).toString());
		if (count > 0) {
			return false;
		} else {
			return true;
		}
	}

	public boolean judgeUpdateDetailNo(String detailNo, String detailId) {
		String sql = "select count(1)\n" + "  from EQU_J_PLAN_REPAIR t\n"
				+ " where t.is_use = 'Y'\n" + "   and t.DETAIL_NO = '"
				+ detailNo + "'\n" + "   and t.REPAIR_DETAIL_ID != '"
				+ detailId + "'";
		Long count = Long.parseLong(bll.getSingal(sql).toString());
		if (count > 0) {
			return false;
		} else {
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject getDetailPlanList(String repairId, String enterprisecode) throws ParseException {
		PageObject result = new PageObject();
		String sql = "select t.*,\n" + "       getequnamebycode(t.equ_code),\n"
				+ "       getspecialname(t.speciality_code),\n"
				+ "       to_char(t.plan_start_time, 'yyyy-MM-dd HH:mm'),\n"
				+ "       to_char(t.plan_end_time, 'yyyy-MM-dd HH:mm'),\n"
				+ "       getworkername(t.charge_by),\n"
				+ "       a.content_name\n"
				+ "  from Equ_j_Plan_Repair t, equ_c_plan_content a\n"
				+ " where t.is_use = 'Y'\n" + " and t.enterprise_code ='"+enterprisecode+"'\n"
				+ "   and a.content_id(+) = t.content_id\n"
				+ "   and t.repair_id = '"+repairId+"'";

		String sqlcount = "select count(1)\n" + "  from Equ_j_Plan_Repair t\n"
				+ " where t.is_use = 'Y'\n" + " and t.enterprise_code ='"+enterprisecode+"'\n"
				+ "\n" + "   and t.repair_id = '"+repairId+"'";
		Long count = Long.parseLong(bll.getSingal(sqlcount).toString());
		if (count > 0) {
			List list = bll.queryByNativeSQL(sql);
			List<EquPlanRepairForm> arrList = new ArrayList<EquPlanRepairForm>();
			Iterator it = list.iterator();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
			while (it.hasNext()) {
				EquPlanRepairForm pForm = new EquPlanRepairForm();
				EquJPlanRepair model = new EquJPlanRepair();
				Object[] o = (Object[]) it.next();
				if (o[0] != null)
					model.setRepairDetailId(Long.parseLong(o[0].toString()));
				if (o[1] != null)
					model.setRepairId(Long.parseLong(o[1].toString()));
				if (o[2] != null)
					model.setContentId(Long.parseLong(o[2].toString()));
				if (o[3] != null)
					model.setDetailNo(o[3].toString());
				if (o[4] != null)
					model.setDetailName(o[4].toString());
				if (o[5] != null)
					model.setEquCode(o[5].toString());
				if (o[6] != null)
					model.setSpecialityCode(o[6].toString());
//				if (o[7] != null)
//					model.setPlanStartTime(df.parse(o[7].toString()));
//				if (o[8] != null)
//					model.setPlanEndTime(df.parse(o[8].toString()));
				if (o[9] != null)
					model.setWorkingdays(Double.parseDouble(o[9].toString()));
				if (o[10] != null)
					model.setFare(Double.parseDouble(o[10].toString()));
				if (o[11] != null)
					model.setChargeBy(o[11].toString());
				if (o[12] != null)
					model.setContent(o[12].toString());
				if (o[13] != null)
					model.setMemo(o[13].toString());
				if (o[14] != null)
					model.setAnnex(o[14].toString());
				if (o[17] != null)
					pForm.setEquName(o[17].toString());
				if (o[18] != null)
					pForm.setSpecialityName(o[18].toString());
				if (o[19] != null)
					pForm.setPlanStartTime(o[19].toString());
				if (o[20] != null)
					pForm.setPlanEndTime(o[20].toString());
				if (o[21] != null)
					pForm.setChargeByName(o[21].toString());
				if (o[22] != null)
					pForm.setContentName(o[22].toString());
				pForm.setPrepair(model);
				arrList.add(pForm);
			}
			result.setList(arrList);
			result.setTotalCount(count);
		}
		return result;
	}
}