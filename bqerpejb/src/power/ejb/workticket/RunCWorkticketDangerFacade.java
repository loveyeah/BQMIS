package power.ejb.workticket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.workticket.form.WorkticketCDanger;

/**
 * Facade for entity RunCWorkticketDanger.
 * 
 * @see power.ejb.workticket.RunCWorkticketDanger
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCWorkticketDangerFacade implements
		RunCWorkticketDangerFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public RunCWorkticketDanger save(RunCWorkticketDanger entity)
			throws CodeRepeatException {
		try {
			if (!checkDangerName(entity.getDangerName(), entity
					.getDangerTypeId())) {
				if (entity.getDangerId() == null) {
					entity.setDangerId(bll.getMaxId(
							"run_c_workticket_danger t", "danger_id"));
				}
				entity.setModifyDate(new java.util.Date());
				entity.setIsUse("Y");
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
				return entity;
			} else {
				throw new CodeRepeatException("危险点名称和危险点类型不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(Long dangerId) throws CodeRepeatException {
		try {
			RunCWorkticketDanger entity = this.findById(dangerId);
			entity.setIsUse("N");
			this.update(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}

	}

	public void deleteMulti(String dangerIds) {
		String sql = "update  run_c_workticket_danger a\n"
				+ "set a.is_use='N' \n" + "where a.danger_id in (" + dangerIds
				+ ")";
		bll.exeNativeSQL(sql);

	}

	public RunCWorkticketDanger update(RunCWorkticketDanger entity)
			throws CodeRepeatException {
		try {
			if (!checkDangerName(entity.getDangerName(), entity
					.getDangerTypeId(), entity.getDangerId())) {
				entity.setModifyDate(new java.util.Date());
				RunCWorkticketDanger result = entityManager.merge(entity);
				LogUtil.log("update successful", Level.INFO, null);
				return result;
			} else {
				throw new CodeRepeatException("危险点名称和危险点类型不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}

	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, Long dangerTypeId,Long PDangerId,
			String dangerName, int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCWorkticketDangerType instances",
				Level.INFO, null);
		try {
			String sql = null;
			String sqlCount = null;
            if(dangerName == null) {
            	dangerName = "%";
            }
			sql = "select t.danger_id,t.danger_name,t.danger_type_id,t.p_danger_id,t.order_by,t.enterprise_code,nvl(GETWORKERNAME(t.modify_by),t.modify_by) modify_by,t.modify_date,t.is_use from run_c_workticket_danger t\n"
					+ "where t.danger_name like '%"
					+ dangerName
					+ "%'\n"
					+ "and t.is_use='Y' and t.enterprise_code='"
					+ enterpriseCode + "'";
			sqlCount = "select count(*) from run_c_workticket_danger t \n"
				+ "where t.danger_name like '%" + dangerName + "%'\n";
			if(PDangerId == 0) {
			    if (dangerTypeId == 0) {
				    sql = sql + " and t.danger_type_id is not null";
					sqlCount = sqlCount + " and t.danger_type_id is not null";
			    } else {
				sql = sql + " and t.danger_type_id=" + dangerTypeId;
				sqlCount = sqlCount + " and t.danger_type_id=" + dangerTypeId;
			    } 
			} else {
				sql = sql + "  and t.danger_type_id is null"+" and t.p_danger_id = '"+PDangerId+"'";
				sqlCount = sqlCount + " and t.danger_type_id is null"+" and t.p_danger_id = '"+PDangerId+"'";
			}
			sql = sql + "  order By t.order_by";
			sqlCount = sqlCount + "  order By t.danger_id";
	
			PageObject result = new PageObject();

			List<RunCWorkticketDanger> list = bll.queryByNativeSQL(sql,
					RunCWorkticketDanger.class, rowStartIdxAndCount);

			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}

	}

	@SuppressWarnings("unchecked")
	public PageObject findListForSelect(String enterpriseCode,String workticketTypeCode,String dangerTypeId,
			String dangerName, int... rowStartIdxAndCount) 
	{
		PageObject result = new PageObject();
		List dangerList=new ArrayList();
		String sql=
			"select t.danger_id,t.danger_name,t.danger_type_id,\n" +
			"t.p_danger_id,t.order_by,t.modify_by,t.modify_date,\n" + 
			"t.enterprise_code,a.danger_type_name\n" + 
			"from run_c_workticket_danger t,run_c_workticket_danger_type a\n" + 
			"where t.danger_type_id=a.danger_type_id(+)\n" + 
			"and  t.is_use='Y'\n" + 
			"and t.enterprise_code='"+enterpriseCode+"'\n" + 
			"and a.enterprise_code='"+enterpriseCode+"'\n" + 
			"and t.p_danger_id=0\n" + 
			"and  t.danger_name like '%"+dangerName+"%'\n" + 
			"and a.is_use='Y'\n";
		    if(dangerTypeId.equals(""))
		    {
		    	sql=sql+"and a.workticket_type_code='"+workticketTypeCode+"'\n";
		    }
		    else
		    {
		    	sql=sql+"and t.danger_type_id="+dangerTypeId+" \n";
		    }
		   sql=sql+"order by t.order_by";
			
		   System.out.println(sql); 
		   String sqlCount=
				"select count(1)  \n"+
				"from run_c_workticket_danger t,run_c_workticket_danger_type a\n" + 
				"where t.danger_type_id=a.danger_type_id(+)\n" + 
				"and  t.is_use='Y'\n" + 
				"and t.enterprise_code='"+enterpriseCode+"'\n" + 
				"and a.enterprise_code='"+enterpriseCode+"'\n" + 
				"and t.p_danger_id=0\n" + 
				"and  t.danger_name like '%"+dangerName+"%'\n" + 
				"and a.is_use='Y'\n";
			    if(dangerTypeId.equals(""))
			    {
			    	sqlCount=sqlCount+"and a.workticket_type_code='"+workticketTypeCode+"'\n";
			    }
			    else
			    {
			    	sqlCount=sqlCount+"and t.danger_type_id="+dangerTypeId+" \n";
			    }
			    sqlCount=sqlCount+"order by t.order_by";
		

		
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
		Iterator it=list.iterator();
		while(it.hasNext())
 		{
			Object[] data=(Object[])it.next();
			WorkticketCDanger model=new WorkticketCDanger();
			if(data[0]!=null)
			{
				model.setDangerId(data[0].toString());
			}
			if(data[1]!=null)
			{
				model.setDangerName(data[1].toString());
			}
			if(data[2]!=null)
			{
				model.setDangerTypeId(data[2].toString());
			}
			if(data[3]!=null)
			{
				model.setPDangerId(data[3].toString());
			}
			if(data[4]!=null)
			{
				model.setOrderBy(data[4].toString());
			}
			if(data[5]!=null)
			{
				model.setModifyBy(data[5].toString());
			}
			if(data[6]!=null)
			{
				model.setModifyDate(data[6].toString());
			}
			if(data[7]!=null)
			{
				model.setEnterpriseCode(data[7].toString());
			}
			if(data[8]!=null)
			{
				model.setTypeName(data[8].toString());
			}
			dangerList.add(model);
 		}
		result.setList(dangerList);
		result.setTotalCount(totalCount);
		return result;
	}
	
	public RunCWorkticketDanger findById(Long dangerId) {
		try {
			RunCWorkticketDanger instance = entityManager.find(
					RunCWorkticketDanger.class, dangerId);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean checkDangerName(String dangerName, Long dangerTypeId,
			Long... dangerId) {
		boolean isSame = false;
		String sql = "select count(*) from run_c_workticket_danger t\n"
				+ "where t.danger_name = '" + dangerName + "'\n"
				+ "and t.is_use = 'Y'\n" + "and t.danger_type_id like '"
				+ dangerTypeId + "'";

		if (dangerId != null && dangerId.length > 0) {
			sql += "  and t.danger_id <> " + dangerId[0];
		}
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}

}