package com.plebbit.plebbit;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.plebbit.dto.ListProperties;

@WebService
public interface IPlebbit {
    /**
     * @param username
     * @param password
     * @return token which will be used to confirm name
     */
    @WebMethod
    String login(String username, String password);

    @WebMethod
    void forgotPassword(String username);

    @WebMethod
    void changePassword(String username, String oldPassword, String newPassword);

    @WebMethod
    int[] getListOfUser(String token); // Get shopping lists of the specified user.

    @WebMethod
    ListProperties getListFromId(int id, String token); // Given the ID of a shopping list and a token id of a user, return the shopping list.

    @WebMethod
    boolean addUserToList(int listId, String token, String inviteUserName);

    @WebMethod
    void createNewList(String token, String listname);

    @WebMethod
    boolean deleteList(String token, int listId);

    @WebMethod
    void addItemToList(String token, String item, int listId);

    @WebMethod
    boolean removeItemFromList(String token, String item, int listId);

    @WebMethod
    void logout(String token);

}
