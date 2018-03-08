package power.web.productiontec.technologySupervise.action;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.technologySupervise.PtJSjfx;
import power.ejb.productiontec.technologySupervise.PtJSjfxFacadeRemote;
import power.web.comm.UploadFileAbstractAction;

@SuppressWarnings("serial")
public class EventAnalysisAction extends UploadFileAbstractAction{
	private File annex;
	private PtJSjfx model;
	private PtJSjfxFacadeRemote remote;
	public EventAnalysisAction(){
		remote = (PtJSjfxFacadeRemote)factory.getFacadeRemote("PtJSjfxFacade");
	}
	public void findPtJSjfxList() throws JSONException{
		String topicName = request.getParameter("topicName");
		String jdzyId = request.getParameter("jdzyId");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.findAll(jdzyId,topicName, employee.getEnterpriseCode(), start,limit);
		} else {
			
			object = remote.findAll(jdzyId,topicName, employee.getEnterpriseCode());
		}
		
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		System.out.println(strOutput);
		write(strOutput);
	}
	public void addPtJSjfxInfo() throws ParseException{	
		String filePath = request.getParameter("filePath");
		String fxDate = request.getParameter("fxDate");
		String jdzyId = request.getParameter("jdzyId");
		if (!filePath.equals("")) {
			String result = filePath
					.substring(filePath.lastIndexOf("\\") + 1);
			String fileName = result.replaceAll(" ", "");
			String[] filetemp = fileName.split("\\.");
			if (filetemp[1].equals("txt")) {
				filetemp[1] = ".doc";
				fileName = filetemp[0] + filetemp[1];
			}
			String Temp = uploadFile(annex, fileName, "productionrec");
			model.setContent(Temp);
		}
		model.setJdzyId(Long.parseLong(jdzyId));
		model.setEnterpriseCode(employee.getEnterpriseCode());
		model.setFillDate(new Date());
		model.setFxDate(str2date(fxDate));
		remote.save(model);
		write("{success : true,msg :'增加成功！'}");
	}
	public void updatePtJSjfxInfo() throws ParseException{
		PtJSjfx entity=remote.findById(model.getSjfxId());
		String filePath = request.getParameter("filePath");
		String fxDate = request.getParameter("fxDate");
		if (entity != null) {
			if (!filePath.equals("")) {
				if (entity.getContent() != null
						&& filePath.equals(entity.getContent().substring(
								entity.getContent().lastIndexOf("/") + 1))) {
					model.setContent(entity.getContent());
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
					model.setContent(Temp);
					
				}
			}
			entity.setFxBy(model.getFxBy());
			entity.setFillBy(model.getFillBy());
			entity.setMainTopic(model.getMainTopic());
			entity.setContent(model.getContent());
			entity.setMemo(model.getMemo());
			entity.setFillDate(new Date());
			entity.setFxDate(str2date(fxDate));
			remote.update(entity);
			write("{success : true,msg :'修改成功！'}");
		}
	}
	public void deletePtJSjfxInfo(){
		String ids=request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success : true,msg :'删除成功！'}");
	}
	private Date str2date(String s) throws java.text.ParseException {
			Date datea = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(s);
			datea = date;
			return datea;
	}
	public File getAnnex() {
		return annex;
	}
	public void setAnnex(File annex) {
		this.annex = annex;
	}
	public PtJSjfx getModel() {
		return model;
	}
	public void setModel(PtJSjfx model) {
		this.model = model;
	}
}
