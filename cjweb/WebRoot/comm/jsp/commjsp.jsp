<%@page import="power.ear.comm.Employee"%> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Employee employee =(Employee) session.getAttribute("employee");
	String style = (employee==null?"xtheme-gray":(employee.getStyle()==null||"".equals(employee.getStyle())?"xtheme-gray":employee.getStyle()));
%>
<base href="<%=basePath%>">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<link rel="stylesheet" type="text/css" href="comm/ext/resources/css/ext-all.css" /> 
<link rel="stylesheet" type="text/css" href="comm/ext/button.css" />
<link href="comm/ext/resources/css/<%=style%>.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="comm/ext/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="comm/ext/ext-all-debug.js"></script>
<script type="text/javascript" src="comm/ext/ext-lang-zh_CN.js"></script>
<script language="javascript">
   var ocss_grid_input_col = {};
   var uploadUrl='http://localhost:8080/power/upload_dir/';
   ocss_grid_input_col["xtheme-gray"] = "background:#F5DEB3;";
   ocss_grid_input_col["xtheme-black"] = "background:#F5DEB3;";
   ocss_grid_input_col["xtheme-darkgray"] = "background:#F5DEB3;";
   ocss_grid_input_col["xtheme-galdaka"] = "background:#B0E0E6;";
   ocss_grid_input_col["xtheme-purple"] = "background:#FFB6C1;";
   ocss_grid_input_col["xtheme-slate"] = "background:#87CEFA;";

   CSS_GRID_INPUT_COL = ocss_grid_input_col["<%=style%>"];
   
   /*common: trim for TextField,TextArea by jyuan */
   var commjsp$Ext_form_TextField_prototype_beforeBlur = Ext.form.TextField.prototype.beforeBlur;
   Ext.form.TextField.prototype.beforeBlur = function() {
       commjsp$Ext_form_TextField_prototype_beforeBlur.call(this, arguments);       
       v = this.getRawValue();
       if(v) {
           v = String(v).replace(/^\s*\t*\r?\n?|\r?\n?\s*\t*$/g, '');
           this.setValue(v);
       }
   }
   Ext.grid.GridPanel.prototype.enableHdMenu = false;
   Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';  
</script>