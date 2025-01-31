package ca.mcgill.ecse321.gamemanager.dto;

public class OwnerDto {
    private String name;
    private String email;

    public OwnerDto() {}

    public OwnerDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
