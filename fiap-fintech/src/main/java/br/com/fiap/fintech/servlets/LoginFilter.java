package br.com.fiap.fintech.servlets;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/login")
public class LoginFilter extends HttpFilter implements Filter {
	private static final long serialVersionUID = 1L;

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		System.out.println("Got into Login Filter!");

		HttpServletRequest request  = (HttpServletRequest) servletRequest;
		HttpServletResponse response  = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession();
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		boolean credentialsAreInformed = (Objects.nonNull(username) && Objects.nonNull(password) 
											&& !username.isEmpty() && !password.isEmpty());
		
		if (!credentialsAreInformed) {
			System.out.println("Credentials not informed");
			//response.sendRedirect("/login");
			//return;
		} 
//		else {
//			session.setAttribute("username", session.getAttribute("username"));
//			session.setAttribute("password", session.getAttribute("password"));
//		}
//		
//		boolean credentialsExistInDatabase = true; // TODO chamar DAO aqui!
//		if (!credentialsExistInDatabase) {
//			response.sendRedirect("/login");
//			return;
//		}	
		
		chain.doFilter(servletRequest, servletResponse);
	}
}
