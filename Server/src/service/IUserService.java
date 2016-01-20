package service;

import entity.User;

/**
 * @author yeye
 * 
 */
public interface IUserService {
	public boolean userRegisterAccount(User u);

	public boolean userLogin(User u);
	
	public boolean checkAccountExist(User u);
	
	public User selectUserId(String account);
}
