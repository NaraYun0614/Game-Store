package ca.mcgill.ecse321.gamemanager.dto;

import ca.mcgill.ecse321.gamemanager.model.Game;
import ca.mcgill.ecse321.gamemanager.model.GameCopy;

public class GameCopyResponseDto {
    private int gameCopyId;
    private GameDto gameDto;

    @SuppressWarnings("unused")
    public GameCopyResponseDto(){}

    public GameCopyResponseDto(GameCopy gameCopy) {
        this.gameCopyId = gameCopy.getGameCopyId();
        Game game = gameCopy.getGame();
        this.gameDto = new GameDto(game);
    }

    public int getGameCopyId() {
        return gameCopyId;
    }
    public GameDto getGameDto() {return gameDto;}
}
