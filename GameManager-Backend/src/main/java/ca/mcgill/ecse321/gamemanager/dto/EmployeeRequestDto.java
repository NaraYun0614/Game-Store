package ca.mcgill.ecse321.gamemanager.dto;

public class EmployeeRequestDto {
    private String name;
    private String email;
    private String password;

    public EmployeeRequestDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
