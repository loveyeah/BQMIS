package power.web.run.securityproduction.action;

import java.text.ParseException;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.run.securityproduction.form.SafetyMonthForm;
import power.ejb.run.securityproduction.SpJSafetyMonth;
import power.ejb.run.securityproduction.SpJSafetyMonthFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class SafetyMonthAction  extends AbstractAction{
	private SpJSafetyMonth safetyMonth;
	private SpJSafetyMonthFacadeRemote safetyMonthRemote;
	private String  month;
	
	public SafetyMonthAction() {    
		safetyMonthRemote = (SpJSafetyMonthFacadeRemote)factory.getFacadeRemote("SpJSafetyMonthFacade");
	}
	/**
	 * 增加记录
	 * @throws Exception
	 */
	public void addSafetyMonth() throws Exception {
		String month = request.getParameter("month");
		java.text.SimpleDateFormat formatter= new java.text.SimpleDateFormat("yyyy-MM");
		safetyMonth.setEnterpriseCode(employee.getEnterpriseCode());
		safetyMonth.setMonth(formatter.parse(month));
		SpJSafetyMonth  temp = safetyMonthRemote.save(safetyMonth);
		if(temp == null)
			write("{failure:true,msg:'数据库已经有了该月的数据!'}");
		else write("{success:true,msg:'增 加 成 功!',safetymonthId:"+temp.getSafetymonthId()+"}");
	}
	/**
	 * 更新记录
	 * @throws ParseException
	 */
	public void updateSafetyMonth() throws ParseException {
		String month = request.getParameter("month");
		System.out.println(month);
		String safetyMonthId = request.getParameter("safetymonthId");
		java.text.SimpleDateFormat formatter= new java.text.SimpleDateFormat("yyyy-MM");
		SpJSafetyMonth entity = safetyMonthRemote.findById(Long.parseLong(safetyMonthId));
		entity.setFillDate(safetyMonth.getFillDate());
		entity.setFillBy(safetyMonth.getFillBy());
		entity.setSummary(safetyMonth.getSummary());
		entity.setContent(safetyMonth.getContent());
		entity.setSubject(safetyMonth.getSubject());
		entity.setMonth(formatter.parse(month));
		SpJSafetyMonth  temp = safetyMonthRemote.update(entity);
		if(temp == null)
			write("{failure:true,msg:'数据库已经有了该月的数据!'}");
		else write("{success:true,msg:'修 改 成 功!',safetymonthId:"+temp.getSafetymonthId()+"}");
	}
	/**
	 * 删除记录
	 * @throws Exception
	 */
	public void deleteSafetyMonth() throws Exception {
		 if(safetyMonthRemote.delete(safetyMonth))
		 write("{success:true,msg:'删 除 成 功!'}");
		 else write("{success:false,msg:'删 除 失 败 ！'}");
	}
	/**
	 * 获取记录列表
	 * 
	 * @throws JSONException
	 * 
	 */
	public void getSafetyMonthInfo() throws JSONException {
		String month = request.getParameter("month");
		SafetyMonthForm model= new SafetyMonthForm();
		if(month != null)
			model = safetyMonthRemote.findModelForm(month, employee.getEnterpriseCode());
		write(JSONUtil.serialize(model));
	}
	/**
	 * 检测数据是否唯一
	 * @return
	 */
	
	public SpJSafetyMonth getSafetyMonth() {
		return safetyMonth;
	}
	public void setSafetyMonth(SpJSafetyMonth safetyMonth) {
		this.safetyMonth = safetyMonth;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
}
