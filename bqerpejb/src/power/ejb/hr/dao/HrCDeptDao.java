package power.ejb.hr.dao;

import java.util.List;

import javax.naming.NamingException;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.comm.NativeSqlHelperRemote;

public class HrCDeptDao {	
	/**
	 * 输入上级节点ID查询是否含有下级节点（人力部门表）
	 */
	public boolean hasChild(long pid) {
		try {
			List powerplant = findByProperty(String.valueOf(pid));
			if (powerplant != null && powerplant.size() > 0)
				return true;
			else
				return false;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	// 根据单一属性查询所有符合条件的记录
	private List findByProperty(String str) throws NamingException {
		try {
			String queryString = String.format(
					"select * from hr_c_dept v where v.is_use ='Y' and v.pdept_id =%s", str); //update by sychen 20100902 
//			"select * from hr_c_dept v where v.is_use ='U' and v.pdept_id =%s", str); 
			NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
					.getInstance().getFacadeRemote("NativeSqlHelper");

			List hrcdept = bll.queryByNativeSQL(queryString);
//			List list=new ArrayList();
//			if(hrcdept!=null && hrcdept.size()>0){
//				for(HrCDept o:hrcdept){
//					System.out.println(o.getIsUse());
//					if(o.getIsUse().equals('U'))
//						list.add(o);
//				}
//			}
			return hrcdept;
		} catch (RuntimeException re) {
			throw re;
		}
	}
}