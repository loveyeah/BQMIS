/** 
*获取url参数
*@param psName:参数名称
*@return :参数值(不存在则返回空) 
*/ 
function  getParameter(psName){  
      var url = self.location;  
      var result="";    
      var str = self.location.search.substring(0);  
      if(str.indexOf(psName)!=-1&&(str.substr(str.indexOf(psName)-1,1)=="?"||str.substr(str.indexOf(psName)-1,1)=="&")){   
          if(str.substring(str.indexOf(psName),str.length).indexOf("&")!=-1){   
              var   Test=str.substring(str.indexOf(psName),str.length);   
              result=Test.substr(Test.indexOf(psName),Test.indexOf("&")-Test.indexOf(psName));
              result = result.substring(result.indexOf("=")+1,result.length);
          }   
          else{   
            result=str.substring(str.indexOf(psName),str.length);  
            result = result.substring(result.indexOf("=")+1,result.length); 
          }   
      }  
      return result; 
} 