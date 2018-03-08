package power.ejb.productiontec.relayProtection;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;


@Stateless
public class PtJdbhCSylbwhFacade implements PtJdbhCSylbwhFacadeRemote {
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public PtJdbhCSylbwh save(PtJdbhCSylbwh entity) throws CodeRepeatException {
		LogUtil.log("saving PtJdbhCSylbwh instance", Level.INFO, null);
		    if(!this.checkSame(entity.getSylbName(), entity.getEnterpriseCode()))
		    {
			entity.setSylbId(bll.getMaxId("PT_JDBH_C_SYLBWH", "sylb_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		    }
		    else
		    {
		    	throw new CodeRepeatException("类别名称不能重复！");
		    }
		
	}

	public void deleteMulti(String ids)
	{
		String sql=
			"delete PT_JDBH_C_SYLBWH t\n" +
			"where t.sylb_id in ("+ids+")";
        bll.exeNativeSQL(sql);
        String sqlRef=
        	"delete PT_JDBH_C_LBYXMDY a\n" +
        	"where a.sylb_id in ("+ids+")";
        bll.exeNativeSQL(sqlRef);
	}

	public PtJdbhCSylbwh update(PtJdbhCSylbwh entity) throws CodeRepeatException {
		LogUtil.log("updating PtJdbhCSylbwh instance", Level.INFO, null);
		try {
			if(!this.checkSame(entity.getSylbName(), entity.getEnterpriseCode(), entity.getSylbId()))
			{
			PtJdbhCSylbwh result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
			}
			else
			{
				throw new CodeRepeatException("类别名称不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJdbhCSylbwh findById(Long id) {
		LogUtil.log("finding PtJdbhCSylbwh instance with id: " + id,
				Level.INFO, null);
		try {
			PtJdbhCSylbwh instance = entityManager
					.find(PtJdbhCSylbwh.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	private boolean checkSame(String sortName,String enterpriseCode,final Long ...id )
	{
		String sql=
			"select count(1) from PT_JDBH_C_SYLBWH t\n" +
			"where t.sylb_name='"+sortName+"' and t.enterprise_code='"+enterpriseCode+"'";
		if(id!=null&&id.length>0)
		{
			sql+="\n  and t.sylb_id<>"+id[0];
		}
        int count=Integer.parseInt(bll.getSingal(sql).toString());
        if(count>0) return true;
        else return false;
        	
	}
	

	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode,final int... rowStartIdxAndCount) {
		String sqlCount=
			"select count(*) from PT_JDBH_C_SYLBWH t\n" +
			"where t.enterprise_code='"+enterpriseCode+"'";
		Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
		if(totalCount>0)
		{
			PageObject obj=new PageObject();
			obj.setTotalCount(totalCount);
			String sql=
				"select * from PT_JDBH_C_SYLBWH t\n" +
				"where t.enterprise_code='"+enterpriseCode+"'  order by t.display_no asc";
			List<PtJdbhCSylbwh> list=bll.queryByNativeSQL(sql, PtJdbhCSylbwh.class, rowStartIdxAndCount);
			obj.setList(list);
			return obj;
		}
		else
		{
		return null;
		}
	}

}