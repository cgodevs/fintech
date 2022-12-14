package br.com.fiap.fintech.dao.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.fiap.fintech.dao.RevenueDAO;
import br.com.fiap.fintech.entities.Revenue;
import br.com.fiap.fintech.singleton.ConnectionManager;

public class OracleRevenueDAO implements RevenueDAO {
	private Connection connection;

	private PreparedStatement stmt;
	
	public OracleRevenueDAO() { super(); };

	@Override
	public void insert(Revenue revenue) {
		this.connection = null;
		this.stmt = null;

		try {
			this.connection = ConnectionManager.getInstance().getConnection();

			String sql = "INSERT INTO T_RECEITA (cd_receita, cd_usuario, vl_receita, nm_receita, dt_receita, st_recebido, st_receita_fixa, txt_descricao) " + 
			             "VALUES (SEQ_RECEITA.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
			
			java.sql.Date data = new java.sql.Date(revenue.getEntryDate().getTimeInMillis());

			this.stmt = connection.prepareStatement(sql);
			
			stmt.setInt(1, revenue.getUserCode());
			stmt.setDouble(2, revenue.getEntryValue());
			stmt.setString(3, revenue.getEntryName());
			stmt.setDate(4, data);
			stmt.setString(5, revenue.getIsReceived());
			stmt.setString(6, revenue.getIsFixedEntry());
			stmt.setString(7, revenue.getDescription());

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				this.stmt.close();
				this.connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	} 

	@Override
	public List<Revenue> getAll() {
		this.stmt = null;
		List<Revenue> revenueList = new ArrayList<Revenue>();
		ResultSet result = null;
		try {
			this.connection = ConnectionManager.getInstance().getConnection();
			stmt = connection.prepareStatement("SELECT * FROM T_RECEITA");
			
			result = stmt.executeQuery();

			Revenue revenue = null;
			while (result.next()) {
				java.sql.Date data = result.getDate("dt_receita");
				Calendar revenueDate = Calendar.getInstance();
				revenueDate.setTimeInMillis(data.getTime());

				revenue = new Revenue(
						result.getInt("cd_receita"), 
						result.getInt("cd_usuario"),
						result.getDouble("vl_receita"), 
						result.getString("nm_receita"), 
						revenueDate,
						result.getString("st_recebido"),
						result.getString("st_receita_fixa"), 
						result.getString("txt_descricao")
				);

				revenueList.add(revenue);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				result.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return revenueList;
	}

	@Override
	public void remove(int revenueCode) {
		this.stmt = null;
		
		try {
			this.connection = ConnectionManager.getInstance().getConnection();

			stmt = connection.prepareStatement("DELETE FROM T_RECEITA WHERE cd_receita = ?");
			stmt.setInt(1, revenueCode);
			stmt.executeUpdate();
		} catch (SQLException e) {
				e.printStackTrace();
		} finally {
			try {
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Revenue fetchById(int revenueCode) {
		this.stmt = null;
		Revenue revenue = null;
		ResultSet result = null;

		try {
			this.connection = ConnectionManager.getInstance().getConnection();

			this.stmt = this.connection.prepareStatement("SELECT * FROM T_RECEITA WHERE cd_receita = ?");
			this.stmt.setInt(1, revenueCode);

			result = this.stmt.executeQuery();

			if (result.next()) {
				System.out.println("entrei");
				java.sql.Date data = result.getDate("dt_receita");
				Calendar revenueDate = Calendar.getInstance();
				revenueDate.setTimeInMillis(data.getTime());

				revenue = new Revenue(
						result.getInt("cd_receita"),
						result.getInt("cd_usuario"),
						result.getDouble("vl_receita"),
						result.getString("nm_receita"),
						revenueDate,
						result.getString("st_recebido"),
						result.getString("st_receita_fixa"),
						result.getString("txt_descricao")
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				result.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return revenue;
	}
	
	@Override
	public List<Revenue> fetchAllByUserCode(int userCode) {
		this.stmt = null;
		List<Revenue> revenueList = new ArrayList<Revenue>();
		ResultSet result = null;

		try {
			this.connection = ConnectionManager.getInstance().getConnection();

			stmt = connection.prepareStatement("SELECT * FROM T_RECEITA WHERE cd_usuario = ?");
			stmt.setInt(1, userCode);

			result = stmt.executeQuery();
			
			Revenue revenue = null;
			
			while (result.next()) {
				java.sql.Date data = result.getDate("dt_receita");
				Calendar revenueDate = Calendar.getInstance();
				revenueDate.setTimeInMillis(data.getTime());

				revenue = new Revenue(
						result.getInt("cd_receita"), 
						result.getInt("cd_usuario"),
						result.getDouble("vl_receita"), 
						result.getString("nm_receita"), 
						revenueDate,
						result.getString("st_recebido"),
						result.getString("st_receita_fixa"), 
						result.getString("txt_descricao")
				);

				revenueList.add(revenue);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return revenueList;
	}

	@Override
	public void update(Revenue revenue) {
		this.stmt = null;

		try {
			this.connection = ConnectionManager.getInstance().getConnection();

			String sql = "UPDATE T_RECEITA SET cd_usuario = ?, vl_receita = ?, nm_receita = ?, txt_descricao = ? ,dt_receita = ?, st_recebido = ?, st_receita_fixa = ? WHERE cd_receita = ?";

			stmt = connection.prepareStatement(sql);

			java.sql.Date data = new java.sql.Date(revenue.getEntryDate().getTimeInMillis());

			stmt.setDouble(1, revenue.getUserCode());
			stmt.setDouble(2, revenue.getEntryValue());
			stmt.setString(3, revenue.getEntryName());
			stmt.setString(4, revenue.getDescription());
			stmt.setDate(5, data);
			stmt.setString(6, revenue.getIsReceived());
			stmt.setString(7, revenue.getIsFixedEntry());
			stmt.setInt(8, revenue.getEntryCode());

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}