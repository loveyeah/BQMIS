package power.web.productiontec.chemistry.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.chemistry.PtHxjdJNqqxl;
import power.ejb.productiontec.chemistry.PtHxjdJNqqxlDetail;
import power.ejb.productiontec.chemistry.PtHxjdJNqqxlDetailFacadeRemote;
import power.ejb.productiontec.chemistry.PtHxjdJNqqxlFacadeRemote;
import power.ejb.productiontec.chemistry.form.CondenserLeakForm;
import power.web.comm.UploadFileAbstractAction;

@SuppressWarnings("serial")
public class CondenserleakAction extends UploadFileAbstractAction{

	private PtHxjdJNqqxlFacadeRemote remote;
	private PtHxjdJNqqxlDetailFacadeRemote detailRemote;
	private PtHxjdJNqqxl condenser;
	private PtHxjdJNqqxlDetail detail;
	private File conFile;
	
	public CondenserleakAction()
	{
		remote = (PtHxjdJNqqxlFacadeRemote)factory.getFacadeRemote("PtHxjdJNqqxlFacade");
		detailRemote = (PtHxjdJNqqxlDetailFacadeRemote)factory.getFacadeRemote("PtHxjdJNqqxlDetailFacade");
	}
	
	/**
	 * 删除凝汽器泄漏台帐记录
	 */
	public void deleteCondenserLeak()
	{
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}

	/**
	 * 增加，修改凝汽器泄漏台帐记录
	 */
	public void modifyCondenserLeakDetails()
	{
		try {
			String filePath = request.getParameter("filePath");
			String addOrUpdateRecords = request.getParameter("addOrUpdateRecords"); 
			String nqjxlId =request.getParameter("nqjxlId");
		
			if (nqjxlId.equals("") && (!filePath.equals(""))) {
				String result = filePath.substring(filePath.lastIndexOf("\\") + 1);
				String fileName = result.replaceAll(" ", "");
				String[] filetemp = fileName.split("\\.");
				if (filetemp[1].equals("txt")) {
					filetemp[1] = ".doc";
					fileName = filetemp[0] + filetemp[1];
				}
				String Temp = uploadFile(conFile, fileName, "productionrec");
				condenser.setContent(Temp);
			}
			else if(!filePath.equals("")){
				PtHxjdJNqqxl model = remote.findById(Long.parseLong(nqjxlId));
				
				if (model.getContent() != null
						&& filePath.equals(model.getContent().substring(
								model.getContent().lastIndexOf("/") + 1))) {
					condenser.setContent(model.getContent());
				} else {
					String result = filePath.substring(filePath
							.lastIndexOf("\\") + 1);
					String fileName = result.replaceAll(" ", "");
					String[] filetemp = fileName.split("\\.");
					if (filetemp[1].equals("txt")) {
						filetemp[1] = ".doc";
						fileName = filetemp[0] + filetemp[1];
					}
					String Temp = uploadFile(conFile, fileName, "productionrec");
					condenser.setContent(Temp);
				}
			}		
			
			PtHxjdJNqqxl tempPhj = condenser;
			tempPhj.setEnterpriseCode(employee.getEnterpriseCode());
			if (addOrUpdateRecords != null) {
				List<CondenserLeakForm> list = parseContent(addOrUpdateRecords,tempPhj);
				remote.modifyRecords(list, nqjxlId);
				write("{success:true}");
			} 
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success:false}");
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private List<CondenserLeakForm> parseContent(String records,PtHxjdJNqqxl tempPhj) throws JSONException
	{
		List<CondenserLeakForm> result = new ArrayList<CondenserLeakForm>();
		Object object = JSONUtil.deserialize(records);
		List list=(List)object;
		int intLen = list.size();
		for(int i=0;i<intLen;i++){
			Map map=(Map)list.get(i);
			CondenserLeakForm m = this.parseContentModal(map, tempPhj);
			if(tempPhj.getNqjxlId()==null||tempPhj.getNqjxlId().equals(""))
			{
				m.setNqjxlDetailId(null);
			}
			result.add(m);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private CondenserLeakForm parseContentModal(Map map,PtHxjdJNqqxl tempPhj)
	{
		CondenserLeakForm model=new CondenserLeakForm();
		PtHxjdJNqqxl flagPhj = new PtHxjdJNqqxl();
		
		Object nqjxlId = map.get("condenser.nqjxlId");
		if(nqjxlId != null && !(nqjxlId.equals("")))
			flagPhj.setNqjxlId(Long.parseLong(nqjxlId.toString()));
		Object deviceCode = map.get("condenser.deviceCode");
		if(deviceCode != null && !(deviceCode.equals("")))
			flagPhj.setDeviceCode(deviceCode.toString());
		Object memo = map.get("condenser.memo");
		if(memo != null && !(memo.equals("")))
			flagPhj.setMemo(memo.toString());
		Object fillBy = map.get("condenser.fillBy");
		if(fillBy != null && !(fillBy.equals("")))
			flagPhj.setFillBy(fillBy.toString());
		Object waterQuanlity = map.get("condenser.waterQuanlity");
		if(waterQuanlity != null && !(waterQuanlity.equals("")))
			flagPhj.setWaterQuanlity(waterQuanlity.toString());
		Object place = map.get("condenser.place");
		if(place != null && !(place.equals("")))
			flagPhj.setPlace(place.toString());
		
		Object handleStep = map.get("condenser.handleStep");
		if(handleStep != null && !(handleStep.equals("")))
			flagPhj.setHandleStep(handleStep.toString());
		
		Object handleResult = map.get("condenser.handleResult");
		if(handleResult != null && !(handleResult.equals("")))
			flagPhj.setHandleResult(handleResult.toString());
		
		Object enterpriseCode = map.get("condenser.enterpriseCode");
		if(enterpriseCode != null && !(enterpriseCode.equals("")))
			flagPhj.setEnterpriseCode(enterpriseCode.toString());
		
		Object deviceName = map.get("deviceName");
		Object fillName = map.get("fillName");
		Object fillDate = map.get("fillDate");
		Object nqjxlDetailId = map.get("nqjxlDetailId");
		Object projectNames = map.get("projectNames");
		Object itemName1 = map.get("itemName1");
		Object itemName2 = map.get("itemName2");
		Object itemName3 = map.get("itemName3");
		Object itemName4 = map.get("itemName4");
		Object startDate = map.get("startDate");
		Object endDate = map.get("endDate");
		Object enterpriseCodeDetail = map.get("enterpriseCode");
		
		if(flagPhj.getNqjxlId() != null && !(flagPhj.getNqjxlId().toString().equals("")) )
		{
			model.setCondenser(flagPhj);
		}
		else
		{
			model.setCondenser(tempPhj);
		}
		if(deviceName != null)
			model.setDeviceName(deviceName.toString());
		
		if(fillName != null)
			model.setFillName(fillName.toString());
		if(startDate != null && !(startDate.equals("")))
			model.setStartDate(startDate.toString());	
		
		if(endDate != null && !(endDate.equals("")))
			model.setEndDate(endDate.toString());
		if(fillDate != null)
			model.setFillDate(fillDate.toString());	
		if(nqjxlDetailId != null)
			model.setNqjxlDetailId(Long.parseLong(nqjxlDetailId.toString()));
		if(projectNames != null)
			model.setProjectNames(projectNames.toString());
		if(itemName1 != null)
			model.setItemName1(Double.parseDouble(itemName1.toString()));
		if(itemName2 != null)
			model.setItemName2(Double.parseDouble(itemName2.toString()));
		if(itemName3 != null)
			model.setItemName3(Double.parseDouble(itemName3.toString()));
		if(itemName4 != null )
			model.setItemName4(Double.parseDouble(itemName4.toString()));
		
		if(enterpriseCodeDetail != null && !(enterpriseCodeDetail.equals("")))
			model.setEnterpriseCode(enterpriseCodeDetail.toString());
		
		return model;
	}

	
	/**
	 * 显示凝汽器泄漏台帐列表
	 * @throws JSONException
	 */
	public void findCondenserLeakList() throws JSONException
	{
		String deviceCode = request.getParameter("deviceCode");
		String sDate = request.getParameter("sDate");
		String eDate =  request.getParameter("eDate");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) 
		{
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.findCondenserLeakList(deviceCode, sDate, eDate, employee.getEnterpriseCode(), start,limit);
		}
		else {
			obj = remote.findCondenserLeakList(deviceCode, sDate, eDate, employee.getEnterpriseCode());
		}
		String strOutput = "";
		if (obj == null || obj.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(obj);
		}
		write(strOutput);
	}
	
	/**
	 * 显示凝汽器泄漏明细列表
	 * @throws JSONException
	 */
	public void findcondenserDetailsList() throws JSONException
	{
		String nqjxlId = request.getParameter("nqjxlId");
		if(nqjxlId == null)
			nqjxlId = "";
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) 
		{
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = detailRemote.findAll(nqjxlId,employee.getEnterpriseCode(), start,limit);
		}
		else {
			if(!nqjxlId.equals(""))
			{
			 obj = detailRemote.findAll(nqjxlId,employee.getEnterpriseCode());
			}
			else
			{
				obj.setTotalCount(3l);
				@SuppressWarnings("unused")
				List<CondenserLeakForm> list=new ArrayList<CondenserLeakForm>();
				
				/*CondenserLeakForm form1 = new CondenserLeakForm();
				form1.setProjectNames("循环水");
				form1.setNqjxlDetailId(0l);
				form1.setNqjxlId(0l);
				form1.setItemName1(0d);
				form1.setItemName2(0d);
				form1.setItemName3(0d);
				form1.setItemName4(0d);
				form1.setEnterpriseCode(employee.getEnterpriseCode());
				*/
				CondenserLeakForm form2 = new CondenserLeakForm();
				form2.setProjectNames("凝结水泄漏前");
				form2.setNqjxlId(0l);
				form2.setNqjxlDetailId(0l);
				form2.setItemName1(0d);
				form2.setItemName2(0d);
				form2.setItemName3(0d);
				form2.setItemName4(0d);
				form2.setEnterpriseCode(employee.getEnterpriseCode());
				
				CondenserLeakForm form3 = new CondenserLeakForm();
				form3.setProjectNames("凝结水捉漏后");
				form3.setNqjxlId(0l);
				form3.setNqjxlDetailId(0l);
				form3.setItemName1(0d);
				form3.setItemName2(0d);
				form3.setItemName3(0d);
				form3.setItemName4(0d);
				form3.setEnterpriseCode(employee.getEnterpriseCode());
				
				//list.add(form1);
				list.add(form2);
				list.add(form3);
				obj.setList(list);
			}
		}
		String strOutput = "";
		if (obj == null || obj.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(obj);
		}
		write(strOutput);
	}
	
	public PtHxjdJNqqxl getCondenser() {
		return condenser;
	}

	public void setCondenser(PtHxjdJNqqxl condenser) {
		this.condenser = condenser;
	}

	public File getConFile() {
		return conFile;
	}

	public void setConFile(File conFile) {
		this.conFile = conFile;
	}

	public PtHxjdJNqqxlDetail getDetail() {
		return detail;
	}

	public void setDetail(PtHxjdJNqqxlDetail detail) {
		this.detail = detail;
	}
}
