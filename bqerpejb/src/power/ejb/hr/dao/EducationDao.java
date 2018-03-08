/**
 * 
 */
package power.ejb.hr.dao;

import java.util.List;

import javax.naming.NamingException;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.HrCEducation;
import power.ejb.hr.HrCEducationFacadeRemote;


/**
 * @author ustcpower
 *
 */
public class EducationDao {
	public void DeleteEdu(long educationId) throws Exception {
		HrCEducationFacadeRemote bll = (HrCEducationFacadeRemote) Ejb3Factory
		.getInstance().getFacadeRemote("HrCEducationFacade");
		
		HrCEducation model=bll.findById(educationId);
		bll.delete(model);
	}
	
public List GetEducationList(int start,int end,Integer [] count) throws Exception{
		
		
		String sql=
			"select * from\n" +
			"(\n" + 
			" select a.education_id,a.education_name,a.is_use,a.retrieve_code,(row_number() over(order by a.education_id)) rb from hr_c_education a\n" + 
			" ) tt\n" + 
			" where tt.rb between  "+start+"  and  "+end;
		
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
		.getInstance().getFacadeRemote("NativeSqlHelper");
		
		List list=bll.queryByNativeSQL(sql);
		
		String sqlcount="select count(*) from hr_c_education";
		count[0]=Integer.parseInt(bll.getSingal(sqlcount).toString());
		return list;

}

//	public long GetEducationMaxId() throws NamingException
//	{
//		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
//		.getInstance().getFacadeRemote("NativeSqlHelper");
//		long id = 0;
//		String sql="select nvl(max(education_id)+1,1) from hr_c_education";
//		Object obj= bll.getSingal(sql);
//		if(obj != null){
//			id=Long.parseLong(obj.toString());
//		}
//		return id;
//	}
}
