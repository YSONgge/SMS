package service.impl;

import dao.IUserDao;
import dao.impl.IUserDaoImpl;
import entity.User;
import service.IUserService;

/**
 * @author yeye
 * 
 */
public class IUserServiceImpl implements IUserService {
	IUserDao dao = null;

	public IUserServiceImpl(){
		dao = new IUserDaoImpl();
	}
	@Override
	public boolean userRegisterAccount(User u) {
		return dao.userRegisterAccount(u);
	}

	@Override
	public boolean userLogin(User u) {
		return dao.userLogin(u);
	}
	@Override
	public boolean checkAccountExist(User u) {
		return dao.checkAccountExist(u);
	}

}
