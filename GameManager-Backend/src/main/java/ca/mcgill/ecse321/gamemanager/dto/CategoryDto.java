package ca.mcgill.ecse321.gamemanager.dto;

import java.util.List;

public class CategoryDto {

    private int categoryId;
    private String name;
    private String description;
    private List<Integer> gameIds;  // List of associated game IDs

    @SuppressWarnings("unused")
    private CategoryDto() {}

    public CategoryDto(int categoryId, String name, String description, List<Integer> gameIds) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.gameIds = gameIds;
    }

    // Getters and setters
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getGameIds() {
        return gameIds;
    }

    public void setGameIds(List<Integer> gameIds) {
        this.gameIds = gameIds;
    }
}
