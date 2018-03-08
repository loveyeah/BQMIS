package power.web.comm; 
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import power.ear.comm.Employee;
import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.system.SysJUrFacadeRemote;

public class ValidateFilter implements Filter {  
	protected String format = null;
	protected FilterConfig filterConfig = null;
	protected boolean validate = true;
    public void init(FilterConfig filterConfig) throws ServletException { 
    	this.filterConfig = filterConfig;
		this.format = filterConfig.getInitParameter("format");
		String value = filterConfig.getInitParameter("validate");
		if (value == null)
			this.validate = true;
		else if (value.equalsIgnoreCase("true"))
			this.validate = true;
		else
			this.validate = false;
    } 
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException { 
    	if(this.validate)
    	{ 
    		HttpServletRequest  httpRequest  = (HttpServletRequest)  request;
    		HttpServletResponse httpResponse = (HttpServletResponse) response; 
    		httpResponse.setContentType("text/html;charset=utf-8");   
    		HttpSession session = httpRequest.getSession();
        	String uri = httpRequest.getRequestURI();   
        	Employee employee =(Employee) session.getAttribute("employee");  
        	if( (uri.indexOf(".jsp")!=-1 || uri.indexOf(".action")!=-1) &&( uri.indexOf("system/checkUserLoginRight.action") == -1) && (uri.indexOf("power/index.jsp") == -1))
        	{ 
			    if(employee ==null)//验证是否登录
			    { 
			    	PrintWriter writer = response.getWriter();
					writer.write("您还没有登录或者会话已过期!<a href='/power' target='_parent'>重新登录!</a>");
					writer.flush();
					writer.close();
					return;
			    }
        	} 
        	if(uri.indexOf(".jsp")!=-1 && uri.indexOf("workflow/test") == -1 && uri.indexOf("workflow/manager/flowshow") == -1  &&(uri.indexOf("power/index.jsp") ==-1))
        	{   
        		int si = uri.indexOf("/", 1); 
    			if(si!=-1)
    			{
    				uri = uri.substring(si+1).trim();    
	    		    {
	    		    	String fileId = httpRequest.getParameter("id"); 
	    		    	SysJUrFacadeRemote remote = (SysJUrFacadeRemote) Ejb3Factory.getInstance().getFacadeRemote("SysJUrFacade");
	    		    	boolean b = remote.validateFileRight(employee.getEmpId(), fileId,uri);
	    		    	if(!b)
	    		    	{
	    		    		PrintWriter writer = response.getWriter();
	    		    		writer.write("您没有权限使用这个模块!,请联系管理员!<br/>");
	    					writer.flush();
	    					writer.close();
	    					return;
	    		    	}
	    		    } 
    			}
        	} 
        	
//        	else if(uri.indexOf(".action") != -1)
//        	{
//        		
//        	}
    	} 
    	chain.doFilter(request, response);
    } 
    /**
     * 
     */
    public void destroy() { 
    	this.filterConfig = null;
    }
}