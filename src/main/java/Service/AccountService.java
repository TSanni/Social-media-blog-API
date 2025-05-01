package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }
    
    public Account registerNewAccount(Account account) {
        Account newAccount = null;

        if ((account.getUsername() != "") && (account.getPassword().length() >= 4)) {
            newAccount = accountDAO.registerNewAccount(account);
        }

        return newAccount;
    }

    public Account loginAccount(Account account) {
        Account loggedInAccount = null;

        loggedInAccount = accountDAO.loginToAccount(account);

        return loggedInAccount;
    }
}