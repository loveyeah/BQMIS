/**
 * 
 */
package power.web.hr.emptype;
import java.util.List;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.HrCEmpType;
import power.ejb.hr.HrCEmpTypeFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
/**
 * @author fish
 *
 */
public class EmpTypeAction extends AbstractAction {
	private String start;
	private String limit;
	private String empTypeId;
	private String empTypeName;
	private String value;
	private String retrieveCode;
	
	
	HrCEmpTypeFacadeRemote bll;
	public void empTypeMaint() throws Exception
	{
		String method=request.getParameter("method");
		try
		{
			if("list".equals(method))
			{
				GetEmpTypeList();
			}
			else if("add".equals(method))
			{
				addEmpType(); 
				
			}
			else if("update".equals(method))
			{ 
				update(); 
				
			}
			else if("delete".equals(method))
			{
				delete();
				write("{success:true}");
			}
			else if("convert".equals(method))
			{
				convert();
				write("{success:true}");
			}
		}
		catch(Exception exc)
		{
			write("{failure:true,data:'"+exc.getMessage()+"'}");
		}
	}
	//获取员工类别列表
	public void GetEmpTypeList() throws Exception{
		ListRange<HrCEmpType> lTtype= new ListRange<HrCEmpType>();
		bll=(HrCEmpTypeFacadeRemote) Ejb3Factory.getInstance().getFacadeRemote("HrCEmpTypeFacade");
			int[] obj = { Integer.parseInt(start), Integer.parseInt(limit)}; 
			String strWhere="model.isUse<>";
			String param="N"; //update by sychen 20100901
//			String param="D"; 
			List<HrCEmpType> list=bll.findByPropertys(strWhere, param, obj);
			lTtype.setTotalProperty(GetALLRoleCount());
			lTtype.setRoot(list);
			String retlist=JSONUtil.serialize(lTtype);
			write(retlist);
	}
	//获取总记录数
	private int GetALLRoleCount() throws Exception{
		int counts = 0;
		String sqlstr = "select count(1) from hr_c_emp_type where emp_type_id > 0 And is_use<>'N'";//update by sychen 20100901
//		String sqlstr = "select count(1) from hr_c_emp_type where emp_type_id > 0 And is_use<>'D'";
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
					.getInstance().getFacadeRemote("NativeSqlHelper");
		Object ob = bll.getSingal(sqlstr);
		counts = Integer.parseInt(ob.toString());
		return counts;
	}
	//新增员工类别记录
	public void addEmpType() throws JSONException{
		HrCEmpType model=new HrCEmpType();
		HrCEmpTypeFacadeRemote bll=(HrCEmpTypeFacadeRemote) factory.getFacadeRemote("HrCEmpTypeFacade");	
		model.setEmpTypeName(getEmpTypeName());
		model.setIsUse(getValue());
		model.setRetrieveCode(getRetrieveCode()); 
		try {
			bll.save(model);
		} catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}  
		write("{success:true,data:'"+JSONUtil.serialize(model)+"'}");
		 
	}
	//获取一条员工类别记录
	public void getEmptypeModel() throws Exception{
		HrCEmpTypeFacadeRemote bll=(HrCEmpTypeFacadeRemote) Ejb3Factory.getInstance().getFacadeRemote("HrCEmpTypeFacade");
		HrCEmpType model=new HrCEmpType();
		long id=Long.parseLong(request.getParameter("id"));
		model=bll.findById(id);
		String jsonStr="{model:"+JSONUtil.serialize(model)+"}";
		write(jsonStr);
	}
	//删除记录
	public void delete() throws Exception{
		HrCEmpTypeFacadeRemote bll=(HrCEmpTypeFacadeRemote) Ejb3Factory.getInstance().getFacadeRemote("HrCEmpTypeFacade");
		String ids=request.getParameter("ids");
		HrCEmpType model=new HrCEmpType();
		if(ids != null && ids.length()>0)
		{
			String [] array=ids.split(",");
			for(String id : array)
			{
				long nid=Long.parseLong(id);
				model=bll.findById(nid);
				model.setIsUse("N"); //update by sychen 20100901
//				model.setIsUse("D");
				bll.update(model);
			}
		}
	}
	//修改记录
	public void update() throws JSONException{
		HrCEmpTypeFacadeRemote bll=(HrCEmpTypeFacadeRemote) Ejb3Factory.getInstance().getFacadeRemote("HrCEmpTypeFacade");
		HrCEmpType model=new HrCEmpType();
		model.setEmpTypeId(Long.parseLong(getEmpTypeId()));
		model.setEmpTypeName(getEmpTypeName());
		model.setIsUse(getValue());
		model.setRetrieveCode(getRetrieveCode());
		try {
			bll.update(model);
		} catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
		write("{success:true,data:'"+JSONUtil.serialize(model)+"'}");
	 
	}
	public void convert() throws Exception{
		String typename=request.getParameter("typename");
		if(!typename.equals(""))
		{
		String sqlstr = String
		.format("select pkg_com.fn_get_initial_upper('%s') from dual",typename);
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
		.getInstance().getFacadeRemote("NativeSqlHelper");
		Object ob = bll.getSingal(sqlstr);
		String retrievecode=(ob.toString());
		String jsonStr=JSONUtil.serialize(retrievecode);
		write(jsonStr);
		}
		
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getLimit() {
		return limit;
	}
	public void setLimit(String limit) {
		this.limit = limit;
	}
	public String getEmpTypeId() {
		return empTypeId;
	}
	public void setEmpTypeId(String empTypeId) {
		this.empTypeId = empTypeId;
	}
	public String getEmpTypeName() {
		return empTypeName;
	}
	public void setEmpTypeName(String empTypeName) {
		this.empTypeName = empTypeName;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRetrieveCode() {
		return retrieveCode;
	}
	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}
	

}
