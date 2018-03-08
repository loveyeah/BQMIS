/**
 * 取得两个时间段的间隔的天数
 * 如getBetweenDays("2008-8-1 8:50:01","2008-8-2 18:50:01"); 
 * @param {} start 
 * @param {} end
 */ 
function getBetweenDays(start,end)
{   
	var time1 =parseDate(start);  
	var tiem2 = parseDate(end);      
	var days = (tiem2.getTime()-time1.getTime()) / 86400000; 
	return days; 
}  
 
/**
 * 判断输入框中输入的日期格式是否为年月日时分秒 即 yyyy-mm-dd hh:mi:ss
 * @param {} dateString 
 * @return {Boolean} 
 */
function validatorDataFormat(dateString) {  
	if (dateString.replace(/(^\s*)|(\s*$)/g, "") == "")
	{  
		return false;
	}
	// 年月日时分秒正则表达式  
//	var re=new RegExp(/^(\d{1,4})\-(\d{1,2})\-(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/);
//    var r=re.exec(dateString); 
	var r = dateString.match(/^(\d{1,4})\-(\d{1,2})\-(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/); 
	if (r == null) {   
		return false;
	}
	var d = new Date(r[1], r[2] - 1, r[3], r[4], r[5], r[6]);
	var num = (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[2]
			&& d.getDate() == r[3] && d.getHours() == r[4]
			&& d.getMinutes() == r[5] && d.getSeconds() == r[6]);  
	return num;
}  
/**
 * 将格式为 yyyy-mm-dd hh:mi:ss的字符串转换为Date对象
 * @param {} dateString
 * @return {}
 */
function parseDate(dateString) { 

	if(validatorDataFormat(dateString))
	{  
		var r = dateString.match(/^(\d{1,4})\-(\d{1,2})\-(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/); 
		var d = new Date(r[1], r[2] - 1, r[3], r[4], r[5], r[6]); 
		return d;
	}
	else{
		alert("请输入格式正确的日期\n\r日期格式：yyyy-mm-dd hh:mi:ss");
	} 
}  