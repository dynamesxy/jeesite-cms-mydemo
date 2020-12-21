package com.jeesite.modules.configure;

import javax.servlet.http.HttpServletRequest;

import org.beetl.core.Context;
import org.beetl.core.Function;
import org.beetl.ext.web.WebVariable;
import org.springframework.web.servlet.support.RequestContext;

public class I18nFunction implements Function {

	@Override
	public Object call(Object[] obj, Context context) {
		HttpServletRequest request = (HttpServletRequest) context.getGlobal(WebVariable.REQUEST);
		RequestContext requestContext = new RequestContext(request);
		String message = requestContext.getMessage((String) obj[0]);
		return message;
	}

}
