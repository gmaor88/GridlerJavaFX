package Utils;

import Logic.GameBoard;
import Logic.GamePlayer;
import Logic.Square;
import jaxb.GameDescriptor;

import java.util.ArrayList;

/**
 * Created by dan on 8/19/2016.
 * Class for handling the parsing and of the given xml.
 * Verifies the data from the xml is in the right form.
 */


public class GameLoader {
    public GameBoard loadBoard(GameDescriptor i_GameDescriptor) throws GameLoadException{
        int columns, rows, blocks[], numberOfSlices, slicesId, numberOfBlackSquares;
        int columnIndex, rowIndex;
        GameBoard board;

        //get basic data from xml and look for exceptions
        if(!i_GameDescriptor.getGameType().equalsIgnoreCase("singleplayer")){
            throw new GameLoadException("Invalid Format");
        }

        columns = i_GameDescriptor.getBoard().getDefinition().getColumns().intValue();
        rows = i_GameDescriptor.getBoard().getDefinition().getRows().intValue();
        numberOfSlices = i_GameDescriptor.getBoard().getDefinition().getSlices().getSlice().size();
        numberOfBlackSquares = i_GameDescriptor.getBoard().getSolution().getSquare().size();
        if(rows + columns != numberOfSlices){
            throw new GameLoadException("Invalid number of slices");
        }
        else if(rows*columns < numberOfBlackSquares){
            throw new GameLoadException("Invalid number of black squares");
        }

        board = new GameBoard(rows,columns);

        //gets slices from xml file while also looking for exceptions
        for(int i = 0; i < numberOfSlices ; i++){
            blocks = getBlocks(i_GameDescriptor.getBoard().getDefinition().getSlices().getSlice().get(i).getBlocks());
            slicesId = i_GameDescriptor.getBoard().getDefinition().getSlices().getSlice().get(i).getId().intValue() - 1;
            if(i_GameDescriptor.getBoard().getDefinition().getSlices().getSlice().get(i).getOrientation().equalsIgnoreCase("row")){
                if(getNumberOfBlackSquare(blocks) > columns){
                    throw new GameLoadException("Invalid number of blocks in column "+ slicesId);
                }

                board.setHorizontalSlice(slicesId,blocks);
            }
            else if(i_GameDescriptor.getBoard().getDefinition().getSlices().getSlice().get(i).getOrientation().equalsIgnoreCase("column")){
                if(getNumberOfBlackSquare(blocks) > rows){
                    throw new GameLoadException("Invalid number of blocks in row "+ slicesId);
                }

                board.setVerticalSlice(slicesId, blocks);
            }
            else{
                throw new GameLoadException("Invalid orientation");
            }
        }

        //inserts true value to designated squares
        for(int i = 0; i < numberOfBlackSquares; i++) {
            rowIndex = i_GameDescriptor.getBoard().getSolution().getSquare().get(i).getRow().intValue();
            columnIndex = i_GameDescriptor.getBoard().getSolution().getSquare().get(i).getColumn().intValue();
            try {
                board.getSquare(rowIndex, columnIndex).setTrueSquareSignValue(Square.eSquareSign.BLACKED);
            }
            catch (ArrayIndexOutOfBoundsException e){
                throw new GameLoadException(e.getMessage());
            }
        }

        return board;
    }

    public GamePlayer loadPlayer(GameDescriptor i_GameDescriptor){
        GamePlayer player;
        String playerId,playerName;
        Integer maxNumberOfMoves;
        boolean humanPlayer;
        //gives you the basic option to load player data from xml file

        playerId = i_GameDescriptor.getMultiPlayers().getPlayers().getPlayer().get(0).getId().toString();
        playerName = i_GameDescriptor.getMultiPlayers().getPlayers().getPlayer().get(0).getName();
        humanPlayer = i_GameDescriptor.getMultiPlayers().getPlayers().getPlayer().get(0).getPlayerType().equalsIgnoreCase("Human");
        player = new GamePlayer(humanPlayer, playerName, playerId);
        if(!humanPlayer && Tools.tryParseInt(i_GameDescriptor.getMultiPlayers().getMoves())){
            maxNumberOfMoves = Integer.parseInt(i_GameDescriptor.getMultiPlayers().getMoves());
            player.setMoveLimit(maxNumberOfMoves);
        }

        return  player;
    }

    private int getNumberOfBlackSquare(int[] blocks) {
        int sum = 0;

        for(int i:blocks){
            sum += i + 1;
        }

        return  sum - 1;
    }

    private int[] getBlocks(String i_Blocks) throws GameLoadException {
        int blocks[];
        char ch;
        ArrayList<Integer> intermediate = new ArrayList<>(1);
        //first we trim any whitespace
        //then we split the string in to smaller ones with the , separator
        //then intermediate gets the numbers from the sub strings

        i_Blocks = i_Blocks.replaceAll(" ","");
        for(int i = 0; i < i_Blocks.length(); i++){
            ch = i_Blocks.charAt(i);
            if(('9' < ch || ch < '0') && ch != ','){
                throw new GameLoadException("Invalid string of blocks");
            }
        }

        for(String str: i_Blocks.split(",")){
            if(Tools.tryParseInt(str)) {
                intermediate.add(Integer.parseInt(str));
            }
        }

        blocks = new int[intermediate.size()];
        for(int i = 0; i < intermediate.size(); i++){
            blocks[i] = intermediate.get(i);
        }

        return blocks;
    }
}
