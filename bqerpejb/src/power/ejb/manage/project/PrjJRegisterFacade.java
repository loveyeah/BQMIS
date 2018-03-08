package power.ejb.manage.project;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity PrjJRegister.
 * 
 * @see power.ejb.manage.project.PrjJRegister
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PrjJRegisterFacade implements PrjJRegisterFacadeRemote {
	// property constants
	public static final String PRJ_NO = "prjNo";
	public static final String PRJ_NAME = "prjName";
	public static final String PRJ_DEPT = "prjDept";
	public static final String PRJ_TYPE_ID = "prjTypeId";
	public static final String APPLY_FUNDS = "applyFunds";
	public static final String APPROVED_FUNDS = "approvedFunds";
	public static final String IS_FUNDS_FINISH = "isFundsFinish";
	public static final String PRJ_YEAR = "prjYear";
	public static final String DURATION = "duration";
	public static final String ENTRY_BY = "entryBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";
	public static final String PRJ_BY = "prjBy";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	

	@SuppressWarnings("unchecked")
	public PageObject getPrjRegister(String prjName,String year,String prjType,String isFunds,String enterPriseCode,String workerCode, int... rowStartIdxAndCount)
	{    //modify by ypan 20100909
		PageObject result=new PageObject();
		String sql=
			"select " +
			"r.prj_id," +
			"r.prj_no," +
			"r.prj_name," +
			"r.prj_dept," +
			"getdeptname(r.prj_dept) ," +
			"r.prj_type_id," +
			"r.apply_funds,\n" +
			"r.approved_funds," +
			"r.is_funds_finish," +
			"r.prj_year," +
			"r.duration," +
			"r.entry_by," +
			"getworkername(r.entry_by) ," +
			"r.entry_date  ,\n" + 
			"r.prj_by  ," +
			"getworkername(r.prj_by)," +
			"t.prj_type_name, \n" +
			"r.annex,\n"+
			"decode(r.status_id,0,'未开工',1,'已开工',2,'已验收','')\n"+
			"from PRJ_J_REGISTER  r,PRJ_C_TYPE   t\n" + 
			"where  t.prj_type_id(+)=r.prj_type_id\n " +
			"and  r.enterprise_code='"+enterPriseCode+"'\n" + 
			"and r.is_use='Y'";
        if(year!=null&&!year.equals(""))
        {
        	sql+=" and r.prj_year='"+year+"'";
        }
        if(prjType!=null&&!prjType.equals(""))
        {
        	sql+=" and r.prj_type_id='"+prjType+"'";
        }
        if(isFunds!=null&&!isFunds.equals(""))
        {
        	sql+=" and r.is_funds_finish='"+isFunds+"'";
        }
        if(prjName!=null&&!"".equals(prjName)){
        	sql+=" and r.prj_name like '%"+prjName+"%'";
        }
        if(workerCode!=null&&!"".equals(workerCode)){
        	sql+=" and r.entry_by ='"+workerCode+"'";
        }
        String sqlCount  = "select count(1) from ("+sql+")\n";//add by drdu 20100824 加分页
        List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
        Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
        result.setList(list);
        result.setTotalCount(totalCount);
		return result;
		
	}
	public void delPrjRegister(String ids)
	{
		String sql=
			"update PRJ_J_REGISTER  r\n" +
			"set r.is_use='N'\n" + 
			"where r.prj_id in ("+ids+")";
		   bll.exeNativeSQL(sql);

	}
	public void savePrjRegister(List<PrjJRegister>  addList ,List<PrjJRegister> updateList )
	{
		if (addList.size() > 0) {

			for (PrjJRegister entity : addList) {
				this.save(entity);
				entityManager.flush();
			}
		}
		if (updateList.size() > 0) {
			for (PrjJRegister entity : updateList) {
				this.update(entity);
				entityManager.flush();
			}
		}
		
	}
	public void save(PrjJRegister entity) {
		LogUtil.log("saving PrjJRegister instance", Level.INFO, null);
		try {
			Long prjId = bll.getMaxId("PRJ_J_REGISTER","prj_id");
			entity.setPrjId(prjId);
			entity.setIsUse("Y");
			entity.setStatusId(0l);
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(PrjJRegister entity) {
		LogUtil.log("deleting PrjJRegister instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PrjJRegister.class, entity
					.getPrjId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public PrjJRegister update(PrjJRegister entity) {
		LogUtil.log("updating PrjJRegister instance", Level.INFO, null);
		try {
			PrjJRegister result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PrjJRegister findById(Long id) {
		LogUtil.log("finding PrjJRegister instance with id: " + id, Level.INFO,
				null);
		try {
			PrjJRegister instance = entityManager.find(PrjJRegister.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
}