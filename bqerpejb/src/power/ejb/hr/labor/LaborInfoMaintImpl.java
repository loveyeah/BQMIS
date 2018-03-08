package power.ejb.hr.labor;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.plan.itemplan.BpCItemplanEcoItem;
import power.ejb.manage.plan.trainplan.BpCTrainingType;

@Stateless
public class LaborInfoMaintImpl implements LaborInfoMaint {
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;


	public boolean checkClassInput(HrCLaborClass entity) {
		String st = "select count(*)  from  HR_C_LABOR_CLASS t  where t.labor_class_name='"
				+ entity.getLaborClassName() + "' and t.is_use='Y'";
		
		if(entity.getLaborClassId() != null)
			st += " and t.labor_class_id <>" + entity.getLaborClassId();
		int a = Integer.parseInt(bll.getSingal(st).toString());
		if (a > 0) {
			return true;
		} else
		{
           String  sql = "select count(*)  from  HR_C_LABOR_CLASS t  where t.labor_class_code='"
				+ entity.getLaborClassCode() + "' and t.is_use='Y'";
           if(entity.getLaborClassId() != null)
        	   sql += " and t.labor_class_id <>" + entity.getLaborClassId();
			 if(bll.getSingal(sql).toString().equals("0"))
			 {
				 return false;
			 }
			 else
			 {
				 return true;
			 }
		}
	}
	
	public boolean checkDelete(String ids) {
		String st = "select t.labor_class_id  from  HR_C_LABOR_CLASS t  where t.labor_class_id in (" + ids + ")and t.is_use='Y'\n" +
			" and t.labor_class_id in(select a.labor_class_id from hr_c_labor_material a where  a.is_use='Y')";
		int a = bll.exeNativeSQL(st);
		if (a > 0) {
			return true;
		} else

			return false;
	}
	public HrCLaborClass saveLaborClass(HrCLaborClass entity) {
		try {
			entity.setLaborClassId(bll.getMaxId("HR_C_LABOR_CLASS",
					"labor_class_id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		return entity;

	}

	public HrCLaborClass updateLaborClass(HrCLaborClass entity) {
		try {
			HrCLaborClass result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}

	}

	public void deleteLaborClass(String ids) {
		String sql = "update HR_C_LABOR_CLASS t\n" + "   set t.is_use = 'N'\n"
				+ " where t.labor_class_id in (" + ids + ")";

		bll.exeNativeSQL(sql);

	}

	public HrCLaborClass findByClassId(Long id) {
		try {
			HrCLaborClass instance = entityManager
					.find(HrCLaborClass.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PageObject findLaborClassList(String codeOrName,
			String enterpriseCode, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select t.labor_class_id,\n" + "t.labor_class_code,\n"
				+ "t.labor_class_name\n" + "from HR_C_LABOR_CLASS t\n"
				+ "where t.is_use='Y'\n" + "and t.enterprise_code='"
				+ enterpriseCode + "'\n";

		String sqlCount = "select count(*) from (" + sql + ") \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}

	public boolean checkMaterialInput(HrCLaborMaterial entity) {
		String st = "select t.labor_material_id  from  HR_C_LABOR_MATERIAL t  where t.labor_material_name='"
				+ entity.getLaborMaterialName() + "'and t.is_use='Y'";
		if(entity.getLaborMaterialId() != null)
			st += " and t.labor_material_id <>" + entity.getLaborMaterialId();
		int a = bll.exeNativeSQL(st);
		if (a > 0) {
			return true;
		} else

			return false;
	}
	
	
	public HrCLaborMaterial saveLaborMaterial(HrCLaborMaterial entity) {

		try {
			entity.setLaborMaterialId(bll.getMaxId("HR_C_LABOR_MATERIAL",
					"labor_material_id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		return entity;
	}

	public HrCLaborMaterial updateLaborMaterial(HrCLaborMaterial entity) {
		try {
			HrCLaborMaterial result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}

	}

	public void deleteLaborMaterial(String ids) {

		String sql = "update HR_C_LABOR_MATERIAL t\n"
				+ "   set t.is_use = 'N'\n" + " where t.labor_material_id in ("
				+ ids + ")";

		bll.exeNativeSQL(sql);

	}

	public HrCLaborMaterial findByMaterialId(Long id) {
		try {
			HrCLaborMaterial instance = entityManager.find(
					HrCLaborMaterial.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PageObject findLaborMaterialList(String laborMaterialName,
			String laborClass, String recieveKind, String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select t.labor_material_id,\n"
				+ "t.labor_material_name,\n"
				+ "t.material_code,\n"
				+ "t.material_name,\n"
				+ "t.labor_class_id,\n"
				+ "(select a.labor_class_name from HR_C_LABOR_CLASS a where a.labor_class_id=t.labor_class_id )laborClassName,\n"
				+ "t.unit_id,\n"
				+ "(select b.unit_name from bp_c_measure_unit b where b.unit_id=t.unit_id) unitName,\n"
				+ "t.is_send,\n" + "t.receive_kind,\n" + "t.order_by,\n"
				+ "t.searches_code\n" + "from HR_C_LABOR_MATERIAL t\n"
				+ "where t.is_use='Y'\n" + "and t.enterprise_code='"
				+ enterpriseCode + "'\n";

		String sqlCount = "select count(*) from (" + sql + ") \n";
		sql += " order by t.order_by";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}

	public void saveLaborStandard(List<HrJLaborStandard> addList,List<HrJLaborStandard> updateList, String ids) {

		if (addList != null && addList.size() > 0) {
			for (HrJLaborStandard entity : addList) {
				entity.setLaborStandardId(bll.getMaxId("HR_J_LABOR_STANDARD",
						"labor_standard_id"));
				entityManager.persist(entity);
				entityManager.flush();
			}
		}
		if (updateList != null && updateList.size() > 0) {
			for (HrJLaborStandard entity : updateList) {
				entityManager.merge(entity);
			}
		}
		if (ids != null && ids.length() > 0) {
			String sql = "update HR_J_LABOR_STANDARD a set a.is_use='N' where a.labor_standard_id in ("
					+ ids + ")";
			bll.exeNativeSQL(sql);
		}

	}

	public PageObject findLaborStandardList(Long laborWorkId,
			String enterpriseCode, int... rowStartIdxAndCount) {

		PageObject pg = new PageObject();
		String strWhere ="";
		if (laborWorkId != null && !laborWorkId.equals("")) {
			strWhere += "and t.lb_work_id ='" + laborWorkId + "'\n";
		}
		String sql = "select t.labor_standard_id,\n" + "t.lb_work_id,\n"
				+ "t.labor_material_id,\n" 
				+"(select a.labor_material_name from HR_C_LABOR_MATERIAL a where a.labor_material_id=t.labor_material_id) laborMaterialName,\n"
				+ "t.spacing_month,\n"
				+ "t.material_num,\n" + "t.send_kind\n"
				+ "from HR_J_LABOR_STANDARD t\n" + " where t.is_use='Y'\n"
				+ " and t.enterprise_code='" + enterpriseCode + "'\n";
		if (strWhere != null && !strWhere.equals("")) {
			sql += strWhere;
		}
		String sqlCount = "select count(*) from (" + sql + ") \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;

	}

	public PageObject findNoSelectLaborStandardList(Long laborWorkId,
			String enterpriseCode, int... rowStartIdxAndCount) {

		PageObject pg = new PageObject();
		String sql = "select t.labor_material_id,\n" +
			"       t.labor_material_name,\n" + 
			"       t.unit_id,\n" + 
			"       (select b.unit_name from bp_c_measure_unit b where b.unit_id=t.unit_id) unitName,\n" + 
			"       t.receive_kind\n" + 
			"       from HR_C_LABOR_MATERIAL t\n" + 
			"       where t.is_use='Y'\n" + 
			"       and t.enterprise_code='"+enterpriseCode+"'\n" + 
			"       and t.labor_material_id not in\n" + 
			"        (select a.labor_material_id\n" + 
			"          from HR_J_LABOR_STANDARD a\n" + 
			"          where a.enterprise_code='"+enterpriseCode+"' and a.is_use='Y'" +
			"          and a.lb_work_id =" + laborWorkId + "\n)";
		String sqlCount = "select count(*) from (" + sql + ") \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;

	}

	 public HrJLaborChange saveLaborChange(List<HrJLaborChange> list) 
	 {
		
		Long detailId = bll.getMaxId("HR_J_LABOR_CHANGE", "labor_change_id");
		for (HrJLaborChange model : list) {  
			if (model.getLaborChangeId() != null) {
				updateChangeList(model);
			} else { 
				model.setLaborChangeId(detailId);
				saveChangeList(model);
			}
		}
		return null;

		
	}

	public void saveChangeList(HrJLaborChange entity) {
		try { 
			
			entityManager.persist(entity);
			entity.setIsUse("Y");
			
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public boolean updateChangeList(HrJLaborChange entity) {
		try {
			entityManager.merge(entity);
			return true;
		} catch (RuntimeException re) {
			throw re;

		}
	}

	


	
	public void deleteLaborChange(String ids)
	{
		String sql = "update HR_J_LABOR_CHANGE a set a.is_use = 'N' where a.labor_change_id in (" + ids
		+ ")";

     bll.exeNativeSQL(sql);
		
	}
	
	public PageObject  findLaborChangeList(String workCode,String enterpriseCode,int... rowStartIdxAndCount)
	{
		
		PageObject pg = new PageObject();
		String sql=
			" select  d.labor_change_id,to_char(d.change_date,'yyyy-mm-dd hh:mm:ss') change_date,to_char(d.start_date,'yyyy-mm-dd hh:mm:ss') start_date,\n" +
			"(select t.lb_work_name from hr_c_lbgzbm t where d.old_lb_work_id=t.lb_work_id)lb_work_name_old,(select t.lb_work_name from hr_c_lbgzbm t where d.new_lb_work_id=t.lb_work_id)lb_work_name_new,\n" + 
			"d.is_use,d.ENTERPRISE_CODE,d.work_code,d.new_lb_work_id,d.old_lb_work_id\n" + 
			"from  HR_J_LABOR_CHANGE  d\n" + 
			"\n" + 
			"where\n" + 
			"\n" + 
			" d.work_code='"+workCode+"'\n" + 
			"and d.is_use='Y'\n" + 
			"and   d.enterprise_code='"+enterpriseCode+"' order by d.change_date asc";

			List list = bll.queryByNativeSQL(sql);
			pg.setList(list);
	    System.out.println(sql);
		return pg;
	};


}
