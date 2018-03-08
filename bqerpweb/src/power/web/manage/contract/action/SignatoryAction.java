package power.web.manage.contract.action;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.ConCSignatory;
import power.ejb.manage.contract.ConCSignatoryFacadeRemote;
import power.ejb.system.SysCUlFacadeRemote;
import power.web.comm.AbstractAction;

public class SignatoryAction extends AbstractAction{
	private static final long serialVersionUID = 1L;
	private ConCSignatory sign;
	private String id;
	private ConCSignatoryFacadeRemote remote;
	private SysCUlFacadeRemote uRemote;
	public SignatoryAction(){
		remote=(ConCSignatoryFacadeRemote)factory.getFacadeRemote("ConCSignatoryFacade");
		uRemote=(SysCUlFacadeRemote)factory.getFacadeRemote("SysCUlFacade");
	}
	public ConCSignatory getSign() {
		return sign;
	}
	public void setSign(ConCSignatory sign) {
		this.sign = sign;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void addSignatory() throws Exception{
		String fileName=request.getParameter("filePath");
		if(fileName!=null && !"".equals(fileName)){
			FileInputStream ins=new FileInputStream(fileName);
			byte[] data = new byte[(int) ins.available()];
			ins.read(data);
			sign.setSignatoryFile(data);
		}
		sign.setEnterpriseCode("hfdc");
		sign.setLastModifiedBy(employee.getWorkerCode());
		sign.setLastModifiedDate(new Date());
		sign.setIsUse("Y");
		remote.save(sign);
	}
	public void updateSignatory() throws Exception{
		String fileName=request.getParameter("filePath");
		if(fileName!=null && !"".equals(fileName)){
			FileInputStream ins=new FileInputStream(fileName);
			byte[] data = new byte[(int) ins.available()];
			ins.read(data);
			sign.setSignatoryFile(data);
		}
		ConCSignatory model=remote.findById(sign.getSignId());
		model.setLastModifiedBy(employee.getWorkerCode());
		model.setLastModifiedDate(new Date());
		model.setSignatoryBy(sign.getSignatoryBy());
		model.setSignatoryFile(sign.getSignatoryFile());
		remote.update(model);
	}
	public void delSignatory(){
		ConCSignatory model=remote.findById(Long.parseLong(id));
		model.setIsUse("N");
		model.setLastModifiedBy(employee.getWorkerCode());
		model.setLastModifiedDate(new Date());
		remote.delete(model);
	}
	public void findSignatoryList(){
		PageObject pg=remote.findSignatoryList("hfdc");
		List<ConCSignatory> list=pg.getList();
		Long count=pg.getTotalCount();
		String str="[";
		String wstr="";
		if(list!=null){
			for(ConCSignatory model : list){
//				String s=model.getSignId().toString()+"#";
//				if(model.getSignatoryFile()!=null){
//					s+="false";
//				}else{
//					s+="true";
//				}
				Object obj=uRemote.findWorkerCodeByName(model.getSignatoryBy());
				String workerCode="";
				if(obj!=null){
					workerCode=obj.toString();
				}else{
					workerCode="";
				}
				str+="{\"signId\":\""+model.getSignId()+"\",\"signatoryBy\":\""+model.getSignatoryBy()
				+"\",\"signatoryFile\":\""+model.getSignatoryFile()+"\",\"lastModifiedBy\":\""+model.getLastModifiedBy()
				+"\",\"lastModifiedDate\":\""+model.getLastModifiedDate().toString()+"\",\"workerCode\":\""+workerCode+"\"},";
			}
			if(str.length()>1){
				str=str.substring(0,str.length()-1);
			}
			str+="]";
			wstr="{total:"+count+",list:"+str+"}";
		}
		else{
			wstr="{total:0,list:[]}";
		}
		write(wstr);
	}
	public void viewSignatoryFile() throws Exception{
		ConCSignatory model=remote.findById(Long.parseLong(id));
		if(model!=null){
			byte[] data = model.getSignatoryFile();
			if(data.length>0 && data!=null){
				response.setContentType("image/jpeg");
				OutputStream outs = response.getOutputStream();
				for (int i = 0; i < data.length; i++) {
					outs.write(data[i]);
				}
				outs.close();
			}
		}
	}
}
