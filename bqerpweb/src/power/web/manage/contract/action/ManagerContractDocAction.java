package power.web.manage.contract.action;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.manage.contract.business.ConJConDoc;
import power.ejb.manage.contract.business.ConJConDocFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class ManagerContractDocAction extends AbstractAction {
	private File docFile;
	ConJConDocFacadeRemote docremote = 
		     (ConJConDocFacadeRemote) factory.getFacadeRemote("ConJConDocFacade");
	public void saveContractFile() throws IOException{
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		if(docFile != null)
		{
			InputStream inputStream = null;
			ByteArrayOutputStream outStream = null;
			try {
				inputStream = new BufferedInputStream(new FileInputStream(
						docFile), 16 * 1024);
				outStream = new ByteArrayOutputStream(16 * 1024);
				byte[] buffer = new byte[16 * 1024];
				int n;
				while ((n = inputStream.read(buffer)) != -1)
					outStream.write(buffer, 0, n);
				byte[] data = outStream.toByteArray();
				if (data != null && data.length > 0) {
					Ejb3Factory factory = Ejb3Factory.getInstance();
					ConJConDoc entity = null;
					if ("CON".equals(type)) {
						entity = docremote.findConDocModel(employee
								.getEnterpriseCode(), Long.parseLong(id), type);
					} else {
						entity = docremote.findById(Long.parseLong(id));
					}
					entity.setDocContent(data);
					docremote.update(entity);
				}
				write("success");
			} catch (Exception exc) {
				exc.printStackTrace();
				write("failed");
			} finally {
				if (inputStream != null)
					inputStream.close();
				if (outStream != null)
					outStream.close();
			}
		}
	} 
	public void setDocFile(File docFile) {
		this.docFile = docFile;
	}
}
