package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.LogUtil;
import dao.IUserDao;
import db.ConnectionMysql;
import entity.User;

/**
 * @author yeye
 * 
 */
public class IUserDaoImpl implements IUserDao {

	@Override
	public boolean userRegisterAccount(User u) {
		boolean flag = false;
		Connection conn = new ConnectionMysql().getConnection();
		PreparedStatement pstmt = null;
		String sql = "insert into t_user_account(account,password) values (?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, u.getAccount());
			pstmt.setString(2, u.getPassword());
			int i = pstmt.executeUpdate();
			if (i == 1) {
				flag = true;
			}
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			close(conn, pstmt, null);
		}
		return flag;
	}

	@Override
	public boolean userLogin(User u) {
		Connection conn = new ConnectionMysql().getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		String sql = "select * from t_user_account where account=? and password=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, u.getAccount());
			pstmt.setString(2, u.getPassword());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			close(conn, pstmt, rs);
		}
		return flag;
	}

	@Override
	public boolean checkAccountExist(User u) {
		Connection conn = new ConnectionMysql().getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		String sql = "select * from t_user_account where account=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, u.getAccount());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			close(conn, pstmt, rs);
		}
		return flag;

	}

	@Override
	public User selectUserId(String account) {
		Connection conn = new ConnectionMysql().getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		User u = null;
		String sql = "select user_id from t_user_account where account=?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, account);
			rs = ps.executeQuery();
			while (rs.next()) {
				int userId = rs.getInt(1);
				u = new User(userId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, ps, rs);
		}
		return u;
	}

	private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			LogUtil.e(e);
		}
	}

}
