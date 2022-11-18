package br.com.fiap.fintech.tests;

import java.util.List;

import br.com.fiap.fintech.dao.RevenueDAO;
import br.com.fiap.fintech.entities.Revenue;
import br.com.fiap.fintech.factory.DAOFactory;

public class Test {
	public static void main(String[] args) {
		try {
			System.out.println("rodei");
			RevenueDAO revenueDao = DAOFactory.getRevenueDAO();
			List<Revenue> listOfAllRevenues = revenueDao.getAll();
			for(Revenue revenueItem: listOfAllRevenues) {
				System.out.println(
						"Código da receita: " + revenueItem.getRevenueCode() + "\n" +
						"Código do usuário dono da receita: " + revenueItem.getUserCode() + "\n" +
						"Nome da receita: " + revenueItem.getRevenueName() + "\n" +
						"Descrição da receita: " + revenueItem.getDescription() + "\n\n"
				);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
