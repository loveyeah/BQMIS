package power.web.productiontec.technologySupervise.action;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.technologySupervise.PtJJdzj;
import power.ejb.productiontec.technologySupervise.PtJJdzjFacadeRemote;
import power.web.comm.UploadFileAbstractAction;

@SuppressWarnings("serial")
public class SummaryAction extends UploadFileAbstractAction{
	private File annex;
	private PtJJdzj model;
	private PtJJdzjFacadeRemote remote;
	public SummaryAction(){
		remote = (PtJJdzjFacadeRemote)factory.getFacadeRemote("PtJJdzjFacade");
	}
	public void findPtJJdzjList() throws JSONException{
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
	public void addPtJJdzjInfo() throws ParseException{	
		String jdzyId = request.getParameter("jdzyId");
		String filePath = request.getParameter("filePath");
		String zjDate = request.getParameter("zjDate");
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
		model.setEnterpriseCode(employee.getEnterpriseCode());
		model.setFillDate(new Date());
		model.setJdzyId(Long.parseLong(jdzyId));
		model.setZjDate(str2date(zjDate));
		remote.save(model);
		write("{success : true,msg :'增加成功！'}");
	}
	public void updatePtJJdzjInfo() throws ParseException{
		PtJJdzj entity=remote.findById(model.getJdzjId());
		String filePath = request.getParameter("filePath");
		String zjDate = request.getParameter("zjDate");
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
			entity.setMainTopic(model.getMainTopic());
			entity.setFillBy(model.getFillBy());
			entity.setContent(model.getContent());
			entity.setMemo(model.getMemo());
			entity.setFillDate(new Date());
			entity.setZjBy(model.getZjBy());
			entity.setZjDate(str2date(zjDate));
			remote.update(entity);
			write("{success : true,msg :'修改成功！'}");
		}
	}
	public void deletePtJJdzjInfo(){
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
	public PtJJdzj getModel() {
		return model;
	}
	public void setModel(PtJJdzj model) {
		this.model = model;
	}
}
