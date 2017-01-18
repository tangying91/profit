package org.profit.persist.domain.sys;

import java.io.Serializable;

/**
 * @author TangYing
 */
public class User implements Serializable {

    private int userId;
    private String account;
    private String password;

    public User() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
