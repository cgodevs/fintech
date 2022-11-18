package br.com.fiap.fintech.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.fiap.fintech.dao.ExpenseDAO;
import br.com.fiap.fintech.dao.RevenueDAO;
import br.com.fiap.fintech.entities.Expense;
import br.com.fiap.fintech.entities.Revenue;
import br.com.fiap.fintech.factory.DAOFactory;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private RevenueDAO revenueDao;
	private ExpenseDAO expenseDao;
       
	@Override
	public void init() throws ServletException {
		super.init();
		revenueDao = DAOFactory.getRevenueDAO();
		expenseDao = DAOFactory.getExpenseDAO();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Revenue> revenueList = revenueDao.getAll();
		List<Expense> expenseList = expenseDao.getAll();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	protected double getValueOfAllRevenues(List<Revenue> revenueList) {
		double revenueValues = 0;
		for(Revenue revenue: revenueList) {
			revenueValues += revenue.getRevenueValue();
		}
		return revenueValues;
	}
	
	protected double getValueOfAllExpenses(List<Expense> expenseList) {
		double expenseValues = 0;
		for(Expense expense: expenseList) {
			expenseValues += expense.getExpenseValue();
		}
		return expenseValues;
	}
	
	protected double getValueOfAllRevenues(List<Revenue> revenueList) {
		double revenueValues = 0;
		for(Revenue revenue: revenueList) {
			revenueValues += revenue.getRevenueValue();
		}
		return revenueValues;
	}
}
