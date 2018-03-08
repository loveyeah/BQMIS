package power.web.comm;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import power.ear.comm.Employee;
import power.ear.comm.ejb.Ejb3Factory;

import com.opensymphony.xwork2.ActionSupport;

public class AbstractAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = -5255515889980270607L;
	private static Log log = LogFactory.getLog(AbstractAction.class);
	public static Ejb3Factory factory;
	public HttpServletRequest request;
	public HttpServletResponse response;
	public HttpSession session;
	public Employee employee;
	static {
		factory = Ejb3Factory.getInstance();
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		this.session = request.getSession();
		this.employee = (Employee) session.getAttribute("employee");
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * 以html方式输出字符串
	 * 
	 * @param outStr
	 *            输出字符串
	 */
	public void write(String outStr) {
		try {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.write(outStr);
			writer.flush();
			writer.close();
		} catch (IOException exc) {
			exc.printStackTrace();
			log.debug("AbstractAction的write()方法错误!");
		}
	}

	public void writeXml(String outStr) {
		try {
			response.setContentType("text/xml;charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.write(outStr);
			writer.flush();
			writer.close();
		} catch (IOException exc) {
			exc.printStackTrace();
			log.debug("AbstractAction的writeXml()方法错误!");
		}
	}

	public void writeDoc(byte[] data) {
		try {
			response.setContentType("text/doc;charset=utf-8");
			PrintWriter writer = response.getWriter();
			for (int i = 0; i < data.length; i++) {
				writer.write(data[i]);// 输出到页面
			}
			writer.flush();
			writer.close();
		} catch (IOException exc) {
			exc.printStackTrace();
			log.debug("AbstractAction的writeXml()方法错误!");
		}
	}
}
