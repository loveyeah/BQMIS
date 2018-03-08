package power.web.run.securityproduction.action.danger;


import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jfree.util.ObjectList;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.googlecode.jsonplugin.annotations.JSON;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.metalSupervise.PtJReport;
import power.ejb.run.securityproduction.danger.SpCDangerAssess;
import power.ejb.run.securityproduction.danger.SpCDangerAssessFacade;
import power.ejb.run.securityproduction.danger.SpCDangerAssessFacadeRemote;
import power.ejb.run.securityproduction.danger.SpJDangerDeptRegister;
import power.ejb.run.securityproduction.danger.SpJDangerDeptRegisterFacade;
import power.ejb.run.securityproduction.danger.SpJDangerDeptRegisterFacadeRemote;
import power.ejb.run.securityproduction.danger.SpJDangerDeptValue;
import power.web.comm.AbstractAction;
import power.web.comm.UploadFileAbstractAction;
/**
 * 
 * @author qxjiao 20100729
 *@description 重大危险源报回录下载、汇总action
 *
 */
@SuppressWarnings("serial")
public class DangerReportAction extends UploadFileAbstractAction {
	private SpJDangerDeptRegister model;
	private String start;
	private String limit ;
	private String year ;
	private File annex;
	private SpJDangerDeptRegisterFacadeRemote remote ;
	private SpJDangerDeptRegisterFacade assess ;
	
	//private String en_code = employee.getEnterpriseCode();
	
	public DangerReportAction() {
		remote = (SpJDangerDeptRegisterFacadeRemote)factory.getFacadeRemote("SpJDangerDeptRegisterFacade");
	}
	/**
	 * 功能：查询重大危险源报告列表
	 * @param name 报告名称关键字
	 * @param sort 报告类别
	 * @param start 分页开始页 
	 * @param limit 分页最大结果数
	 */
	@SuppressWarnings("unchecked")
	@JSON(serialize=false)
	public void getDangerReportList(){
		PageObject obj = new PageObject();
		String status = request.getParameter("status");
		List result = remote.findResultList(year, employee.getEnterpriseCode(),employee.getWorkerCode(),status,Integer.parseInt(start) , Integer.parseInt(limit));
		int count = remote.getCount(year, employee.getEnterpriseCode(),employee.getWorkerCode(),status);
		obj.setList(result);
		obj.setTotalCount((long)count);
		
		try {
			write(JSONUtil.serialize(obj));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void queryDangerReport(){
		String status = request.getParameter("status");
		String dangerName = request.getParameter("dangerName");
		PageObject obj = remote.queryDangerList(year, status, dangerName, employee.getEnterpriseCode(), Integer.parseInt(start), Integer.parseInt(limit));
		try {
			write(JSONUtil.serialize(obj));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@JSON(serialize=false)
	public void getValueList(){
		PageObject obj = new PageObject();
		String type = request.getParameter("type");
		List result = remote.findDangerValue(year,type, employee.getEnterpriseCode(),Integer.parseInt(start), Integer.parseInt(limit));
		int count = remote.getValueCount(year,type, employee.getEnterpriseCode());
		obj.setList(result);
		obj.setTotalCount((long)count);
		try {
			System.out.println("json value is :"+JSONUtil.serialize(obj));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			write(JSONUtil.serialize(obj));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 功能：上传附件
	 * 
	 */
	
	public void uploadAnnex(){
		String id = request.getParameter("id");
		SpJDangerDeptRegister entity=remote.findById(Long.parseLong(id));
		String filePath = request.getParameter("filePath");
		System.out.println("传过来的文件名："+filePath);
		model = new SpJDangerDeptRegister();
		if (entity != null) {
			if (!filePath.equals("")) {
				if (entity.getAnnex() != null
						&& filePath.equals(entity.getAnnex().substring(
								entity.getAnnex().lastIndexOf("/") + 1))) {
					
					model.setAnnex(entity.getAnnex());
				} else {
					String result = filePath.substring(filePath
							.lastIndexOf("\\") + 1);
					String fileName = result.replaceAll(" ", "");
					String[] filetemp = fileName.split("\\.");
					if (filetemp[1].equals("txt")) {
						filetemp[1] = ".doc";
						fileName = filetemp[0] + filetemp[1];
					}
					
					String Temp = uploadFile(annex, fileName, "dangerReport");
					System.out.println("上传的文件名："+Temp);
					System.out.println("file path is :"+Temp);
					model.setAnnex(Temp);
				}
			}
			entity.setAnnex(model.getAnnex());
			remote.update(entity);
			write("{success : true,msg :'上传成功！'}");
		}
		
	}
	/**
	 * 
	 * 功能：更新重大危险源报告
	 */
	public void updateTarget(){
		String str = request.getParameter("isUpdate");
		Object object = null;
		try {
			object = JSONUtil.deserialize(str);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("==================="+str);
		List<Map> list = (List<Map>) object;
		if (list!=null&&list.size()>0) {
		for (Map map : list) {
			
			if (map.get("dangerId")!=null) {
				model = remote.findById(Long.parseLong(map.get("dangerId").toString()));
			}
		
			if (map.get("DValue")!=null) {
				model.setDValue(Double.parseDouble(map.get("DValue").toString()));
			}
			if (map.get("d1Value")!=null) {
				model.setD1Value(Double.parseDouble(map.get("d1Value").toString()));
			}
			if (map.get("valueLevel")!=null) {
				model.setValueLevel(Long.parseLong(map.get("valueLevel").toString()));
			}
			model.setLastModifiedBy("999999");
			model.setLastModifiedDate(new Date());
			remote.update(model);
		}
		write("{success:true,msg:'操作成功！'}");
		}
	}
	/**
	 * 功能：删除重大危险源报告
	 */
	public void deleteTarget(){
		String ids = request.getParameter("ids");
		remote.delete(ids);
		write("{success : true,msg :'删除成功！'}");
	}
	
	public void reportRecord(){
		remote.reportRecord(employee.getWorkerCode());
		write("{success : true,msg :'上报成功！'}");
	}
	
	public void confirmReport(){
		remote.confirmReport();
		write("{success : true,msg :'确定成功！'}");
	}
	
	public void checkLBValue(){
		List<Object[]> LBValue = remote.checkLBValue(year, employee.getWorkerCode(), employee.getEnterpriseCode());
		List<Object> nullValue = remote.checkNullBValue();
		String msg = "[";
		for(int i=0 ;i<LBValue.size();i++){
			Object[] obj= LBValue.get(i); 
			if(obj[2]==null&&obj[3]==null&&obj[4]==null&&obj[5]==null&&obj[6]==null&&obj[7]==null&&obj[8]==null&&obj[9]==null&&obj[10]==null&&obj[11]==null){
				
				if("1".equals(obj[1].toString())){
					msg+="{dangerId:"+obj[0]+",valueType:'L'},";
				}else if("2".equals(obj[1].toString())){
					msg+="{dangerId:"+obj[0]+",valueType:'B'},";
				}
			}
		}
		for(int i=0 ;i<nullValue.size();i++){
			Object obj= nullValue.get(i); 
			msg+="{dangerId:"+obj+",valueType:'L'},{dangerId:"+obj+",valueType:'B'},";
		}
		msg +="]";
		this.write(msg);
	}
	
	
	
	public File getAnnex() {
		return annex;
	}
	public void setAnnex(File annex) {
		this.annex = annex;
	}
	public SpJDangerDeptRegisterFacade getAssess() {
		return assess;
	}
	public void setAssess(SpJDangerDeptRegisterFacade assess) {
		this.assess = assess;
	}
	public void setModel(SpJDangerDeptRegister model) {
		this.model = model;
	}
	public SpJDangerDeptRegister getModel() {
		return model;
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
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
}

