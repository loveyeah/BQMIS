package power.web.productiontec.relayProtection;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.chemistry.PtHxjdJZxybybzb;
import power.ejb.productiontec.chemistry.form.ChemistryReportForm;
import power.ejb.productiontec.relayProtection.PtJdbhJSybg;
import power.ejb.productiontec.relayProtection.PtJdbhJSybgFacadeRemote;
import power.ejb.productiontec.relayProtection.PtJdbhJSybgjl;
import power.ejb.productiontec.relayProtection.PtJdbhJSybgjlFacadeRemote;
import power.web.comm.UploadFileAbstractAction;

/**
 * 继电试验报告
 * @author 
 *
 */
@SuppressWarnings("serial")
public class ExperimentReportAction extends UploadFileAbstractAction{
   private PtJdbhJSybgFacadeRemote reportRemote;
   private PtJdbhJSybgjlFacadeRemote remote;
   private PtJdbhJSybg report;
   private File annex;
   public ExperimentReportAction()
   {
	   reportRemote=(PtJdbhJSybgFacadeRemote)factory.getFacadeRemote("PtJdbhJSybgFacade");
	   remote=(PtJdbhJSybgjlFacadeRemote)factory.getFacadeRemote("PtJdbhJSybgjlFacade");
   }
	public PtJdbhJSybg getReport() {
		return report;
	}
	public void setReport(PtJdbhJSybg report) {
		this.report = report;
	}
	public File getAnnex() {
		return annex;
	}
	public void setAnnex(File annex) {
		this.annex = annex;
	}
   
	/**
	 * 查询继电试验报告列表
	 * @throws JSONException
	 */
    public void findExperimentReportList() throws JSONException
    {
    	 String deviceName=request.getParameter("deviceName");
    	 String sDate=request.getParameter("sDate");
    	 String eDate=request.getParameter("eDate");
 		Object objstart = request.getParameter("start");
 		Object objlimit = request.getParameter("limit");
 		PageObject object = null;
 		if (objstart != null && objlimit != null) {
 			int start = Integer.parseInt(request.getParameter("start"));
 			int limit = Integer.parseInt(request.getParameter("limit"));
 			object = reportRemote.findAll(sDate, eDate, deviceName,employee.getEnterpriseCode(), start,limit);
 		} else {
 			
 			object = reportRemote.findAll(sDate, eDate, deviceName,employee.getEnterpriseCode());
 		}
 		
 		String strOutput = "";
 		if (object == null || object.getList() == null) {
 			strOutput = "{\"list\":[],\"totalCount\":0}";
 		} else {
 			strOutput = JSONUtil.serialize(object);
 		}
 		write(strOutput);
    }
    
    /**
     * 新增一条继电试验报告记录
     */
    public void addExperimentReportInfo()
    {
    	String filePath = request.getParameter("filePath");
		if (filePath != null && (!filePath.equals(""))) {
			String result = filePath
					.substring(filePath.lastIndexOf("\\") + 1);
			String fileName = result.replaceAll(" ", "");
			String[] filetemp = fileName.split("\\.");
			if (filetemp[1].equals("txt")) {
				filetemp[1] = ".doc";
				fileName = filetemp[0] + filetemp[1];
			}
			String Temp = uploadFile(annex, fileName, "productionrec");
			report.setContent(Temp);
		}
		report.setEnterpriseCode(employee.getEnterpriseCode());
		report = reportRemote.save(report);
		if(report == null)
		{
			write("{failure:true,msg:'该试验报告名称名称已存在'}");
		}
		else 
		{
			write("{success : true,msg :'数据增加成功！'}");
		}
    }
    
    /**
     * 更新一条继电试验报告记录
     */
    public void updateExperimentReportInfo()
    {
    	PtJdbhJSybg entity=reportRemote.findById(report.getJdsybgId());
    	String filePath = request.getParameter("filePath");
		if (entity != null) {
			if (!filePath.equals("")) {
				if (entity.getContent() != null
						&& filePath.equals(entity.getContent().substring(
								entity.getContent().lastIndexOf("/") + 1))) {
					report.setContent(entity.getContent());
				} else {
					String result = filePath.substring(filePath
							.lastIndexOf("\\") + 1);
					String fileName = result.replaceAll(" ", "");
					String[] filetemp = fileName.split("\\.");
					if (filetemp[1].equals("txt")) {
						filetemp[1] = ".doc";
						fileName = filetemp[0] + filetemp[1];
					}
					String Temp = uploadFile(annex, fileName, "productionrec");
					report.setContent(Temp);
					
				}
			}
			entity.setContent(report.getContent());
			entity.setChargeBy(report.getChargeBy());
			entity.setDeviceId(report.getDeviceId());
			entity.setHumidity(report.getHumidity());
			entity.setJdsybgName(report.getJdsybgName());
			entity.setLastTestDate(report.getLastTestDate());
			entity.setMemo(report.getMemo());
			entity.setPlanTestDate(report.getPlanTestDate());
			entity.setSylbId(report.getSylbId());
			entity.setTemperature(report.getTemperature());
			entity.setTestBy(report.getTestBy());
			entity.setTestDate(report.getTestDate());
			entity.setTestPlace(report.getTestPlace());
			entity.setTestSituation(report.getTestSituation());
			entity.setTestType(report.getTestType());
			entity.setWeather(report.getWeather());
			entity.setEnterpriseCode(employee.getEnterpriseCode());
			report = reportRemote.update(entity);
			if(report == null)
			{
				write("{failure:true,msg:'该试验报告名称名称已存在'}");
			}
			else 
			{
				write("{success : true,msg :'修改数据成功！'}");
			}
		}
    	
    }
    
    /**
     * 删除一条或多条继电试验报告记录
     */
    public void deleteExperimentReport()
    {
    	String ids=request.getParameter("ids");
		reportRemote.deleteMulti(ids);
		write("{success : true,msg :'删除数据成功！'}");
    }
    
    public void getLastTestDate()
    {
    	String deviceId=request.getParameter("deviceId");
        String	lastTestDate= reportRemote.getLastTestDate(Long.parseLong(deviceId), employee.getEnterpriseCode()); 
        write("{success : true,lastTestDate :'"+lastTestDate+"'}");
    }
    
    /**
	 * 查询继电试验报告记录列表
	 * @throws JSONException
	 */
    public void findExperiReportDetailsList() throws JSONException
    {
    	 String jdsybgId=request.getParameter("jdsybgId");
 		Object objstart = request.getParameter("start");
 		Object objlimit = request.getParameter("limit");
 		PageObject object = null;
 		if (objstart != null && objlimit != null) {
 			int start = Integer.parseInt(request.getParameter("start"));
 			int limit = Integer.parseInt(request.getParameter("limit"));
 			object = remote.findAllReprotDetails(jdsybgId, employee.getEnterpriseCode(), start,limit);
 		} else {
 			
 			object = remote.findAllReprotDetails(jdsybgId, employee.getEnterpriseCode());
 		}
 		
 		String strOutput  = JSONUtil.serialize(object);
 		write(strOutput);
    }
    
    
    /**
	 * 批量修改继电试验报告记录数据
	 */
	public void modifyExperiReportDetails()
	{
		try {
			String addOrUpdateRecords = request.getParameter("addOrUpdateRecords"); 
			String jdsybgId = request.getParameter("jdsybgId");
			if (addOrUpdateRecords != null) {
				List<PtJdbhJSybgjl> list = parseContent(addOrUpdateRecords);
				remote.modifyRecords(list);
				write("{success:true}");
			} 
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success:false}");
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private List<PtJdbhJSybgjl> parseContent(String records) throws JSONException
	{
		List<PtJdbhJSybgjl> result = new ArrayList<PtJdbhJSybgjl>();
		Object object = JSONUtil.deserialize(records);
		List list=(List)object;
		int intLen = list.size();
		for(int i=0;i<intLen;i++){
			Map map=(Map)list.get(i);
			PtJdbhJSybgjl m = this.parseContentModal(map);
			result.add(m);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private PtJdbhJSybgjl parseContentModal(Map map)
	{
		PtJdbhJSybgjl model=new PtJdbhJSybgjl();
		
		Object jdsybgjgId = map.get("pjjs.jdsybgjgId");
		if(jdsybgjgId != null && !(jdsybgjgId.equals("")))
			model.setJdsybgjgId(Long.parseLong(jdsybgjgId.toString()));
		Object jdsybgId = map.get("pjjs.jdsybgId");
		if(jdsybgId != null && !(jdsybgId.equals("")))
			model.setJdsybgId(Long.parseLong(jdsybgId.toString()));
		
		
		Object sydId = map.get("pjjs.sydId");
		if(sydId != null && !(sydId.equals("")))
			model.setSydId(Long.parseLong(sydId.toString()));
		Object regulatorId = map.get("pjjs.regulatorId");
		if(regulatorId != null && !(regulatorId.equals("")))
			model.setRegulatorId(Long.parseLong(regulatorId.toString()));
		Object result = map.get("pjjs.result");
		if(result != null && !(result.equals("")))
			model.setResult(Double.parseDouble(result.toString()));
		model.setEnterpriseCode(employee.getEnterpriseCode());
		return model;
	}
}
