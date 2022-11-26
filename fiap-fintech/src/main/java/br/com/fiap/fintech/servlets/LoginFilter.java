package br.com.fiap.fintech.servlets;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@WebFilter("/login")
public class LoginFilter extends HttpFilter implements Filter {
       
    public LoginFilter() {
        super();
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request  = (HttpServletRequest) servletRequest;
		String username = request.getParameter("username");

		HttpSession session = request.getSession();
		session.setAttribute("username", username);
		
		chain.doFilter(servletRequest, servletResponse);
		return;
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
