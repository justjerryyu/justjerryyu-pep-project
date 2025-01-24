package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }


    /**
     * This is used for when a mock accountDAO that exhibits mock behavior is used in the test cases.
     * This would allow the testing of accountService independently of accountDAO.
     * 
     * @param accountDAO
     */
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    
    /**
     * Add a new account to the database.
     * 
     * The registration will be successful if and only if the username is not 
     * blank, the password is at least 4 characters long, and an Account with 
     * that username does not already exist
     *
     * @param account an object representing a new account.
     * @return the newly added account if the add operation was successful, including the account_id. 
     */
    public Account insertAccount(Account account) {
        if (account.username.length() > 0 && account.password.length() >= 4) {
            // username is always unique per the account table
            return accountDAO.insertAccount(account);
        }

        return null;
    }


    /**
     * Retrieve a specific account using its username.
     *
     * @param username an account username
     * @return the account object, null if username does not exist
     */
    public Account getAccountByUsername(String username) {
        return accountDAO.getAccountByUsername(username);
    }
    

    /**
     * Verify Login given account object without account id.
     * 
     * The login will be successful if and only if the username and password provided 
     * in the request body JSON match a real account existing on the database. 
     *
     * @param username an account username
     * @return the account object, null if username does not exist
     */
    public Account login(Account account) {
        Account acc = accountDAO.getAccountByUsername(account.username);
        if (acc != null && acc.username.equals(account.username) && acc.password.equals(account.password)) {
            return acc;
        }

        return null;
    }
}
