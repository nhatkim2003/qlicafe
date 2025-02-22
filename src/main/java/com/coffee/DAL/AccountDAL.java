package com.coffee.DAL;

import com.coffee.DTO.Account;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAL extends Manager{
    public AccountDAL() {
        super("account",
                List.of("id",
                        "username",
                        "password",
                        "staff_id"));
    }

    public List<Account> convertToAccounts(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Account(
                        Integer.parseInt(row.get(0)), // id
                        row.get(1), // username
                        row.get(2), // password
                        Integer.parseInt(row.get(3)) // staff_id
                );
            } catch (Exception e) {
                System.out.println("Error occurred in AccountDAL.convertToAccounts(): " + e.getMessage());
            }
            return new Account();
        });
    }

    public int addAccount(Account account) {
        try {
            System.out.println(account.getPassword());
            return create(account.getId(),
                    account.getUsername(),
                    "",
                    account.getStaff_id()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in AccountDAL.addAccount(): " + e.getMessage());
        }
        return 0;
    }

    public int updateAccount(Account account) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(account.getId());
            updateValues.add(account.getUsername());
            updateValues.add(account.getPassword());
            updateValues.add(account.getStaff_id());
            return update(updateValues, "id = " + account.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in AccountDAL.updateAccount(): " + e.getMessage());
        }
        return 0;
    }

    public int updateAccountPassword(Account account) {
        try {
            String query = "UPDATE `" + getTableName() + "` SET password = '" + account.getPassword() + "' WHERE id = " + account.getId() + ";";
            return executeUpdate(query);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in AccountDAL.updateAccountPassword(): " + e.getMessage());
        }
        return 0;
    }

    public int deleteAccount(String... conditions) {
        try {
            return delete(conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in AccountDAL.deleteAccount(): " + e.getMessage());
        }
        return 0;
    }

    public List<Account> searchAccounts(String... conditions) {
        try {
            return convertToAccounts(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in AccountDAL.searchAccounts(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
