package power.web.message.action;

import java.io.IOException;
import java.util.List;
import javax.naming.NamingException;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.CodeRepeatException;
import power.ejb.message.JljfCObject;
import power.ejb.message.JljfCObjectFacade;
import power.ejb.message.JljfCObjectFacadeRemote;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import power.web.comm.ListRange;

public class  CustomerCompanyAction extends AbstractAction {

	/**
	 * 取得公司信息
	 * 
	 */
	private JljfCObject jljfcobject;
	private String method;
	//客户公司一览
	public void getCustomerCompanyList() throws IOException, NamingException,
			JSONException {
		JljfCObjectFacadeRemote bll = (JljfCObjectFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("JljfCObjectFacade");
		List<JljfCObject> cuscompany = bll.findAll();
		String jsonstr = JSONUtil.serialize(cuscompany);
		write("{root:"+jsonstr+"}");
	}
	
	public void addCusCompany() throws Exception {
		try {
			if ("insert".equals(method)) {
				JljfCObjectFacadeRemote dll = (JljfCObjectFacadeRemote) Ejb3Factory
						.getInstance().getFacadeRemote("JljfCObjectFacade");
				if(jljfcobject != null){
					dll.save(jljfcobject);
				}
			}
		} catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
	}

	public void updateCusCompany() throws Exception {
	try{
		if ("update".equals(method)) {
				JljfCObjectFacadeRemote bll = (JljfCObjectFacadeRemote) Ejb3Factory
					.getInstance().getFacadeRemote("JljfCObjectFacade");
				JljfCObject model=bll.findByZbbmtxCode(jljfcobject.getZbbmtxCode());
				if(jljfcobject != null){
					model.setZbbmtxName(jljfcobject.getZbbmtxName());
					model.setZbbmtxAlias(jljfcobject.getZbbmtxAlias());
					bll.update(model);
				}
			}
		} catch(CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}	
	}
  
	public void deleteCusCompany() throws Exception {
			JljfCObjectFacadeRemote bll = (JljfCObjectFacadeRemote) Ejb3Factory
					.getInstance().getFacadeRemote("JljfCObjectFacade");
			String code=request.getParameter("zbbmtxCode");
			String[] ids=code.split(",");
			for(int i=0;i<ids.length;i++){
				JljfCObject model = bll.findByZbbmtxCode(ids[i]);
				if(model!=null){
					bll.delete(model);
				}	
			}
							
		 }

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public JljfCObject getJljfcobject() {
		return jljfcobject;
	}

	public void setJljfcobject(JljfCObject jljfcobject) {
		this.jljfcobject = jljfcobject;
	}

}