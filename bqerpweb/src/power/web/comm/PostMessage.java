package power.web.comm;
import java.io.DataInputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.db.DBHelper;
import com.opensymphony.engineassistant.po.Group;
import com.opensymphony.engineassistant.po.WorkflowActivity;
import com.opensymphony.engineassistant.util.RightControl;
import com.opensymphony.engineassistant.util.RightControlImpl;
import com.opensymphony.engineassistant.util.RunFlowFileInfo;
import com.opensymphony.engineassistant.util.RunFlowFileInfoJdbcImpl;
import com.opensymphony.engineassistant.util.WorkflowUtil;


import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.basedata.BaseDataManager;
import power.ejb.comm.NativeSqlHelperRemote;


public class PostMessage {
	
	public  boolean sendShortMsg(String tels,String msg)
	{
		boolean success = false;
		URL url = null;   
	    HttpURLConnection httpurlconnection = null; 
	    try {   
	        url = new URL("http://120.192.206.134:8080/hsmsg/msg/send.action");   
	        httpurlconnection = (HttpURLConnection) url.openConnection();   
	        httpurlconnection.setDoInput(true);   
	        httpurlconnection.setDoOutput(true);   
	        httpurlconnection.setRequestMethod("POST");
	        httpurlconnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");   
	        String username = "circle=admin&pwd=admin&mobile=".concat(tels).concat("&message=".concat(URLEncoder.encode(msg,"UTF-8")));
	        httpurlconnection.getOutputStream().write(username.getBytes());
	        httpurlconnection.getOutputStream().flush();   
	        httpurlconnection.getOutputStream().close();   
	        int code = httpurlconnection.getResponseCode();   
	       // System.out.println("目标页面处理结果:" + code); 
	        if (code == 200) {
	            DataInputStream in = new DataInputStream(httpurlconnection   
	                    .getInputStream());   
	            int len = in.available();   
	            byte[] by = new byte[len];   
	            in.readFully(by);   
	            String rev = new String(by);  
	            in.close();   
	            if("0".equals(rev));
	            success = true;
	        }   
	    } catch (Exception e) {   
	        e.printStackTrace();   
	    } finally {   
	        if (httpurlconnection != null) {   
	            httpurlconnection.disconnect();   
	        }   
	    } 
	    return success;
	}
	@SuppressWarnings("unchecked")
	public  void sendMsg(String roleIds,String msg) {   
         //发短信每次不能超过200个人，且移动联通电信分开发
		String [] arrRoles=roleIds.split(",");
		roleIds="";
		for(int i=0;i<arrRoles.length;i++)
		{
			if(!arrRoles[i].equals(""))
			{
			if(roleIds.equals(""))roleIds="'"+arrRoles[i]+"'";
			else
			{
				roleIds += ",'"+arrRoles[i]+"'";
			}
			}
		}
		
		List list= this.findUserPhone(roleIds,"1");
		String phones=""; //移动
		String ltPhones="";//联通
		String dxPhones="";//电信
		int num=0;
		int ltNum=0;
		int dxNum=0;
		for(int i=0;i<list.size();i++)
		{
			Object [] obj=(Object [] )list.get(i);
			if(obj[1]!=null&&!obj[1].equals(""))
			{
			 if(num==199)
			 {
				 this.sendShortMsg(phones, msg);
				 phones="";
				 num=0;
			 }
			 if(dxNum==199)
			 {
				 this.sendShortMsg(dxPhones, msg);
				 dxPhones="";
				 dxNum=0;
			 }
			 if(ltNum==199)
			 {
				 this.sendShortMsg(ltPhones, msg);
				 ltPhones="";
				 ltNum=0;
			 }
			 
			 if( "133,153,187,189".indexOf(obj[1].toString().substring(1,3))!=-1)
			 {
				 if(dxPhones.equals("")) dxPhones=obj[1].toString();
				 else dxPhones+=","+obj[1].toString();
				 dxNum++;
			 }else  if( "130,131,132,155,156".indexOf(obj[1].toString().substring(1,3))!=-1)
			 {
				 if(ltPhones.equals("")) ltPhones=obj[1].toString();
				 else ltPhones+=","+obj[1].toString();
				 ltNum++;
			 }
			 else
			 {
			 if(phones.equals("")) phones=obj[1].toString();
			 else phones+=","+obj[1].toString();
			 num++;
			 }
			
			}
		}
	
		System.out.println("手机号：移动"+phones+"联通："+ltPhones+"电信"+dxPhones+"信息："+msg+num);
		if(!phones.equals("")&&num!=199)
		{
			this.sendShortMsg(phones, msg);
		}
		if(!ltPhones.equals("")&&ltNum!=199)
		{
			this.sendShortMsg(ltPhones, msg);
		}
		if(!dxPhones.equals("")&&dxNum!=199)
		{
			this.sendShortMsg(dxPhones, msg);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void sendMsgByWorker(String workCodes,String msg)
	{
		 //发短信每次不能超过200个人，且移动联通电信分开发
		String [] arrCodes=workCodes.split(",");
		workCodes="";
		for(int i=0;i<arrCodes.length;i++)
		{
			if(!arrCodes[i].equals(""))
			{
			if(workCodes.equals(""))workCodes="'"+arrCodes[i]+"'";
			else
			{
				workCodes += ",'"+arrCodes[i]+"'";
			}
			}
		}

		List list= this.findUserPhone(workCodes,"2");
		String phones=""; //移动
		String ltPhones="";//联通
		String dxPhones="";//电信
		int num=0;
		int ltNum=0;
		int dxNum=0;
		for(int i=0;i<list.size();i++)
		{
			Object [] obj=(Object [] )list.get(i);
			if(obj[1]!=null&&!obj[1].equals(""))
			{
			 if(num==199)
			 {
				 this.sendShortMsg(phones, msg);
				 phones="";
				 num=0;
			 }
			 if(dxNum==199)
			 {
				 this.sendShortMsg(dxPhones, msg);
				 dxPhones="";
				 dxNum=0;
			 }
			 if(ltNum==199)
			 {
				 this.sendShortMsg(ltPhones, msg);
				 ltPhones="";
				 ltNum=0;
			 }
			 
			 if( "133,153,187,189".indexOf(obj[1].toString().substring(1,3))!=-1)
			 {
				 if(dxPhones.equals("")) dxPhones=obj[1].toString();
				 else dxPhones+=","+obj[1].toString();
				 dxNum++;
			 }else  if( "130,131,132,155,156".indexOf(obj[1].toString().substring(1,3))!=-1)
			 {
				 if(ltPhones.equals("")) ltPhones=obj[1].toString();
				 else ltPhones+=","+obj[1].toString();
				 ltNum++;
			 }
			 else
			 {
			 if(phones.equals("")) phones=obj[1].toString();
			 else phones+=","+obj[1].toString();
			 num++;
			 }
			
			}
		}
	
		System.out.println("手机号：移动"+phones+"联通："+ltPhones+"电信"+dxPhones+"信息："+msg+num);
		if(!phones.equals("")&&num!=199)
		{
			this.sendShortMsg(phones, msg);
		}
		if(!ltPhones.equals("")&&ltNum!=199)
		{
			this.sendShortMsg(ltPhones, msg);
		}
		if(!dxPhones.equals("")&&dxNum!=199)
		{
			this.sendShortMsg(dxPhones, msg);
		}
	}
	
	@SuppressWarnings("unchecked")
	public String getEquFailuresRoles(String specialName){
		String roleIds="";
		String sql = "select r.role_id from sys_c_roles r where r.role_name like '" + specialName + "%'";
		List<Map<String, Object>> result;
		result = DBHelper.query(sql);
		if (result != null && result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				Map m = result.get(i);
				if(roleIds.equals("")) roleIds=m.get("ROLE_ID").toString();
				else
				{
					roleIds +=","+m.get("ROLE_ID").toString();
				}
			}
		}
		return roleIds;
	}
	
	@SuppressWarnings("unchecked")
	public String getFistStepRoles(String flowCode,String actionIdStr,String deptId,String sepeciality)
	{
		
		String roleIds="";
			List<Map<String, Object>> result;
			Long actionId = Long.parseLong(actionIdStr);
			String sql = "select distinct f.role_id, f.role_name, f.accredit_section\n"
					+ "  from wf_c_type a, wf_c_step b, wf_c_event c, wf_c_step d,wf_j_rrs e,sys_c_roles f\n"
					+ " where a.flow_type = b.flow_type\n"
					+ "   and a.flow_type = c.flow_type\n"
					+ "   and a.flow_type = d.flow_type\n"
					+ "   and a.flow_type = e.flow_type\n"
					+ "   and b.step_id = c.from_step_id\n"
					+ "   and c.to_step_id = d.step_id\n"
					+ "   and d.step_id=e.step_id\n"
					+ "   and e.role_id = f.role_id\n"
					+ "   and f.is_use='Y'\n"
					+ "   and a.is_use = 'Y'\n"
					+ "   and a.flow_code = ?\n"
					+ "   and b.step_type = ?\n"
					+ "   and c.event_id = ?\n";
			if (deptId != null && !"".equals(deptId)) {
				sql += "and ( f.accredit_section = '' or   f.accredit_section is null or\n"
						+ " instr(  (select  sys_connect_by_path(dept_id,',')||','   path\n"
						+ "     from hr_c_dept\n"
						+ "     where  dept_id=?\n"
						+ "     start   with   pdept_id=-1\n"
						+ "     connect   by   prior   dept_id=pdept_id and is_use='Y'),','||f.accredit_section||',')<>0)";  //update by sychen 20100902
//				+ "     connect   by   prior   dept_id=pdept_id and is_use='U'),','||f.accredit_section||',')<>0)"; 
				result = DBHelper.query(sql,
						new Object[] { flowCode,
								WorkflowUtil.WORKFLOW_START_STEP, actionId,
								deptId });
			} else {
				result =  DBHelper.query(sql, new Object[] { flowCode,
						WorkflowUtil.WORKFLOW_START_STEP, actionId });
			}

			if(sepeciality != null && !"".equals(sepeciality))
			{
				sql += " and f.role_name like  '%"+ sepeciality +"%'\n";
				result = DBHelper.query(sql, new Object[] { flowCode,
						WorkflowUtil.WORKFLOW_START_STEP, actionId});
			}
			if (result != null && result.size() > 0) {
				for (int i = 0; i < result.size(); i++) {
					Map m = result.get(i);
					if(roleIds.equals("")) roleIds=m.get("ROLE_ID").toString();
					else
					{
						roleIds +=","+m.get("ROLE_ID").toString();
					}
				}
			}
			return roleIds;
		
		}
	
	@SuppressWarnings("unchecked")
	public String  getNextSetpRoles(String entryId,String actionId,String ... special) 
	{
		String roleIds="";
		RunFlowFileInfo wfControl = new RunFlowFileInfoJdbcImpl(null);
	
		List<WorkflowActivity> steps = wfControl.getNextSteps(Long
				.parseLong(entryId), Long.parseLong(actionId));
		if (steps != null) {
			WorkflowActivity model=steps.get(0);
			 RightControl rightControl= new RightControlImpl();
		
			Long stepId = model.getStepId();
			List<Group> list = rightControl.getGroups(Long
					.parseLong(entryId), stepId);
			for(Group map:list)
			{
				if(special!=null&&special.length>0)
				{
					if(map.getGroupName().equals(special[0]))
					{
				     if(roleIds.equals("")) roleIds=map.getGroupId()+"";
				      else roleIds +=","+map.getGroupId();
					}
				}else
				{
					
				     if(roleIds.equals("")) roleIds=map.getGroupId()+"";
				      else roleIds +=","+map.getGroupId();
					
				}
			}
		}
		//System.out.println(roleIds);
		return roleIds;
	}
	
	
	@SuppressWarnings("unchecked")
	 private List findUserPhone(String ids,String flag)
	{
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
		.getInstance().getFacadeRemote("NativeSqlHelper");
		List list=new ArrayList();
		String sql="";
		if(flag.equals("1"))
		{
		sql=	"select distinct b.chs_name,b.mobile_phone\n" +
			"  from sys_c_ul a, hr_j_emp_info b\n" + 
			" where a.worker_code = b.emp_code\n" + 
			"   and a.is_use = 'Y'\n" + 
			"   and b.is_use = 'Y'\n" + 
			"   and a.worker_id in (select distinct t.worker_id\n" + 
			"                         from sys_j_ur t\n" + 
			"                        where t.role_id in ("+ids+")\n" + 
			"                          and t.is_use = 'Y')";
		}
		else
		{
			sql=
				"select distinct b.chs_name, b.mobile_phone\n" +
				"  from hr_j_emp_info b\n" + 
				" where b.emp_code in ("+ids+")\n" + 
				"   and b.is_use = 'Y'";

		}
		list=bll.queryByNativeSQL(sql);
        return list;
		
	}
	
	
}
