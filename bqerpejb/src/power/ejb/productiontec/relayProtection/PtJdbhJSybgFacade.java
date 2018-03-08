package power.ejb.productiontec.relayProtection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import power.ejb.productiontec.relayProtection.form.ExperimentReportForm;


@Stateless
public class PtJdbhJSybgFacade implements PtJdbhJSybgFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "PtJdbhJBhzztzFacade")
	protected PtJdbhJBhzztzFacadeRemote rem;

	
	public PtJdbhJSybg save(PtJdbhJSybg entity) {
		LogUtil.log("saving PtJdbhJSybg instance", Level.INFO, null);
		try {
			String sql = "select count(*) from PT_JDBH_J_SYBG t\n"
				+ "where t.JDSYBG_NAME = '" + entity.getJdsybgName() + "'";
				if(Long.parseLong((bll.getSingal(sql).toString())) > 0)
				{
					return null;
				}
			entity.setJdsybgId(bll.getMaxId("PT_JDBH_J_SYBG", "jdsybg_id"));
			PtJdbhJBhzztz pjjb = rem.findById(entity.getDeviceId());
			GregorianCalendar calendar = new GregorianCalendar();
			entity.setLastTestDate(pjjb.getLastCheckDate());
			entity.setPlanTestDate(pjjb.getNextCheckDate());
			if(entity.getTestDate() != null)
			{
				pjjb.setLastCheckDate(entity.getTestDate());
				calendar.setTime(entity.getTestDate());
				calendar.add(Calendar.MONTH, pjjb.getTestCycle().intValue());
				pjjb.setNextCheckDate(calendar.getTime());
				rem.update(pjjb);
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void deleteMulti(String ids)
	{
		String sql=
			"delete PT_JDBH_J_SYBG t\n" +
			"where t.jdsybg_id in ("+ids+")";
		bll.exeNativeSQL(sql);
		String refSql=
			"delete PT_JDBH_J_SYBGJL a\n" +
			"where  a.jdsybg_id in ("+ids+")";

       bll.exeNativeSQL(refSql);
	}

	
	public PtJdbhJSybg update(PtJdbhJSybg entity) {
		LogUtil.log("updating PtJdbhJSybg instance", Level.INFO, null);
		try {
			String sql = "select count(*) from PT_JDBH_J_SYBG t\n"
				+ "where t.JDSYBG_NAME = '" + entity.getJdsybgName() + "'"
				+ "and t.JDSYBG_ID !=" + entity.getJdsybgId();
				if(Long.parseLong((bll.getSingal(sql).toString())) > 0)
				{
					return null;
				}
				PtJdbhJBhzztz pjjb = rem.findById(entity.getDeviceId());
				GregorianCalendar calendar = new GregorianCalendar();
				entity.setLastTestDate(pjjb.getLastCheckDate());
				entity.setPlanTestDate(pjjb.getNextCheckDate());
				if(entity.getTestDate() != null)
				{
					pjjb.setLastCheckDate(entity.getTestDate());
					calendar.setTime(entity.getTestDate());
					calendar.add(Calendar.MONTH, pjjb.getTestCycle().intValue());
					pjjb.setNextCheckDate(calendar.getTime());
					rem.update(pjjb);
				}
			PtJdbhJSybg result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJdbhJSybg findById(Long id) {
		LogUtil.log("finding PtJdbhJSybg instance with id: " + id, Level.INFO,
				null);
		try {
			PtJdbhJSybg instance = entityManager.find(PtJdbhJSybg.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String sDate,String eDate,String deviceName,String enterpriseCode,int... rowStartIdxAndCount)
	{
		String sqlCount=
			"select count(*)\n" +
			"from PT_JDBH_J_SYBG t,PT_JDBH_C_SYLBWH a\n" + 
			"where t.sylb_id=a.sylb_id(+)\n" + 
			"and t.enterprise_code='"+enterpriseCode+"'\n" + 
			"and a.enterprise_code(+)='"+enterpriseCode+"'\n" ;
		if(sDate != null && (!sDate.equals("")))
		{
			sqlCount = sqlCount + " and t.test_date>=to_date('"+sDate+"','yyyy-MM-dd')\n "; 
		}
		if(eDate != null && (!eDate.equals("")))
		{
			sqlCount = sqlCount + "and t.test_date<=to_date('"+eDate+"','yyyy-MM-dd')\n "; 
		}
		if(deviceName != null && (!deviceName.equals("")))
		{
			sqlCount = sqlCount + "and GETDEVICENAMEBYID(t.device_id) like '%"+deviceName+"%'";
		}			
         Long totalCount= Long.parseLong(bll.getSingal(sqlCount).toString());
         if(totalCount>0)
         {
        	 PageObject obj=new PageObject();
        	 obj.setTotalCount(totalCount);
        	 String sql=
        		 "select\n" +
        		 "t.jdsybg_id,\n" + 
        		 "t.device_id,GETDEVICENAMEBYID(t.device_id),\n" + 
        		 "t.sylb_id,a.sylb_name,\n" + 
        		 "t.jdsybg_name,t.test_place,\n" + 
        		 "to_char(t.test_date, 'yyyy-MM-dd'),\n" + 
        		 "to_char(t.last_test_date, 'yyyy-MM-dd'),\n" + 
        		 "to_char(t.plan_test_date, 'yyyy-MM-dd'),\n" + 
        		 "t.test_type,t.weather,\n" + 
        		 "t.temperature,t.humidity,\n" + 
        		 "t.test_by,\n" + 
        		 "t.charge_by,GETWORKERNAME(t.charge_by),\n" + 
        		 "t.test_situation,t.content,t.memo,t.enterprise_code, \n" + 
        		 "GETWORKERNAME(t.test_by) \n" +
        		 "from PT_JDBH_J_SYBG t,PT_JDBH_C_SYLBWH a\n" + 
        		 "where t.sylb_id=a.sylb_id(+)\n" + 
        		 "and t.enterprise_code='"+enterpriseCode+"'\n" + 
     			"and a.enterprise_code(+)='"+enterpriseCode+"'\n ";
        	 if(sDate != null && (!sDate.equals("")))
     		{
     			sql = sql + " and t.test_date>=to_date('"+sDate+"','yyyy-MM-dd')\n "; 
     		}
     		if(eDate != null && (!eDate.equals("")))
     		{
     			sql = sql + "and t.test_date<=to_date('"+eDate+"','yyyy-MM-dd')\n "; 
     		}
     		if(deviceName != null && (!deviceName.equals("")))
     		{
     			sql = sql + "and GETDEVICENAMEBYID(t.device_id) like '%"+deviceName+"%'";
     		}
     		sql = sql + " order by t.jdsybg_id ";
            List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
            List objList=new ArrayList();
            Iterator it= list.iterator();
            SimpleDateFormat sbf = new SimpleDateFormat("yyyy-MM-dd");
            while(it.hasNext())
            {
            	Object[] data=(Object [])it.next();
            	ExperimentReportForm model=new ExperimentReportForm();
            	PtJdbhJSybg entity=new PtJdbhJSybg();
            	if(data[0]!=null)
            	{
            		entity.setJdsybgId(Long.parseLong(data[0].toString()));
            	}
            	if(data[1]!=null)
            	{
            		entity.setDeviceId(Long.parseLong(data[1].toString()));
            	}
            	if(data[2]!=null)
            	{
            		model.setDeviceName(data[2].toString());
            	}
            	if(data[3]!=null)
            	{
            		entity.setSylbId(Long.parseLong(data[3].toString()));
            	}
            	if(data[4]!=null)
            	{
            		model.setSortName(data[4].toString());
            	}
            	if(data[5]!=null)
            	{
            		entity.setJdsybgName(data[5].toString());
            	}
            	if(data[6]!=null)
            	{
            		entity.setTestPlace(data[6].toString());
            	}
            	if(data[7]!=null)
            	{
            		try {
						entity.setTestDate(sbf.parse(data[7].toString()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
            		model.setStrTestDate(data[7].toString());
            	}
            	if(data[8]!=null)
            	{
            		model.setStrLastTestDate(data[8].toString());
            	}
            	if(data[9]!=null)
            	{
            		model.setStrPlanTestDate(data[9].toString());
            	}
            	if(data[10]!=null)
            	{
            		entity.setTestType(data[10].toString());
            	}
            	if(data[11]!=null)
            	{
            		entity.setWeather(data[11].toString());
            	}
            	if(data[12]!=null)
            	{
            	  entity.setTemperature(Double.parseDouble(data[12].toString()));	
            	}
            	if(data[13]!=null)
            	{
            		entity.setHumidity(Double.parseDouble(data[13].toString()));
            	}
            	if(data[14]!=null)
            	{
            		entity.setTestBy(data[14].toString());
            	}
            	
            	if(data[15]!=null)
            	{
            		entity.setChargeBy(data[15].toString());
            	}
            	if(data[16]!=null)
            	{
            		model.setChargeByName(data[16].toString());
            	}
            	if(data[17]!=null)
            	{
            		entity.setTestSituation(data[17].toString());
            	}
            	if(data[18]!=null)
            	{
            		entity.setContent(data[18].toString());
            	}
            	if(data[19]!=null)
            	{
            		entity.setMemo(data[19].toString());
            	}
            	if(data[20]!=null)
            	{
            		entity.setEnterpriseCode(data[20].toString());
            	}
            	if(data[21] != null)
            	{
            		model.setTestByName(data[21].toString());
            	}
            	model.setReport(entity);
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
	
	public String getLastTestDate(Long deviceId,String enterpriseCode)
	{
		String sql=
			"select to_char(t.test_date, 'yyyy-MM-dd')\n" +
			"from PT_JDBH_J_SYBG t\n" + 
			"where t.device_id="+deviceId+"  and t.enterprise_code='"+enterpriseCode+"' and rownum=1\n" + 
			"order by t.jdsybg_id desc";
		
	   Object obj=bll.getSingal(sql);
	   if(obj!=null)
	   {
		   return obj.toString(); 
	   }
	   else
	   {
		   return "";
	   }
	}


}