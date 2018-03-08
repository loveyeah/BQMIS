package power.ejb.run.powernotice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.run.powernotice.form.PowerNoticeForPrint;

/**
 * Facade for entity RunJPowerNoticeApprove.
 * 
 * @see power.ejb.run.powernotice.RunJPowerNoticeApprove
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJPowerNoticeApproveFacade implements
		RunJPowerNoticeApproveFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public void save(RunJPowerNoticeApprove entity) {
		LogUtil.log("saving RunJPowerNoticeApprove instance", Level.INFO, null);
		try {
			entity.setApproveDate(new java.util.Date());
			entity.setApproveId(bll.getMaxId("run_j_power_notice_approve", "approve_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(RunJPowerNoticeApprove entity) {
		LogUtil.log("deleting RunJPowerNoticeApprove instance", Level.INFO,
				null);
		try {
			entity = entityManager.getReference(RunJPowerNoticeApprove.class,
					entity.getApproveId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public RunJPowerNoticeApprove update(RunJPowerNoticeApprove entity) {
		LogUtil.log("updating RunJPowerNoticeApprove instance", Level.INFO,
				null);
		try {
			RunJPowerNoticeApprove result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJPowerNoticeApprove findById(Long id) {
		LogUtil.log("finding RunJPowerNoticeApprove instance with id: " + id,
				Level.INFO, null);
		try {
			RunJPowerNoticeApprove instance = entityManager.find(
					RunJPowerNoticeApprove.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public PowerNoticeForPrint findByNoForPrint(String enterpriseCode,String noticeNo) throws ParseException
	{
		PowerNoticeForPrint model=new PowerNoticeForPrint();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    SimpleDateFormat sf=new SimpleDateFormat("yyyy年MM月dd日 kk时mm分");
		SimpleDateFormat ddf = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat ssf=new SimpleDateFormat("yyyy年MM月dd日");
		String sql="select a.notice_no,\n" +
			"GETDEPTNAME(a.contact_dept),\n" + 
			"GETWORKERNAME(a.contact_monitor),\n" + 
			"to_char(a.contact_date, 'yyyy-MM-dd hh24:mi:ss'),\n" + 
			"decode(a.notice_sort,'1','300MW机组设备','4','125MW机组设备','')as notice_sort,\n" + 
			"a.contact_content,\n" + 
			"a.memo,\n" + 
			"GETWORKERNAME(b.approve_by),\n" + 
			"to_char(b.approve_date, 'yyyy-MM-dd'),\n" + 
			"b.receive_team\n" + 
			"from run_c_power_notice a,run_j_power_notice_approve b\n" + 
			"where a.notice_no=b.notice_no(+)\n" + 
			"and a.notice_no='"+noticeNo+"'\n" + 
			"and b.approve_status(+)='3'\n" + 
			"and a.is_use='Y'\n" + 
			"and a.enterprise_code='"+enterpriseCode+"'";
       List list=bll.queryByNativeSQL(sql);
       if(list!=null&&list.size()>0)
       {
    	   Object[] data=  (Object[]) list.get(0);
    	   model.setNoticeNo(data[0].toString());
    	   if(data[1]!=null)
    	   {
    		   model.setDeptName(data[1].toString());
    	   }
    	   if(data[2]!=null)
    	   {
    		   model.setMonitorName(data[2].toString());
    	   }
    	   if(data[3]!=null)
    	   {
    		   Date contactDate=df.parse(data[3].toString());
				
    		  model.setContactDate(sf.format(contactDate));
    	   }
    	   //update by sychen 20100810
    	   if(data[4]!=null)
    	   {
    		   model.setNoticeSort(data[4].toString());
    	   }
    	   if(data[5]!=null)
    	   {
    		   model.setContactContent(data[5].toString());
    	   }
    	   if(data[6]!=null)
    	   {
    		   model.setMemo(data[6].toString());
    	   }
    	   if(data[7]!=null)
    	   {
    		   model.setApproveBy(data[7].toString());
    	   }
    	   if(data[8]!=null)
    	   {
    		   Date approveDate=ddf.parse(data[8].toString());
				
     		  model.setApproveDate(ssf.format(approveDate));
    	   }
    	   if(data[9]!=null)
    	   {
    		   model.setReceiveTeam(data[9].toString());
    	   }
//    	   if(data[4]!=null)
//    	   {
//    		   model.setContactContent(data[4].toString());
//    	   }
//    	   if(data[5]!=null)
//    	   {
//    		   
//    		   Date finishDate=df.parse(data[5].toString());
//			
//    		   model.setFinishDate(sf.format(finishDate));
//    	   }
//    	   if(data[6]!=null)
//    	   {
//    		   model.setElectricMonitor(data[6].toString());
//    	   }
//    	   if(data[7]!=null)
//    	   {
//    		   model.setValidateMonitor(data[7].toString());
//    	   }

    	   //update by sychen 20100810 end
       }
		return model;
	}

	


	
	
}