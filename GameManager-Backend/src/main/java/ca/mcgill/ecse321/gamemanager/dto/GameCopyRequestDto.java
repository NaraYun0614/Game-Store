package ca.mcgill.ecse321.gamemanager.dto;

import ca.mcgill.ecse321.gamemanager.model.Game;
import ca.mcgill.ecse321.gamemanager.model.GameCopy;
import com.fasterxml.jackson.annotation.JsonCreator;

public class GameCopyRequestDto extends GameDto{

    public GameCopyRequestDto(){}
    public GameCopyRequestDto(Game game){
        super(game);
    }
//    private GameDto gameDto;
//    @JsonCreator
//    public GameCopyRequestDto(GameDto gameDto) {
//        this.gameDto = gameDto;
//    }

//    public GameDto getGame(){
//        return this.gameDto;
//    }
//    public void setGame(GameDto aGameDto) {
//        this.gameDto = aGameDto;
//    }
//    public int getGameId() {return this.gameDto.getGameId();}
}
