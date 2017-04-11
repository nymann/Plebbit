package com.plebbit.plebbit;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface IPlebbit {
	/**
	 * 
	 * @param username
	 * @param password
	 * @return token which will be used to confirm name
	 */
	@WebMethod public String login(String username, String password);

}
