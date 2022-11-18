package br.com.fiap.fintech.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.fiap.fintech.dao.ExpenseDAO;
import br.com.fiap.fintech.dao.RevenueDAO;
import br.com.fiap.fintech.entities.Entry;
import br.com.fiap.fintech.entities.Expense;
import br.com.fiap.fintech.entities.Revenue;
import br.com.fiap.fintech.factory.DAOFactory;
import br.com.fiap.fintech.singleton.Dashboard;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private RevenueDAO revenueDao;
	private ExpenseDAO expenseDao;
	private Dashboard dashboard;
       
	@Override
	public void init() throws ServletException {
		super.init();
		revenueDao = DAOFactory.getRevenueDAO();
		expenseDao = DAOFactory.getExpenseDAO();
		dashboard = dashboard.getInstance();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Revenue> revenueList = revenueDao.getAll();
		List<Expense> expenseList = expenseDao.getAll();
		
		ArrayList<Entry> entries = getAllEntries(revenueList, expenseList);
		
		main(entries);
	
		request.getRequestDispatcher("dashboard.jsp").forward(request, response);
	}
	
	protected void main(ArrayList<Entry> entries) {
		for(Entry entry: entries) {		
			handleStartWithBalance(entry);
		}
	}

	private void handleStartWithBalance(Entry entry) {
		if(isAnEntryBeforeTheBeginningOfTheMonth(entry)) {
			if(entry.isRevenue()) {
				startWithBalance += entry.getEntryValue();
			} else {
				startWithBalance -= entry.getEntryValue();
			}	
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	protected ArrayList<Entry> getAllEntries(List<Revenue> revenueList, List<Expense> expenseList) {
		ArrayList<Entry> entryList = new ArrayList<Entry>();
		entryList.addAll(revenueList);
		entryList.addAll(expenseList);
		return entryList;
	}

	protected double getStarWithBalance(ArrayList<Entry> entries) {
		double startWithBalance = 0;
		for(Entry entry: entries) {
			if(isAnEntryBeforeTheBeginningOfTheMonth(entry)) {
				if(entry.isRevenue()) {
					startWithBalance += entry.getEntryValue();
				} else {
					startWithBalance -= entry.getEntryValue();
				}	
			}
		}
		return startWithBalance;
	}
	
//	protected double getCurrentBalance(List<Expense> expenseList) {
//		double expenseValues = 0;
//		for(Expense expense: expenseList) {
//			expenseValues += expense.getExpenseValue();
//		}
//		return expenseValues;
//	}
//	
//	protected double getExpectedToCloseWithBalance(List<Revenue> revenueList) {
//		double revenueValues = 0;
//		for(Revenue revenue: revenueList) {
//			revenueValues += revenue.getRevenueValue();
//		}
//		return revenueValues;
//	}
//	
//	protected double getMonthlyRevenueValue(List<Revenue> revenueList) {
//		double revenueValues = 0;
//		for(Revenue revenue: revenueList) {
//			revenueValues += revenue.getRevenueValue();
//		}
//		return revenueValues;
//	}
//	
//	protected double getMonthlyExpenseValue(List<Revenue> revenueList) {
//		double revenueValues = 0;
//		for(Revenue revenue: revenueList) {
//			revenueValues += revenue.getRevenueValue();
//		}
//		return revenueValues;
//	}
	
	protected boolean isAnEntryBeforeTheBeginningOfTheMonth(Entry entry) {
		Calendar entryDate = entry.getEntryDate();
		Calendar monthStartDate = (Calendar) entryDate.clone();
		monthStartDate.set(Calendar.DAY_OF_MONTH, 1);
		return entryDate.before(monthStartDate);
	}}
