Ext.useShims = true;
Ext.onReady(function() {
			var errMsg = getParameter("errMsg");
			if (errMsg != "") {
				if (errMsg == "-1") {
					errMsg = "工号或密码错误!请重新输入!";
				} else if (errMsg == "-2") {
					errMsg = "服务器异常,请联系管理员!";
				}
				Ext.get("lblErrorMsg").dom.innerText = errMsg;
				document.getElementById('un').focus();
			}
		});

function checkFormData() {
	if (Ext.get("user.workerCode").dom.value == "") {
		alert("工号不能为空!");
		return false;
	}
	if (Ext.get("user.loginPwd").dom.value == "") {
		alert("密码不能为空!");
		return false;
	}
	loginForm.submit();
}
