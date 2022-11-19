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
		dashboard = Dashboard.getInstance();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Revenue> revenueList = revenueDao.getAll();
		List<Expense> expenseList = expenseDao.getAll();
		
		ArrayList<Entry> entries = getAllEntries(revenueList, expenseList);
		
		main(request, entries);
	
		request.getRequestDispatcher("dashboard.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
	
	protected void main(HttpServletRequest request, ArrayList<Entry> entries) {
		for(Entry entry: entries) {		
			handleStartWithBalance(entry);
			handleCurrentBalance(entry);
			handleExpectedToCloseWithBalance(entry);
			handleMonthlyEntryValues(entry);
		}
		
		request.setAttribute("dashboardData", dashboard);
	}
	
	protected ArrayList<Entry> getAllEntries(List<Revenue> revenueList, List<Expense> expenseList) {
		ArrayList<Entry> entryList = new ArrayList<Entry>();
		entryList.addAll(revenueList);
		entryList.addAll(expenseList);
		return entryList;
	}

	private void handleStartWithBalance(Entry entry) {
		if(isAnEntryBeforeTheBeginningOfTheMonth(entry)) {
			if(entry.isRevenue()) {
				dashboard.setStartWithBalance(dashboard.getStartWithBalance() + entry.getEntryValue());
			} else {
				dashboard.setStartWithBalance(dashboard.getStartWithBalance() - entry.getEntryValue());
			}	
		}
	}
	
	protected boolean isAnEntryBeforeTheEndOfTheMonth(Entry entry) {
		Calendar entryDate = entry.getEntryDate();
		Calendar monthStartDate = Calendar.getInstance();
		return entryDate.before(monthStartDate);
	}
	
	private void handleCurrentBalance(Entry entry) {
		if(isAnEntryBeforeTheCurrentDate(entry)) {
			if(entry.isRevenue()) {
				dashboard.setCurrentBalance(dashboard.getCurrentBalance() + entry.getEntryValue());
			} else {
				dashboard.setCurrentBalance(dashboard.getCurrentBalance() - entry.getEntryValue());
			}	
		}
	}
	
	protected boolean isAnEntryBeforeTheCurrentDate(Entry entry) {
		Calendar entryDate = entry.getEntryDate();
		Calendar currentDate = Calendar.getInstance();
		return entryDate.before(currentDate);
	}
	
	private void handleExpectedToCloseWithBalance(Entry entry) {
		if(isAnEntryBeforeTheEndOfTheMonth(entry)) {
			if(entry.isRevenue()) {
				dashboard.setExpectedToCloseWithBalance(
					dashboard.getExpectedToCloseWithBalance() - entry.getEntryValue()
				);
			} else {
				dashboard.setExpectedToCloseWithBalance(
					dashboard.getExpectedToCloseWithBalance() - entry.getEntryValue()
				);
			}	
		}
	}
	
	protected boolean isAnEntryBeforeTheBeginningOfTheMonth(Entry entry) {
		Calendar entryDate = entry.getEntryDate();
		Calendar monthEndDate = (Calendar) entryDate.clone();
		int lastDayOfTheMonth = monthEndDate.getActualMaximum(Calendar.DAY_OF_MONTH);
		monthEndDate.set(Calendar.DAY_OF_MONTH, lastDayOfTheMonth);
		return entryDate.before(monthEndDate);
		
		
	}
	
	protected void handleMonthlyRevenueValue(Entry entry) {
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
		
		if(entry.getEntryDate().get(Calendar.MONTH) == currentMonth) {
			
		}
	}
	
	protected void handleMonthlyEntryValues(Entry entry) {
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
		
		if(		
			entry.isRevenue() &&
			entry.getEntryDate().get(Calendar.MONTH) == currentMonth
		) {
			dashboard.setMonthlyRevenueValue(
					dashboard.getMonthlyRevenueValue() + entry.getEntryValue()
			);
		}
		
		if(		
			!entry.isRevenue() &&
			entry.getEntryDate().get(Calendar.MONTH) == currentMonth
		) {
			dashboard.setMonthlyExpenseValue(
					dashboard.getMonthlyExpenseValue() + entry.getEntryValue()
			);
		}
	}
}

	

	
