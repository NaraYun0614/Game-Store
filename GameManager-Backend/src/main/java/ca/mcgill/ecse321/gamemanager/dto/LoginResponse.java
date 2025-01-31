package ca.mcgill.ecse321.gamemanager.dto;

public class LoginResponse {
    private String userEmail;
    private String message;
    private boolean success;

    public LoginResponse(){

    }
    public LoginResponse(String userEmail, String message, boolean success) {
        this.userEmail = userEmail;
        this.message = message;
        this.success = success;
    }

    // Getters and setters
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
