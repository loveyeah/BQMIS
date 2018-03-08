package power.ejb.productiontec.relayProtection;

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
import power.ejb.productiontec.relayProtection.form.PtCLbyxmdyForm;


@Stateless
public class PtJdbhCLbyxmdyFacade implements PtJdbhCLbyxmdyFacadeRemote {
	// property constants
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public PtJdbhCLbyxmdy save(PtJdbhCLbyxmdy entity) {
		LogUtil.log("saving PtJdbhCLbyxmdy instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	private void delete(String syxmIds,Long sylbId,String enterpriseCode)
	{
		String sql=
			"delete PT_JDBH_C_LBYXMDY t\n" +
			"where t.sylb_id="+sylbId+"\n" + 
			"and t.syxm_id in ("+syxmIds+")  \n" + 
			"and t.enterprise_code='"+enterpriseCode+"'";
		bll.exeNativeSQL(sql);

	}
	
	public PtJdbhCLbyxmdy update(PtJdbhCLbyxmdy entity) {
		LogUtil.log("updating PtJdbhCLbyxmdy instance", Level.INFO, null);
		try {
			PtJdbhCLbyxmdy result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJdbhCLbyxmdy findById(PtJdbhCLbyxmdyId id) {
		LogUtil.log("finding PtJdbhCLbyxmdy instance with id: " + id,
				Level.INFO, null);
		try {
			PtJdbhCLbyxmdy instance = entityManager.find(PtJdbhCLbyxmdy.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void saveRecords(Long sortId,String selectIds,String noselectIds,String enterpriseCode)
	{
		if(!noselectIds.equals(""))
		{
		this.delete(noselectIds, sortId, enterpriseCode);
		entityManager.flush();
		}
		if(!selectIds.equals(""))
		{
		String [] ids=selectIds.split(",");
		for(int i=0;i<ids.length;i++)
		{
			if(!this.ifHaveRecord(Long.parseLong(ids[i]), sortId, enterpriseCode))
			{
				PtJdbhCLbyxmdy entity=new PtJdbhCLbyxmdy();
				entity.setEnterpriseCode(enterpriseCode);
				PtJdbhCLbyxmdyId idModel=new PtJdbhCLbyxmdyId();
				idModel.setSylbId(sortId);
				idModel.setSyxmId(Long.parseLong(ids[i]));
				entity.setId(idModel);
				this.save(entity);
				entityManager.flush();
			}
		}
		}
		
	}
	
	private boolean ifHaveRecord(Long syxmId,Long sylbId,String enterpriseCode)
	{
		String sql=
			"select count(*) from PT_JDBH_C_LBYXMDY  t\n" +
			"where t.sylb_id="+sylbId+"\n" + 
			"and t.syxm_id="+syxmId+"\n" + 
			"and t.enterprise_code='"+enterpriseCode+"'";
		int count= Integer.parseInt(bll.getSingal(sql).toString());
        if(count>0)
        {
        	return true;
        }
        else
        {
        	return false;
        }
		
	}

	

	

	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode,Long sylbId,final int... rowStartIdxAndCount) {
		String sqlCount=
			"select count(*)\n" +
			"from PT_JDBH_C_SYXMWH b\n" + 
			"where  b.enterprise_code='"+enterpriseCode+"'\n" ;
		Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
		if(totalCount>0)
		{
			PageObject obj=new PageObject();
			obj.setTotalCount(totalCount);
			String sql=
				"select distinct b.syxm_id,b.syxm_name,\n" +
				"decode(\n" + 
				"(select count(*)   from  PT_JDBH_C_LBYXMDY tt  where  tt.sylb_id="+sylbId+"  and b.syxm_id=tt.syxm_id\n" + 
				"),0,'N','Y') flag,b.display_no\n" + 
				"from PT_JDBH_C_LBYXMDY t,PT_JDBH_C_SYXMWH b\n" + 
				"where  t.syxm_id(+)=b.syxm_id\n" + 
				"and t.enterprise_code(+)='"+enterpriseCode+"'\n" + 
				"and b.enterprise_code='"+enterpriseCode+"'\n" + 
				"order by b.display_no asc";

			List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			Iterator it= list.iterator();
		    List objList=new ArrayList();
		    while(it.hasNext())
		    {
		    	PtCLbyxmdyForm model=new PtCLbyxmdyForm();
		    	Object[] data=(Object [])it.next();
		    	if(data[0]!=null)
		    	{
		    		model.setSyxmId(Long.parseLong(data[0].toString()));
		    	}
		    	
		    	if(data[1]!=null)
		    	{
		    		model.setSyxmName(data[1].toString());
		    	}
		    	if(data[2]!=null)
		    	{
		    		model.setFlag(data[2].toString());
		    	}
		    	objList.add(model);
		    }
           obj.setList(objList);
           return obj;
		}
		else
		{
		return null;
		}
	}

}