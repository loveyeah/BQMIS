package power.ejb.workticket.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.workticket.form.ChargerForm;
@Stateless
public class WorkticketChargerImpl implements WorkticketCharger{
	@SuppressWarnings("unused")
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public List<ChargerForm> findWatcher(String userName,final int... rowStartIdxAndCount){
		String sql= 

			"select a.worker_code, a.worker_name\n" +
			"  from sys_c_ul a, sys_j_ur b\n" + 
			" where a.worker_id = b.worker_id\n" + 
			"   and b.role_id = 75\n" + 
			"   and a.worker_name||a.worker_code like ?\n" + 
			"   and a.is_use=b.is_use\n" + 
			"   and a.is_use='Y'"; 
		List list=bll.queryByNativeSQL(sql, new Object[]{"%"+userName+"%"},rowStartIdxAndCount);
		List<ChargerForm> arrlist=new ArrayList();
		Iterator it=list.iterator();
		while(it.hasNext()){
			Object[] o=(Object[])it.next();
			ChargerForm form=new ChargerForm(); 
			if(o[0]!=null)
				form.setWorkerCode(o[0].toString());
			if(o[1]!=null)
				form.setWorkerName(o[1].toString());  
			arrlist.add(form);
			}
		return arrlist;
	}
	
	/**
	 * 查询在某个部门下的某种工作票负责人
	 * @param String workticketTypeName 工作票类型名称
	 * @param String userName 用户工号或者姓名
	 */
	public List<ChargerForm> findCharger(String workticketTypeName,String userName){
		workticketTypeName+="负责人";
		String sql=
			"select \n" +
			" u.worker_id,\n" +
			" u.worker_code,\n" + 
			" u.worker_name,\n"+
			" GETDEPTBYWORKCODE(u.worker_code) dept_code,\n"+
			" GETDEPTNAME(GETDEPTBYWORKCODE(u.worker_code)) dept_name"+
			"  from sys_c_ul u, sys_c_roles r, sys_j_ur t\n" + 
			" where r.role_name =?\n" + 
			"   and u.worker_id = t.worker_id\n" + 
			"   and t.role_id = r.role_id\n" + 
			"   and u.is_use='Y'\n" + 
			"   and r.is_use='Y'\n" + 
			"   and t.is_use='Y'\n" + 
			"   and u.worker_code||u.worker_name like ?";
		List list=bll.queryByNativeSQL(sql, new Object[]{workticketTypeName,"%"+userName+"%"});
		List<ChargerForm> arrlist=new ArrayList();
		Iterator it=list.iterator();
		while(it.hasNext()){
			Object[] o=(Object[])it.next();
			ChargerForm form=new ChargerForm();
			if(o[0]!=null)
				form.setWorkerId(Long.parseLong(o[0].toString()));
			if(o[1]!=null)
				form.setWorkerCode(o[1].toString());
			if(o[2]!=null)
				form.setWorkerName(o[2].toString());
			if(o[3]!=null)
				form.setDeptCode(o[3].toString());
			if(o[4]!=null)
				form.setDeptName(o[4].toString());
			arrlist.add(form);
			}
		return arrlist;
	}
}
