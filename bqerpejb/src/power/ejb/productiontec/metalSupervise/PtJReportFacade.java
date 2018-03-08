package power.ejb.productiontec.metalSupervise;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

import power.ejb.productiontec.metalSupervise.form.PtJReportForm;


/**
 * Facade for entity PtJReport.
 * 
 * @see power.ejb.productiontec.metalSupervise.PtJReport
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJReportFacade implements PtJReportFacadeRemote {
	// property constants
	public static final String REPORT_TYPE = "reportType";
	public static final String TOPIC = "topic";
	public static final String CONTENT = "content";
	public static final String YEAR = "year";
	public static final String SMART_DATE = "smartDate";
	public static final String TIME_TYPE = "timeType";
	public static final String WORK_FLOW_NO = "workFlowNo";
	public static final String CHECK_MARK = "checkMark";
	public static final String FILL_BY = "fillBy";
	public static final String MEMO = "memo";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public void save(PtJReport entity) {
		LogUtil.log("saving PtJReport instance", Level.INFO, null);
		try {
			entity.setReportId(bll.getMaxId("PT_J_REPORT", "report_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMulti(String ids) {
		String sql=
			"delete PT_J_REPORT t\n" +
			"where t.report_id in ("+ids+")";
		bll.exeNativeSQL(sql);
	}

	
	public PtJReport update(PtJReport entity) {
		LogUtil.log("updating PtJReport instance", Level.INFO, null);
		try {
			PtJReport result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJReport findById(Long id) {
		LogUtil.log("finding PtJReport instance with id: " + id, Level.INFO,
				null);
		try {
			PtJReport instance = entityManager.find(PtJReport.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}


	@SuppressWarnings("unchecked")
	public PageObject findAll(String year,String smartDate,String reportType,String timeType,String enterpriseCode,final int... rowStartIdxAndCount) {
		String sqlCount=
			"select count(*)\n" +
			"  from pt_j_report t\n" + 
		//	" where t.report_type  '"+reportType+"'\n" + 
			"   where t.time_type = '"+timeType+"'\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'";
		String sql=
 		   " select t.*,getworkername(t.fill_by),to_char(t.fill_date, 'yyyy-MM-dd')\n" +
			"  from pt_j_report t\n" + 
			//" where t.report_type = '"+reportType+"'\n" + 
			"   where t.time_type = '"+timeType+"'\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'";
			
		String strWhere = "";
		String strOrder = "   order by t.year desc,t.smart_date desc";
		if(reportType!=null&&reportType.length()>0)
		{
			//modify by fyyang 20100512
			strWhere+=" and t.report_type = '"+reportType+"'\n" ;
		}
		if (year != null && year.length() > 0)
			strWhere += "  and t.year = '"+year+"'";
		if (smartDate != null && smartDate.length() >0)
			strWhere += "  and t.smart_date = '"+smartDate+"'";
		sqlCount += strWhere;
		sql += strWhere;
		sql += strOrder;
       Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
       if(totalCount>0)
       {
    	   PageObject obj=new PageObject();
    	   obj.setTotalCount(totalCount);
    	   List<PtJReportForm> list=bll.queryByNativeSQL(sql,rowStartIdxAndCount);
    	   List arrlist = new ArrayList();
   		   Iterator it = list.iterator();
   		if (list != null && list.size() > 0) {
			while (it.hasNext()) {
				PtJReportForm form = new PtJReportForm();
				PtJReport model = new PtJReport();
				Object[] data = (Object[]) it.next();
				model.setReportId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					model.setReportType(data[1].toString());
				if (data[2] != null)
					model.setTopic(data[2].toString());
				if (data[3] != null)
					model.setContent(data[3].toString());
				if (data[4] != null)
					model.setYear(data[4].toString());
				if (data[5] != null)
					model.setSmartDate(data[5].toString());
				if (data[6] != null)
					model.setTimeType(Long.parseLong(data[6].toString()));
				if (data[7] != null)
					model.setWorkFlowNo(Long.parseLong(data[7].toString()));
				if (data[8] != null)
					model.setCheckMark(Long.parseLong(data[8].toString()));
				if (data[9] != null)
					model.setFillBy(data[9].toString());
				if (data[11] != null)
					model.setMemo(data[11].toString());
				if (data[13] != null)
					form.setFillName(data[13].toString());
				if (data[14]!= null)
					form.setFillDate(data[14].toString());
				form.setModel(model);
				arrlist.add(form);
			}
   		}
           obj.setList(arrlist);
           return obj;
       }
       else
       {
    	   return null;
       }
		
	}

}