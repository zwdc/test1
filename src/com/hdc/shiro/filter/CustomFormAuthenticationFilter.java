package com.hdc.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hdc.entity.User;
import com.hdc.service.IUserService;
import com.hdc.util.BeanUtilsExt;
import com.hdc.util.Constants;
import com.hdc.util.UserUtil;

/**
 * 验证验证码的拦截器
 * @author zml
 *
 */

public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {

	@Autowired
	private IUserService userService;
	
	//在访问被拒绝
	/**
	 * onAccessDenied 表示访问拒绝时是否自己处理
	 * return true:   访问拒绝也不自己处理，继续拦截器链的执行
	 * return false:  表示自己已经处理了
	 */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        if(request.getAttribute(getFailureKeyAttribute()) != null) {
        	//当验证码验证失败时不再走身份认证拦截器
            return true;
        }
        
        HttpServletRequest httpServletRequest = (HttpServletRequest) request; 
        //HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                return executeLogin(request, response);
            } else {
            	if ("XMLHttpRequest".equals(httpServletRequest.getHeader("X-Requested-With"))){  
            		HttpServletResponse res = WebUtils.toHttp(response);
            		res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            	}else{
            		return true;
            	}
            	return false;
            }
        } else {
        	if ("XMLHttpRequest".equals(httpServletRequest.getHeader("X-Requested-With"))){  
        		HttpServletResponse res = WebUtils.toHttp(response);
        		res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        	}else{
        		return true;
        	}
            return false;
        }
        
        //return super.onAccessDenied(request, response, mappedValue);
    }
    
    //可以根据不同角色设置跳转不同页面
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token,
            Subject subject,
            ServletRequest request,
            ServletResponse response)
                 throws Exception {
    	boolean contextRelative = true;
    	Session session = subject.getSession();
    	if(session != null){
    		//会话过期 attribute 为空，重新设置。
    		User user = (User) session.getAttribute(Constants.CURRENT_USER);
    		if(BeanUtilsExt.isBlank(user)){
    			user = this.userService.getUserByName(subject.getPrincipal().toString());
    			UserUtil.saveUserToSession(session, user);
    		}
    	}
    	String successUrl = this.getSuccessUrl();
    	//登录前的url
    	/*SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(request);
    	if (savedRequest != null && savedRequest.getMethod().equalsIgnoreCase(AccessControlFilter.GET_METHOD)) {
            successUrl = savedRequest.getRequestUrl();
            contextRelative = false;
        }*/
        WebUtils.issueRedirect(request, response, successUrl, null, contextRelative);
		return false;
    }
    
}
