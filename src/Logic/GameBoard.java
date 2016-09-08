package Logic;

import java.util.ArrayList;
import Logic.Square;
import Utils.GameLoadException;

/**
 * Created by Maor Gershkovitch on 8/8/2016.
 * logic class which holds the sum of most of the logic package and defines the full griddler board
 */
public class GameBoard {

    private Square[][] m_Board;
    private final int f_BoardHeight;
    private final int f_BoardWidth;
    private ArrayList<ArrayList<Block>> m_VerticalSlices;
    private ArrayList<ArrayList<Block>> m_HorizontalSlices;
    private int m_MaxHorizontalSlicesLength = 0;
    private int m_MaxVerticalSlicesLength = 0;

    public GameBoard(int i_BoardHeight, int i_BoardWidth) throws GameLoadException{
        if(100 < i_BoardHeight || i_BoardHeight < 10 || 100 < i_BoardWidth || i_BoardWidth < 10){
            throw new GameLoadException("Invalid board size");
        }
        f_BoardHeight = i_BoardHeight;
        f_BoardWidth = i_BoardWidth;
        m_VerticalSlices = new ArrayList<>();
        initializeSlicesArray(m_VerticalSlices, i_BoardWidth);
        m_HorizontalSlices = new ArrayList<>();
        initializeSlicesArray(m_HorizontalSlices, i_BoardHeight);
        m_Board = new Square[f_BoardHeight][f_BoardWidth];
        initializeBoard();
    }

   /* public GameBoard(GameBoard i_GameBoard) throws GameLoadException{
        this.GameBoard(i_GameBoard.getBoardHeight(), i_GameBoard.getBoardWidth());
    }*/
   public GameBoard(GameBoard i_GameBoard){
       f_BoardHeight = i_GameBoard.getBoardHeight();
       f_BoardWidth = i_GameBoard.getBoardWidth();
       m_VerticalSlices = new ArrayList<>();
       initializeSlicesArray(m_VerticalSlices, f_BoardWidth);
       m_HorizontalSlices = new ArrayList<>();
       initializeSlicesArray(m_HorizontalSlices, f_BoardHeight);
       m_Board = new Square[f_BoardHeight][f_BoardWidth];
       initializeBoard();
       copyGameBoardData(i_GameBoard);
   }

    private void copyGameBoardData(GameBoard i_GameBoard) {
        copySlices(m_VerticalSlices, i_GameBoard.m_HorizontalSlices);
        copySlices(m_HorizontalSlices, i_GameBoard.m_VerticalSlices);
        copyBoard(i_GameBoard.m_Board);
    }

    private void copyBoard(Square[][] i_Board) {
        for(int i = 0; i < f_BoardHeight; i++){
            for (int j = 0; j < f_BoardWidth; j++){
                m_Board[i][j].setTrueSquareSignValue(i_Board[i][j].getTrueSquareSignValue());
            }
        }
    }

    private void copySlices(ArrayList<ArrayList<Block>> i_DestintionSlices, ArrayList<ArrayList<Block>> i_SourceSlices) {
        for(int i = 0; i < i_SourceSlices.size(); i++){
            for (int j = 0; j < i_SourceSlices.get(i).size(); j++){
                i_DestintionSlices.get(i).add(new Block(i_SourceSlices.get(i).get(j).getSize()));
            }
        }
    }

    private void initializeBoard() {
        for (int j = 0; j < f_BoardHeight; j++) {
            for (int i = 0; i < f_BoardWidth; i++) {
                m_Board[j][i] = new Square();
            }
        }
    }

//    public ArrayList getVerticalSlices(){
//        return m_VerticalSlices;
//    }
//
//    public ArrayList getHorizontalSlices(){
//        return m_HorizontalSlices;
//    }

    private void initializeSlicesArray(ArrayList<ArrayList<Block>> i_Slices, int i_Size) {
        for(int i = 0; i < i_Size; i++){
            i_Slices.add(new ArrayList<Block>());
        }
    }

    public int getBoardHeight() {
        return f_BoardHeight;
    }

    public int getBoardWidth() {
        return f_BoardWidth;
    }

    public  int getMaxHorizontalSlicesLength(){return m_MaxHorizontalSlicesLength;}

    public int getMaxVerticalSlicesLength(){return  m_MaxVerticalSlicesLength;}

    public void setVerticalSlice(int i_Width, int [] i_Blocks){
        for(int num: i_Blocks){
            m_VerticalSlices.get(i_Width).add(new Block(num));
        }

        if(m_MaxVerticalSlicesLength < i_Blocks.length){
            m_MaxVerticalSlicesLength = i_Blocks.length;
        }
    }

    public  ArrayList<Block> getVerticalSlice(int i_Width){
        return  m_VerticalSlices.get(i_Width);
    }

    public void setHorizontalSlice(int i_Height, int [] i_Blocks){
        for (int num: i_Blocks){
            m_HorizontalSlices.get(i_Height).add(new Block(num));
        }

        if(m_MaxHorizontalSlicesLength < i_Blocks.length){
            m_MaxHorizontalSlicesLength = i_Blocks.length;
        }
    }

    public  ArrayList<Block> getHorizontalSlice(int i_Height){
        return  m_HorizontalSlices.get(i_Height);
    }

    public Square getSquare(int i_Height, int i_Width) throws ArrayIndexOutOfBoundsException{
        if(1 > i_Height || i_Height > f_BoardHeight){
            throw new ArrayIndexOutOfBoundsException ("Height Out of Bounds");
        }

        if(1 > i_Width || i_Width > f_BoardWidth){
            throw new ArrayIndexOutOfBoundsException ("Width Out of Bounds");
        }

        return  m_Board[i_Height - 1][i_Width - 1];
    }

    public MoveSet insert(int i_StartRow, int i_StartColumn, int i_EndRow, int i_EndColumn, Square.eSquareSign i_Sign, String i_Comment) throws  ArrayIndexOutOfBoundsException{
        MoveSet moveset = new MoveSet(i_Comment);

        if(i_EndRow > f_BoardHeight){
            throw new ArrayIndexOutOfBoundsException("End row is out of bounds");
        }
        else if(i_EndColumn > f_BoardWidth){
            throw new ArrayIndexOutOfBoundsException("End column is out of bounds");
        }

        for(int i = i_StartRow - 1; i < i_EndRow; i++){
            for(int j = i_StartColumn - 1; j < i_EndColumn; j++){
                moveset.AddNewPoint(i + 1, j + 1, m_Board[i][j].getCurrentSquareSign());// was without +1
                m_Board[i][j].setCurrentSquareSign(i_Sign);
            }
        }

        return moveset;
    }

    MoveSet insert(MoveSet i_Move){
        MoveSet moveset = new MoveSet(i_Move.getComment());

        for (Point point:i_Move.getPointsList()){
            moveset.AddNewPoint(point.getRowCord(), point.getColCord(),
                    m_Board[point.getRowCord()][point.getColCord()].getCurrentSquareSign());
            m_Board[point.getRowCord()][point.getColCord()].setCurrentSquareSign(point.getSign());
        }

        return moveset;
    }
    
    public void updateBlocks(){
        clearMarking();
        updateHorizontalBlocks();
        updateVerticalBlocks();
    }

    private void clearMarking() {
        boolean mark = true;

        for(ArrayList<Block> arr: m_HorizontalSlices){
            for (Block block: arr){
                block.setMarked(!mark);
            }
        }

        for(ArrayList<Block> arr: m_VerticalSlices){
            for (Block block: arr){
                block.setMarked(!mark);
            }
        }
    }

    private void blocksScanner(int i_AmountToScan, int i_LengthOfScan, boolean i_HorizontalScan){
        int blockSizeCounter = 0;

        /*
        * runs on the board and finds black squares once a sequence has been found it looks for the relevant block
        * in the appropriate place if found it signals the block of the occurrence
         */
        for(int i = 0; i < i_AmountToScan; i++){
            for(int j = 0; j < i_LengthOfScan; j++){
                if(i_HorizontalScan) {
                    if (m_Board[i][j].getCurrentSquareSign() == Square.eSquareSign.BLACKED) {
                        blockSizeCounter++;
                    } else if (blockSizeCounter != 0) {
                        flagHorizontalBlock(i, blockSizeCounter);
                        blockSizeCounter = 0;
                    }
                }
                else{
                    if (m_Board[j][i].getCurrentSquareSign() == Square.eSquareSign.BLACKED) {
                        blockSizeCounter++;
                    } else if (blockSizeCounter != 0) {
                        flagVerticalBlock(i, blockSizeCounter);
                        blockSizeCounter = 0;
                    }
                }
            }

            if(i_HorizontalScan){
                if (blockSizeCounter != 0) {
                    flagHorizontalBlock(i, blockSizeCounter);
                    blockSizeCounter = 0;
                }
            }
            else{
                if (blockSizeCounter != 0) {
                    flagVerticalBlock(i, blockSizeCounter);
                    blockSizeCounter = 0;
                }
            }
        }
    }

    private void flagVerticalBlock(int i_Index, int i_BlockSize) {
        boolean mark = true;

        for(Block block: m_VerticalSlices.get(i_Index)){
            if(!block.isMarked() && block.getSize() == i_BlockSize){
                block.setMarked(mark);
                return;
            }
        }
    }

    private void flagHorizontalBlock(int i_Index, int i_BlockSize) {
        boolean mark = true;

        for(Block block: m_HorizontalSlices.get(i_Index)){
            if(!block.isMarked() && block.getSize() == i_BlockSize){
                block.setMarked(mark);
                return;
            }
        }
    }

    private void updateVerticalBlocks() {
        boolean horizontalScan = true;

        blocksScanner(f_BoardWidth, f_BoardHeight, !horizontalScan);
    }

    private void updateHorizontalBlocks(){
        boolean horizontalScan = true;

        blocksScanner(f_BoardHeight, f_BoardWidth, horizontalScan);
    }

    public double getBoardCompletionPercentage() {
        double completionPercentage = 0;

        for (int i = 0; i < f_BoardHeight; i++) {
            for (int j = 0; j < f_BoardWidth; j++) {
                if (m_Board[i][j].CheckIfCellMarkedCorrectly()) {
                    completionPercentage++;
                }
            }
        }

        completionPercentage /= (f_BoardHeight * f_BoardHeight);

        return  completionPercentage*100;
    }
}
