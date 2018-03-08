/**
 * 
 */
package power.web.hr.education;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.naming.NamingException;
import com.googlecode.jsonplugin.JSONUtil;

import power.web.comm.AbstractAction;
import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.hr.HrCEducation;
import power.ejb.hr.HrCEducationFacadeRemote;
import power.ejb.hr.dao.EducationDao;

/**
 * @author ustcpower
 *
 */
public class EducationAction extends AbstractAction{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private EducationInfo eduInfo;
	
	public void MyGet() throws Exception {
		String method = request.getParameter("method").toString();
		if(method.equals("getlist"))
		{
			EducationList();
		}
		else if(method.equals("delete"))
		{
			EducationDelete();
		}
		else if(method.equals("add"))
		{
			EducationAdd();
		}
		else if(method.equals("update"))
		{
			EducationUpdate();
		}
		else if(method.equals("getdata"))
		{
			getDataForUpdate();
		}
	}
	
	public void EducationList() throws Exception
	{
		 Integer[] counts = {0};
		  int start = Integer.parseInt(request.getParameter("start"));
			
			int limit = Integer.parseInt(request.getParameter("limit"));
			
			int end = start + limit;
			EducationDao dao=new EducationDao();
			List list= dao.GetEducationList(start+1, end, counts);
			int count=counts[0];
			Iterator it=list.iterator();
		    List eduList = new ArrayList();
		    String use="";
		   
			while(it.hasNext())
			{
				EducationInfo model=new EducationInfo();
				Object[] data =(Object[]) it.next();
				if(data[0]!=null)
				{
					model.setEducationId(data[0].toString());
				}
				if(data[1]!=null)
				{
					model.setEducationName(data[1].toString());
				}
				if(data[2]!=null)
				{
					if(data[2].toString().equals("Y"))//update by sychen 20100901
//						if(data[2].toString().equals("U"))
					{
						use="使用";
					}
					else if(data[2].toString().equals("N"))
					{
						use="停用";
					}
					else if(data[2].toString().equals("L"))
					{
						use="注销";
					}
					else
					{
						use="";
					}
					model.setIsUse(use);
				}
				if(data[3]!=null)
				{
					model.setRetrieveCode(data[3].toString());
				}
				eduList.add(model);
			}
			
			String	str = "{total :"+count+", root:" + JSONUtil.serialize(eduList) + "}";

			write(str);
		
	}

	public void EducationAdd() throws NamingException, IOException
	{
		HrCEducationFacadeRemote bll = (HrCEducationFacadeRemote) factory
				.getFacadeRemote("HrCEducationFacade");
		EducationDao dao = new EducationDao();
		HrCEducation model = new HrCEducation();
		try {
			if (eduInfo != null) { 
				model.setEducationName(eduInfo.getEducationName());
				model.setRetrieveCode(eduInfo.getRetrieveCode());
				model.setIsUse(eduInfo.getIsUse()); 
				bll.save(model);
			}
			String str = "{success: true,msg:\'增加成功\'}";
			write(str);
		} catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
	}
	
	public void getDataForUpdate() throws Exception {

		long id = 0;
		Object obj = request.getParameter("id");
		id = Long.parseLong(obj.toString()); 
		HrCEducationFacadeRemote bll = (HrCEducationFacadeRemote) factory.getFacadeRemote("HrCEducationFacade");
		HrCEducation model = bll.findById(id);
		String str = JSONUtil.serialize(model);
		write("{success: true,data:" + str + "}");

	}
	
	public void EducationUpdate() throws Exception{
		HrCEducationFacadeRemote bll = (HrCEducationFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrCEducationFacade");
		HrCEducation model = new HrCEducation();
		try {
			if (model != null) {
				model.setEducationId(Long.parseLong(eduInfo.getEducationId()));
				model.setEducationName(eduInfo.getEducationName());
				model.setIsUse(eduInfo.getIsUse());
				model.setRetrieveCode(eduInfo.getRetrieveCode());
				bll.update(model);
				String str = "{success: true,msg:\'修改成功\'}";
				write(str);
			}
		} catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
			
	}


	
	public void EducationDelete() throws Exception
	{
		String ids = request.getParameter("ids");
		String []myids = ids.split(",");
		for(int i=0;i<myids.length;i++)
		{
			if(!myids[i].equals(""))
			{
				Long myid = Long.parseLong(myids[i]);
				EducationDao edudao = new EducationDao();
				edudao.DeleteEdu(myid);
				String str = "{success:true,msg:\'ok\'}";
				write(str);
			}
		}
	}

	public EducationInfo getEduInfo() {
		return eduInfo;
	}

	public void setEduInfo(EducationInfo eduInfo) {
		this.eduInfo = eduInfo;
	}
	
}