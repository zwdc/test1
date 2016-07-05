package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hdc.util.TempltUtil;

public class ExcWordTest {
	
	   public void excWord(HttpServletResponse response,HttpServletRequest request) throws IOException{  
	  
	          
	    try {   
	        Map dataMap = new HashMap();   
	        if (getData(response,request,dataMap)) {   
	        File previewFile = new File(request.getSession().getServletContext().getRealPath(TempltUtil.PREVIEW_DOC));   
	        InputStream is = new FileInputStream(previewFile);   
	        response.reset();   
	        response.setContentType("application/vnd.ms-word;charset=UTF-8");   
	        response.addHeader("Content-Disposition","attachment; filename=\"" + TempltUtil.PREVIEW_DOC + "\"");   
	        byte[] b = new byte[1024];   
	        int len;   
	        while ((len=is.read(b)) >0) {   
	        response.getOutputStream().write(b,0,len);   
	        }   
	        is.close();   
	        response.getOutputStream().flush();   
	        response.getOutputStream().close();   
	        }   
	        } catch (Exception e) {   
	        e.printStackTrace();   
	        }   
	   }  
	
	private boolean getData(HttpServletResponse response,HttpServletRequest request,Map dataMap) {    
	    
	    dataMap.put("title_name", "用户信息");    
	  
	    dataMap.put("user_name", "张三");    
	  
	    dataMap.put("org_name", "xx公司");    
	  
	    dataMap.put("dept_name", "事业部");  
	    TempltUtil.toPreview(request, TempltUtil.WORD_TEMPLATE, dataMap);   
	    return true;  
	  
	 }    
}
