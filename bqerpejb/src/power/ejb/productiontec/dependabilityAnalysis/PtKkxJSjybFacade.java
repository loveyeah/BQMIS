package power.ejb.productiontec.dependabilityAnalysis;

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
import power.ejb.productiontec.dependabilityAnalysis.form.PtKkxJSjybForm;

/**
 * Facade for entity PtKkxJSjyb.
 * 
 * @see power.ejb.productiontec.dependabilityAnalysis.PtKkxJSjyb
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtKkxJSjybFacade implements PtKkxJSjybFacadeRemote {
	// property constants
	public static final String BLOCK_CODE = "blockCode";
	public static final String FDL = "fdl";
	public static final String UTH = "uth";
	public static final String PH = "ph";
	public static final String UNDH = "undh";
	public static final String SH = "sh";
	public static final String RH = "rh";
	public static final String POT = "pot";
	public static final String POH = "poh";
	public static final String UOT = "uot";
	public static final String UOH = "uoh";
	public static final String FOT = "fot";
	public static final String FOH = "foh";
	public static final String FOR1 = "for1";
	public static final String EAF = "eaf";
	public static final String EXR = "exr";
	public static final String POF = "pof";
	public static final String UOF = "uof";
	public static final String FOF = "fof";
	public static final String AF = "af";
	public static final String SF = "sf";
	public static final String UDF = "udf";
	public static final String UTF = "utf";
	public static final String UOR = "uor";
	public static final String FOOR = "foor";
	public static final String MTTPO = "mttpo";
	public static final String MTTUO = "mttuo";
	public static final String CAH = "cah";
	public static final String MTBF = "mtbf";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public void save(PtKkxJSjyb entity) {
		LogUtil.log("saving PtKkxJSjyb instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(PtKkxJSjyb entity) {
		LogUtil.log("deleting PtKkxJSjyb instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtKkxJSjyb.class, entity
					.getKkxybId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public PtKkxJSjyb update(PtKkxJSjyb entity) {
		LogUtil.log("updating PtKkxJSjyb instance", Level.INFO, null);
		try {
			PtKkxJSjyb result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtKkxJSjyb findById(Long id) {
		LogUtil.log("finding PtKkxJSjyb instance with id: " + id, Level.INFO,
				null);
		try {
			PtKkxJSjyb instance = entityManager.find(PtKkxJSjyb.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public PageObject getMonthInput(String month, String enterpriseCode,
			int... rowStartIdxAndCount) {
		try {
			PageObject pg = new PageObject();
			final String queryString = "select t.sjlr_id,t.block_code,t.jzzt_id,"
					+ "t.start_date,t.end_date,t.keep_time,"
					+ "t.reduce_exert,t.stop_times,t.success_times,"
					+ "t.failure_times,t.repair_mandays,t.repair_cost,"
					+ "t.stop_reason,t.enterprise_code"
					+ "from PT_KKX_J_SJLR t where t.enterprise_code='"
					+ enterpriseCode
					+ "'"
					+ " and  to_date("
					+ month
					+ ",'yyyy-mm')>t.start_date "
					+ "and to_date("
					+ month
					+ ",'yyyy-mm')<t.end_date ";
			String sql = "select count(1) from PT_KKX_J_SJLR t where t.enterprise_code='"
					+ enterpriseCode
					+ "'"
					+ " and  to_date("
					+ month
					+ ",'yyyy-mm')>t.start_date "
					+ "and to_date("
					+ month
					+ ",'yyyy-mm')<t.end_date ";
			Long count = Long.parseLong(bll.getSingal(sql).toString());
			List<PtKkxJSjlr> list = bll.queryByNativeSQL(queryString,
					PtKkxJSjlr.class, rowStartIdxAndCount);
			pg.setList(list);
			pg.setTotalCount(count);
			return pg;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PtKkxJSjyb entities.
	 * 
	 * @return List<PtKkxJSjyb> all PtKkxJSjyb entities
	 */
	@SuppressWarnings("unchecked")
	public List<PtKkxJSjybForm> getcomputelist(String month,
			String enterpriseCode) {
		@SuppressWarnings("unused")
		String statusUNDH = "";
		String sqlString = "	select t.block_code, r.block_name"
				+ "	, getblockitemh(t.block_code,v_status,v_month,v_enterprisecode ) UNDH   ,"
				+ "	 getblockitemh(t.block_code,v_status,v_month,v_enterprisecode ) SH  ,"
				+ "	 getblockitemh(t.block_code,v_status,v_month,v_enterprisecode ) RH  ,"

				+ "	 getblockitemh(t.block_code,v_status,v_month,v_enterprisecode ) POH  ,"
				+ "	 getblockitemh(t.block_code,v_status,v_month,v_enterprisecode ) UOH  ,"
				+ "	 getblockitemh(t.block_code,v_status,v_month,v_enterprisecode ) FOH  ,"
				+ "	 getblockitemt(t.block_code,v_status,v_month,v_enterprisecode ) POT ,"
				+ "	getblockitemt(t.block_code,v_status,v_month,v_enterprisecode )UOT ,"
				+ "	getblockitemt(t.block_code,v_status,v_month,v_enterprisecode ) FOT ,"
				+ "	getblockEUNDH(t.block_code,v_status,v_month,v_enterprisecode ) EUNDH "
				+ "	from PT_KKX_C_BLOCK_INFO t,equ_c_block r "
				+ "	where t.block_code=r.block_code"
				+ "	and t.enterprise_code='" + enterpriseCode + "'"
				+ " order by r.id";
		List list = bll.queryByNativeSQL(sqlString);
		List<PtKkxJSjybForm> arrlist = new ArrayList<PtKkxJSjybForm>();
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			Object[] data = (Object[]) iterator.next();
			PtKkxJSjybForm model = new PtKkxJSjybForm();
			if (data[1] != null)
				model.setBlockCode(data[1].toString());
			if (data[2] != null)
				model.setUndh(Double.parseDouble(data[2].toString()));
			if (data[3] != null)
				model.setSh(Double.parseDouble(data[2].toString()));
			if (data[4] != null)
				model.setRh(Double.parseDouble(data[2].toString()));
			if (data[5] != null)
				model.setPoh(Double.parseDouble(data[2].toString()));
			if (data[6] != null)
				model.setUoh(Double.parseDouble(data[2].toString()));
			if (data[7] != null)
				model.setFoh(Double.parseDouble(data[2].toString()));

			if (data[8] != null)
				model.setPot(Double.parseDouble(data[2].toString()));
			if (data[9] != null)
				model.setUot(Double.parseDouble(data[2].toString()));
			if (data[10] != null)
				model.setFot(Double.parseDouble(data[2].toString()));
			if (data[11] != null)
				model.setEundh(Double.parseDouble(data[2].toString()));
			arrlist.add(model);
		}

		return arrlist;
	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String month,String enterpriseCode, int... rowStartIdxAndCount) {
		String sql = "select t.* " + " from PT_KKX_J_SJYB t "
				+ " where t.enterprise_code='" + enterpriseCode + "'";
		if (month != null && !(month.equals(""))) {
			sql = sql + " and to_char(t.month,'yyyy-mm') ='" + month + "' ";

		}
		sql = sql + " order by t.MONTH";
		String sqlcount = "select count(*) " + " from PT_KKX_J_SJYB t "
				+ " where t.enterprise_code='" + enterpriseCode + "'";
		if (month != null && !(month.equals(""))) {
			sqlcount = sqlcount + " and  to_char(t.month,'yyyy-mm') ='" + month + "' ";

		}
		List<PtKkxJSjyb> list = bll.queryByNativeSQL(sql, PtKkxJSjyb.class,
				rowStartIdxAndCount);
		Long count = Long.parseLong(bll.getSingal(sqlcount).toString());
		PageObject object = new PageObject();
		object.setList(list);
		object.setTotalCount(count);
		return object;

	}
	
	
	public void modifyRecords(List<PtKkxJSjyb> list)
	{
		if(list != null)
		{
			for (PtKkxJSjyb m : list)
			{
				this.update(m);
			}
		}
	}
}