package domain;

public class UserInfo {
    private String username;
    private String password;
    private String userDBName;
    public UserInfo(){

    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserDBName() {
        return userDBName;
    }

    public void setUserDBName(String userDBName) {
        this.userDBName = userDBName;
    }
}
