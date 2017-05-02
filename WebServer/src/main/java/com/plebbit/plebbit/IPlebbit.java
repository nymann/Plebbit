package com.plebbit.plebbit;

import com.plebbit.dto.Item;
import com.plebbit.dto.ListProperties;

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
    @WebMethod
    String login(String username, String password);

    @WebMethod
    void forgotPassword(String username);

    @WebMethod
    void changePassword(String username, String oldPassword, String newPassword);

    @WebMethod
    int[] getListOfUser(String token);

    @WebMethod
    ListProperties getListFromId(int id, String token);

    @WebMethod
    boolean addUserToList(int listId, String token, String inviteUserName);

    @WebMethod
    boolean createNewList(String token, String listname);

    @WebMethod
    boolean deleteList(String token, int listId);

    @WebMethod
    boolean addItemToList(String token, String item, int listId);

    @WebMethod
    boolean removeItemFromList(String token, String item, int listId);

    @WebMethod
    void logout(String token);

    @WebMethod
    boolean tokenStillValid(String token);

    @WebMethod
    int getPassedSecondsSinceLastChange(int listid);

    @WebMethod
    boolean setBoughtItem(int listId, String itemName, boolean bought, String token);

    @WebMethod
    Item getItem(int listId, String itemName, String token);

    @WebMethod
    boolean renameItemName(int listId, String itemName, String newItemName, String token);

    @WebMethod
    boolean renameListName(int listId, String newListName, String token);

}
