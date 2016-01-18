package dao;

import entity.User;

public interface IUserDao {

	public boolean userRegisterAccount(User u);

	public boolean userLogin(User u);
	
	public boolean checkAccountExist(User u);

}
