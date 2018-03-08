package power.web.run.securityproduction.action.danger;


import java.io.File;
import java.util.Date;
import java.util.List;

import org.jfree.util.ObjectList;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.metalSupervise.PtJReport;
import power.ejb.run.securityproduction.danger.SpCDangerAssess;
import power.ejb.run.securityproduction.danger.SpCDangerAssessFacade;
import power.ejb.run.securityproduction.danger.SpCDangerAssessFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.UploadFileAbstractAction;
/**
 * 
 * @author qxjiao 20100729
 *@description 重大危险源报告action
 *
 */
@SuppressWarnings("serial")
public class DangerAssessAction extends UploadFileAbstractAction {
	private SpCDangerAssess model;
	
	private File annex;
	private SpCDangerAssessFacadeRemote remote ;
	private SpCDangerAssessFacade assess ;
	
	public SpCDangerAssessFacade getAssess() {
		return assess;
	}
	public void setAssess(SpCDangerAssessFacade assess) {
		this.assess = assess;
	}
	public DangerAssessAction() {
		remote = (SpCDangerAssessFacadeRemote)factory.getFacadeRemote("SpCDangerAssessFacade");
	}
	/**
	 * 功能：查询重大危险源报告列表
	 * @param name 报告名称关键字
	 * @param sort 报告类别
	 * @param start 分页开始页 
	 * @param limit 分页最大结果数
	 */
	@SuppressWarnings("unchecked")
	public void getDangerAssessList(){
		String name = request.getParameter("name")==null?"":request.getParameter("name");
		String sort = request.getParameter("sort")==null?"":request.getParameter("sort");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject resultList = new PageObject();
		List result = remote.findByKeyword(name,sort,employee.getEnterpriseCode(),Integer.parseInt(start),Integer.parseInt(limit));
		int count = remote.getCount(name, sort,employee.getEnterpriseCode());
		resultList.setTotalCount((long)count);
		resultList.setList(result);
		
		System.out.println("name:"+name);
		System.out.println("sort: "+sort);
		try {
			write(JSONUtil.serialize(resultList));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getConditionList(){
		
		
	}
	
	/**
	 * 功能：新增重大危险源报告
	 * 
	 */
	public void save(){
		SpCDangerAssess entity = new SpCDangerAssess();
		String filePath = request.getParameter("filePath");
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
					String Temp = uploadFile(annex, fileName, "danger");
					model.setAnnex(Temp);
					
				}
			}
			entity.setFileName(model.getFileName());
			System.out.println("asses sort is :"+model.getAssessSort());
			if("评估标准".equals(model.getAssessSort())){
				model.setAssessSort("0");
			}else{
				model.setAssessSort("1");
			}
			
			entity.setAnnex(model.getAnnex());
			entity.setAssessSort(model.getAssessSort());
			entity.setEntryBy(model.getEntryBy());
			entity.setEntryDate(new Date());
			entity.setIsUse("Y");
			if(employee.getEnterpriseCode()!=null||!"".equals(employee.getEnterpriseCode()))
			entity.setEnterpriseCode(employee.getEnterpriseCode());
			
			
			remote.save(entity);
			write("{success : true,msg :'增加成功！'}");
		}
		
		
	}
	/**
	 * 
	 * 功能：更新重大危险源报告
	 */
	public void updateTarget(){
		String id = request.getParameter("assesId");
		SpCDangerAssess entity=remote.findById(Long.parseLong(id));
		String filePath = request.getParameter("filePath");
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
					
					String Temp = uploadFile(annex, fileName, "danger");
					model.setAnnex(Temp);
					
				}
			}
			entity.setFileName(model.getFileName());
			System.out.println("asses sort is :"+model.getAssessSort());
			if("评估标准".equals(model.getAssessSort())){
				model.setAssessSort("0");
			}else{
				model.setAssessSort("1");
			}
			entity.setAnnex(model.getAnnex());
			entity.setAssessSort(model.getAssessSort());
			entity.setEntryBy(model.getEntryBy());
			entity.setEntryDate(new Date());
			remote.update(entity);
			write("{success : true,msg :'修改成功！'}");
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
	public SpCDangerAssess getModel() {
		return model;
	}
	public void setModel(SpCDangerAssess model) {
		this.model = model;
	}
	public File getAnnex() {
		return annex;
	}
	public void setAnnex(File annex) {
		this.annex = annex;
	}
}

