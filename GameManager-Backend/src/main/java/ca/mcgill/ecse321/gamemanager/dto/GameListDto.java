package ca.mcgill.ecse321.gamemanager.dto;

import java.util.ArrayList;
import java.util.List;

public class GameListDto {
    private List<GameDto> games;

    public GameListDto() {
        this.games = new ArrayList<>();
    }

    public GameListDto(List<GameDto> games) {
        this.games = games;
    }

    public List<GameDto> getGames() {
        return games;
    }

    public void setGames(List<GameDto> games) {
        this.games = games;
    }

}
