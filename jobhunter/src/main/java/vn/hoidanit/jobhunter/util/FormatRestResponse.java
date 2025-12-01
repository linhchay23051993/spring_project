package vn.hoidanit.jobhunter.util;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import vn.hoidanit.jobhunter.domain.response.RestResponse;
import vn.hoidanit.jobhunter.util.anotation.ApiMessage;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		RestResponse<Object> res = new RestResponse<Object>();
		if (response instanceof ServletServerHttpResponse servletResponse) {
			int status = servletResponse.getServletResponse().getStatus();
			res.setStatusCode(status);
			if (body instanceof String) {
				return body;
			}

			if (status >= 400) {
				return body;
			} else {
				res.setData(body);
				ApiMessage message = returnType.getMethodAnnotation(ApiMessage.class);
				res.setMessage(message != null ? message.value() : "Call API SUCCESS");
			}

		}
		return res;
	}

}
