package entity;

/**
 * @author yeye
 *
 */
public class User {
	private int userId;
	private String account;
	private String password;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(int userId, String account, String password) {
		super();
		this.userId = userId;
		this.account = account;
		this.password = password;
	}
	
	

	public User(String account, String password) {
		super();
		this.account = account;
		this.password = password;
	}
	
	public User(int userId) {
		super();
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
