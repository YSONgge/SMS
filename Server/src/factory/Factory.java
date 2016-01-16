package factory;

import service.IUserService;
import service.impl.IUserServiceImpl;

public class Factory {
	public static IUserService getIUserService() {
		return new IUserServiceImpl();
	}
}
