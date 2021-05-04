package om.tracked;

import java.io.Serializable;

public class User implements Serializable {
    // VARIABLES
    private String username;
    private String pw;

    public void Class() {
    };

    // METHODS - Setters
    public void setUsername(String username) {
        this.username = username;
    };

    public void setPw(String password){
        this.pw = password;
    };


    // METHODS - Getters
    public String getUsername() {
        return username;
    }
    public String getPw() {
        return pw;
    }

}
