package com.example.smartcloudstorage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterRegexApply {
    private String password;
    private String email;

    public boolean validatePassword(String password){
        String regexPassword = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";
        Pattern patternPassword = Pattern.compile(regexPassword);
        Matcher m = patternPassword.matcher(password);
        return m.matches();
    }

    public boolean validateEmail(String email){
        String emailPassword = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        Pattern patternemail = Pattern.compile(emailPassword);
        Matcher m = patternemail.matcher(email);
        return m.matches();
    }
}
