package com.coffee.BLL;

import com.coffee.DAL.AccountDAL;
import com.coffee.DTO.Account;
import com.coffee.DTO.Staff;
import com.coffee.utils.Email;
import com.coffee.utils.Password;
import com.coffee.utils.VNString;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccountBLL extends Manager<Account> {
    private AccountDAL accountDAL;

    public AccountBLL() {
        accountDAL = new AccountDAL();
    }

    public Object[][] getData() {
        List<Account> accountList = accountDAL.searchAccounts();
        return getData(accountList);
    }

    public Pair<Boolean, String> addAccount(Account account) {
        Pair<Boolean, String> result;

        result = validateUserName(account.getUsername());
        if (!result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        result = exists(account);
        if (result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        if (accountDAL.addAccount(account) == 0)
            return new Pair<>(false, "Thêm tài khoản không thành công.");

        String password = Password.generateRandomPassword(8);
        System.out.println(password);
        String hashedPassword = Password.hashPassword(password);
        account.setPassword("first" + hashedPassword);
        accountDAL.updateAccountPassword(account);

        Staff staff = new StaffBLL().searchStaffs("id = " + account.getStaff_id()).get(0);
        Email.sendOTP(staff.getEmail(), "Mở tài khoản nhân viên ",
                "<html><p>Xin thông báo thông tin tài khoản của nhân viên</p>" +
                        "    <p>Tên đăng nhập là: <strong>" + account.getUsername() + "</strong></p>" +
                        "    <p>Mật khẩu mặc định là: <strong>" + password + "</strong></p>" +
                        "    <p>Vui lòng thực hiện thay đổi mật khẩu.</p></html>");
        
        return new Pair<>(true, "Thêm tài khoản thành công.");
    }

    public Pair<Boolean, String> updateAccount(Account account) {
        Pair<Boolean, String> result;

        result = validateUserName(account.getUsername());
        if (!result.getKey()) {
            return new Pair<>(false, result.getValue());
        }

        if (accountDAL.updateAccount(account) == 0)
            return new Pair<>(false, "Cập nhật tài khoản không thành công.");

        return new Pair<>(true, "Cập nhật tài khoản thành công.");
    }

    public Pair<Boolean, String> updateAccountPassword(Account account, String password) {
        Pair<Boolean, String> result;
        result = validatePassWord(password);
        if (!result.getKey()) {
            return new Pair<>(false, result.getValue());
        }
        String hashedPassword = Password.hashPassword(password);
        account.setPassword("first" + hashedPassword);

        if (accountDAL.updateAccountPassword(account) == 0)
            return new Pair<>(false, "Thay đổi mật khẩu không thành công.");

        return new Pair<>(true, "Thay đổi mật khẩu thành công.");
    }

    public Pair<Boolean, String> deleteAccount(Account account) {
        if (accountDAL.deleteAccount("id = " + account.getId()) == 0)
            return new Pair<>(false, "Xoá tài khoản không thành công.");

        return new Pair<>(true, "Xoá tài khoản thành công.");
    }

    public List<Account> searchAccounts(String... conditions) {
        return accountDAL.searchAccounts(conditions);
    }

    public List<Account> findAccounts(String key, String value) {
        List<Account> list = new ArrayList<>();
        List<Account> accountList = accountDAL.searchAccounts();
        for (Account account : accountList) {
            if (getValueByKey(account, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(account);
            }
        }
        return list;
    }

    public List<Account> findAccountsBy(Map<String, Object> conditions) {
        List<Account> accounts = accountDAL.searchAccounts();
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            accounts = findObjectsBy(entry.getKey(), entry.getValue(), accounts);
        return accounts;
    }

    public Pair<Boolean, String> exists(Account newAccount) {
        List<Account> accounts = accountDAL.searchAccounts("username = '" + newAccount.getUsername() + "'");
        if (!accounts.isEmpty()) {
            return new Pair<>(true, "Tên tài khoản đã tồn tại.");
        }
        return new Pair<>(false, "");
    }

    public Pair<Boolean, String> validatePassWord(String passWord) {
        if (passWord.isBlank())
            return new Pair<>(false, "Mật khẩu tài khoản không được bỏ trống.");
        if (passWord.contains(" "))
            return new Pair<>(false, "Mật khẩu tài khoản không được có khoảng trắng.");
        if (!VNString.containsUpperCase(passWord))
            return new Pair<>(false, "Mật khẩu phải chứa ít nhất 1 chữ cái in hoa.");
        if (!VNString.containsNumber(passWord))
            return new Pair<>(false, "Mật khẩu phải chứa ít nhất 1 chữ số.");
        if (!VNString.containsSpecial(passWord))
            return new Pair<>(false, "Mật khẩu phải chứa ít nhất 1 ký tự đặc biệt.");
        if (!VNString.containsLowerCase(passWord))
            return new Pair<>(false, "Mật khẩu phải chứa ít nhất 1 chữ cái thường.");
        return new Pair<>(true, passWord);
    }


    public Pair<Boolean, String> validateUserName(String username) {
        if (username.isBlank())
            return new Pair<>(false, "Tên tài khoản không được để trống.");
        if (VNString.containsUnicode(username))
            return new Pair<>(false, "Tên tài khoản không được chứa ký tự không hỗ trợ.");
        if (VNString.containsSpecial(username))
            return new Pair<>(false, "Tên tài khoản không được chứa ký tự đặc biệt.");
        return new Pair<>(true, username);
    }

    @Override
    public Object getValueByKey(Account account, String key) {
        return switch (key) {
            case "id" -> account.getId();
            case "username" -> account.getUsername();
            case "staff_id" -> account.getStaff_id();
            default -> null;
        };

    }

    public static void main(String[] args) {
        AccountBLL accountBLL = new AccountBLL();
        Account account = new Account(accountBLL.getAutoID(accountBLL.searchAccounts()), "ducanh", 4);
        accountBLL.addAccount(account);
//
//        account.setStaff_id(8);
//        accountBLL.updateAccount(account);
//
//        accountBLL.deleteAccount(account);
        System.out.println(accountBLL.searchAccounts());

    }
}
