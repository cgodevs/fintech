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

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Revenue> revenueList = revenueDao.getAll();
		List<Expense> expenseList = expenseDao.getAll();
		
		ArrayList<Entry> allEntries = getAllEntries(revenueList, expenseList);
		
		setDashboard(request ,allEntries);
		
		request.getRequestDispatcher("dashboard.jsp").forward(request, response);
	}
	
	private ArrayList<Entry> getAllEntries(List<Revenue> revenueList, List<Expense> expenseList) {
		ArrayList<Entry> entryList = new ArrayList<Entry>();
		entryList.addAll(revenueList);
		entryList.addAll(expenseList);
		return entryList;
	}
	
	private void setDashboard(HttpServletRequest request, ArrayList<Entry> entries) {
		for(Entry entry: entries) {		
			handleStartWithBalance(entry);
			handleCurrentBalance(entry);
			handleExpectedToCloseWithBalance(entry);
			handleMonthlyEntryValues(entry);
		}
		
		dashboard.setComingNextEntries(takeSomeNextEntries(entries, 5) );
		
		request.setAttribute("dashboardData", dashboard);	
	}

	private void handleStartWithBalance(Entry entry) {
		if(isAnEntryBeforeTheBeginningOfTheMonth(entry)) {
			if(entry instanceof Revenue) 
				dashboard.setStartWithBalance(dashboard.getStartWithBalance() + entry.getEntryValue());
			else 
				dashboard.setStartWithBalance(dashboard.getStartWithBalance() - entry.getEntryValue());
		}
	}
	
	private boolean isAnEntryBeforeTheBeginningOfTheMonth(Entry entry) {
		Calendar entryDate = entry.getEntryDate();
		Calendar thisMonthFirstDay = Calendar.getInstance();
		thisMonthFirstDay.set(Calendar.DAY_OF_MONTH, 1);
		return entryDate.before(thisMonthFirstDay);
	}
	
	private void handleCurrentBalance(Entry entry) {
		if(isAnEntryBeforeTheCurrentDate(entry)) {
			if(entry instanceof Revenue) 
				dashboard.setCurrentBalance(dashboard.getCurrentBalance() + entry.getEntryValue());
			else 
				dashboard.setCurrentBalance(dashboard.getCurrentBalance() - entry.getEntryValue());
		}
	}
	
	private boolean isAnEntryBeforeTheCurrentDate(Entry entry) {
		Calendar entryDate = entry.getEntryDate();
		Calendar currentDate = Calendar.getInstance();
		return entryDate.before(currentDate);
	}

	private void handleExpectedToCloseWithBalance(Entry entry) {
		if(isAnEntryBeforeTheNextMonth(entry)) {
			if(entry instanceof Revenue) {
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
	
	private boolean isAnEntryBeforeTheNextMonth(Entry entry) {
		Calendar entryDate = entry.getEntryDate();
		Calendar nextMonthFirstDay = Calendar.getInstance();
		nextMonthFirstDay.add(Calendar.MONTH, 1);
		return entryDate.before(nextMonthFirstDay);
	}
	
	private void handleMonthlyEntryValues(Entry entry) {
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
		
		if(		
			(entry instanceof Revenue) &&
			entry.getEntryDate().get(Calendar.MONTH) == currentMonth
		) {
			dashboard.setMonthlyRevenueValue(
					dashboard.getMonthlyRevenueValue() + entry.getEntryValue()
			);
		}
		
		if(		
			(entry instanceof Expense) &&
			entry.getEntryDate().get(Calendar.MONTH) == currentMonth
		) {
			dashboard.setMonthlyExpenseValue(
					dashboard.getMonthlyExpenseValue() + entry.getEntryValue()
			);
		}
	}
	
	private static ArrayList<Entry> takeSomeNextEntries(ArrayList<Entry> allEntries, int numberOfEntries){
		ArrayList<Entry> nextEntries = new ArrayList<Entry>();
		ArrayList<Entry> futureEntries = getFutureEntries(allEntries);
		for(int n = 0; n < numberOfEntries; n++) 
			nextEntries.add(futureEntries.get(n));
		
		return sortEntries(nextEntries);
	}
	
	private static ArrayList<Entry> getFutureEntries(ArrayList<Entry> entries) {
		ArrayList<Entry> nextEntries = new ArrayList<Entry>();
		Calendar today = Calendar.getInstance();
		
		for (Entry entry: entries) {
			if (entry.getEntryDate().after(today)) {
				nextEntries.add(entry);
			}
		}
		return nextEntries;
	}
	
	private static ArrayList<Entry> sortEntries(ArrayList<Entry> entries) {	
		for (int i = 0; i < entries.size(); i++) { 
			Entry currentEntry = entries.get(i);
			Entry comparingEntry = entries.get(i+1);
			if (currentEntry.getEntryDate().after(comparingEntry.getEntryDate())){
				entries.set(i, comparingEntry);
				entries.set(i+1, currentEntry);
			}
		}
		
		return entries;
	}
	
}