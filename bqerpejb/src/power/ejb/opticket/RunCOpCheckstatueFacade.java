package power.ejb.opticket;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ejb.comm.NativeSqlHelperRemote;

/**
 * @author slTang
 */
@Stateless
public class RunCOpCheckstatueFacade implements RunCOpCheckstatueFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public void save(RunCOpCheckstatue entity) {
		try {
			if(entity.getCheckStatueId()==null){
				entity.setCheckStatueId(bll.getMaxId("RUN_C_OP_CHECKSTATUE", "CHECK_STATUE_ID"));
			}
			entity.setIsUse("Y");
			entity.setModifyDate(new Date());
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	
	public void delete(RunCOpCheckstatue entity) {
		entity.setIsUse("N");
		this.update(entity);
	}

	public RunCOpCheckstatue update(RunCOpCheckstatue entity) {
		try {
			RunCOpCheckstatue result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public RunCOpCheckstatue findById(Long id) {
		try {
			RunCOpCheckstatue instance = entityManager.find(
					RunCOpCheckstatue.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<RunCOpCheckstatue> findAll(String enterpriseCode) {
		String sql="select t.* from RUN_C_OP_CHECKSTATUE t ";
		String whereSql=" where t.is_use='Y' ";
		if(enterpriseCode!=null){
			whereSql +=" and t.enterprise_code='"+enterpriseCode+"'";
		}
		sql+=whereSql+" order by t.check_bef_flag,t.Display_No";
		return bll.queryByNativeSQL(sql, RunCOpCheckstatue.class);
	}
	
	public List<RunCOpCheckstatue> findPublic(String enterpriseCode,String checkBefFlag,String isRunning){
		String sql="select t.* from RUN_C_OP_CHECKSTATUE t ";
		String whereSql=" where t.is_use='Y' ";
		if(enterpriseCode!=null && enterpriseCode.length()>0){
			whereSql +=" and t.enterprise_code='"+enterpriseCode+"'";
		}
		if(checkBefFlag!=null && checkBefFlag.length()>0){
			//操作前;操作后;公用 
			whereSql +=" and t.check_bef_flag in ('C','"+checkBefFlag+"') ";
		}
		if(isRunning!=null && isRunning.length()>0){
			whereSql +=" and t.IS_RUN_FLAG='"+isRunning+"'";
		}
		 
		sql+=whereSql; 
		sql += " order by t.check_bef_flag, t.DISPLAY_NO";
		return bll.queryByNativeSQL(sql, RunCOpCheckstatue.class);
	}

}