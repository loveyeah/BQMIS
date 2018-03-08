package power.web.hr.degree;

import java.util.ArrayList;
import java.util.List;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrCDegree;
import power.ejb.hr.HrCDegreeFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONUtil;
/**
 * @author fyyang 
 * 学位基本信息
 */


public class DegreeAction extends AbstractAction{ 
	HrCDegreeFacadeRemote bll = (HrCDegreeFacadeRemote) factory.getFacadeRemote("HrCDegreeFacade"); 
	private int start;
	private int limit;
	
	
	private DegreeModel degreemodel;
	public DegreeModel getDegreemodel() {
		return degreemodel;
	}

	public void setDegreemodel(DegreeModel degreemodel) {
		this.degreemodel = degreemodel;
	}
	
	
	public void getDegree() throws Exception{
		String method=request.getParameter("method");
		if(method.equals("query"))
		{
			getList();
		}
		else if(method.equals("getdata"))
		{
			getDataForUpdate();
		}
		else if(method.equals("delete"))
		{
			deleteDegree();
		
		}
		else if(method.equals("add"))
		{
			addDegree();
		}
		else if(method.equals("update"))
		{
			updateDegree();
		}
	
	}
	
	//获得学位信息列表
	public void getList() throws Exception{  
			PageObject result = bll.findAll(start,limit);
			List<HrCDegree> list= result.getList();
			Long count=result.getTotalCount();
		    List<DegreeModel> blocklist = new ArrayList<DegreeModel>();  
			for(HrCDegree m : list)
			{
				DegreeModel model=new DegreeModel();  
				model.setDegreeId( m.getDegreeId()==null?"": m.getDegreeId().toString()); 
				model.setDegreeName(m.getDegreeName()==null?"":m.getDegreeName()); 
				model.setRetrieveCode(m.getRetrieveCode()==null?"":m.getRetrieveCode());
				String use = ""; 
				if(m.getIsUse()!=null)
				{
					if(m.getIsUse().equals("Y"))//update by sychen 20100831
//						if(m.getIsUse().equals("U"))
					{
						use="使用";
					}
					else if(m.getIsUse().equals("N"))
					{
						use="停用";
					}
					else if(m.getIsUse().equals("L"))
					{
						use="注销";
					} 
					model.setIsUse(use);
				}  
				blocklist.add(model);
			}  
			String	str = "{total :"+count+", root:" + JSONUtil.serialize(blocklist) + "}"; 
			write(str);
		
	}
	//删除学位信息
	public void deleteDegree() throws Exception
	{ 
		 String ids= request.getParameter("ids");
		 if(ids != null && !ids.equals(""))
		 {
			 bll.delete(ids);
			 String	str = "{success: true,msg:\'ok\'}";
			 write(str);
		 }
	}
	
	//增加学位信息
	public void addDegree()  {
		HrCDegree model = new HrCDegree();
		HrCDegreeFacadeRemote bll = (HrCDegreeFacadeRemote) factory.getFacadeRemote("HrCDegreeFacade");
		try {
			if (degreemodel != null) {
				model.setDegreeName(degreemodel.getDegreeName());
				model.setRetrieveCode(degreemodel.getRetrieveCode());
				model.setIsUse(degreemodel.getIsUse());
				bll.save(model);
			}
			String str = "{success: true,msg:\'增加成功\'}";
			write(str);
		} catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
	}
	
	// 获得一条学位信息记录
	public void getDataForUpdate() throws Exception{
	
		 long id=0;
			Object obj=request.getParameter("id");
			id=Long.parseLong(obj.toString());
			HrCDegreeFacadeRemote bll=(HrCDegreeFacadeRemote)Ejb3Factory
			.getInstance().getFacadeRemote("HrCDegreeFacade");
			HrCDegree model=bll.findById(id);
			 String str=JSONUtil.serialize(model);
			 write("{success: true,data:"+str+"}");
		
		
	}
	
	//修改学位信息
	public void updateDegree(){
		HrCDegreeFacadeRemote bll=(HrCDegreeFacadeRemote)factory.getFacadeRemote("HrCDegreeFacade");
		HrCDegree model=new HrCDegree();
		try{
			if(model!=null)
			{
				model.setDegreeId(Long.parseLong(degreemodel.getDegreeId()));
			
				model.setDegreeName(degreemodel.getDegreeName());
				model.setIsUse(degreemodel.getIsUse());
				model.setRetrieveCode(degreemodel.getRetrieveCode());
				bll.update(model);
				String	str = "{success: true,msg:\'修改成功\'}";
				  write(str);
			}
		} catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
			
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}




}
