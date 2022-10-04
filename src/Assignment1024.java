import java.util.Random;
import java.util.Scanner;

public class Assignment1024 {
    public static void main(String[] args) {
        int[] score = {0};

        //Printing the Rules of Game
        intro();

        //Taking and Controlling Dimension
        int dimension;
        dimension = takeDimensionInput();

        //Creating the Grid
        int[][] grid = new int[dimension][dimension];

        //This Loop Continues Until There is No Empty or Mergeable Tiles
        do{
            //Creating Empty Cell List and Filling With the Empty Cell Indexes
            int[][] emptyCells = new int[countEmptyCells(grid)][2];
            makeEmptyCellList(grid, emptyCells);

            //Adding Random '1' Value Tile(s)
            if (emptyCells.length == (dimension * dimension)) {
                randomTile(grid, emptyCells);
            }
            randomTile(grid, emptyCells);

            //Controlling 'Game Over' Cases
            if (isGameOver(grid, countEmptyCells(grid))) break;

            //Taking and Controlling the Move
            char move;
            do {
                do {
                    printCurrentState(grid, score);
                    move = takeMove();
                }while (!controlMove(move));
            }while (!calculateMove(move, grid, score));

        }while(true);

        //Printing 'Game Over' and Latest State of Grid
        outcome(grid,score);

    }


    //---METHODS: The names is explanatory but I commented for brief info---
    //Printing the Rules of Game
    public static void intro(){
        System.out.println("----------Welcome to 1024 Game----------");
        System.out.println("This game played in a square grid, we will be asking for dimensions of square later.");
        System.out.println("The goal is to reach 1024 by adding same valued cells by moving and merging them together.");
        System.out.println("You will be moving all values to the direction you choose, be careful.");
        instructions();
        System.out.println("Let's Play...\n");
    }



    //Instructions for Playing the Game
    public static void instructions(){
        System.out.println("The game is played by 1,2,3,4 or W,A,S,D keys.");
        System.out.println("1.Up    -- W");
        System.out.println("2.Left  -- A");
        System.out.println("3.Down  -- S");
        System.out.println("4.Right -- D");
        System.out.println("Example. You can either enter 1 for Up or W... ");
    }



    //Taking Dimension Input and Invoke the Method Responsible for Control
    public static int takeDimensionInput(){
        Scanner input = new Scanner(System.in);
        int number;

        do {
            System.out.println("Please enter your desired dimension: (The number should between 4-10)");
            number = input.nextInt();
        }while (!controlDimension(number));

        return number;

    }



    //Controlling the 'Dimension' Input from Previous Method
    public static boolean controlDimension(int input){
        if (input < 4 || input > 10){
            //Printing Error
            System.out.println("You entered wrong input.");
            return false;
        }
        else return true;
    }



    //Counting Empty Cells
    public static int countEmptyCells(int[][] grid){
        int count = 0;
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[i].length; j++){
                if (grid[i][j] == 0) count++;
            }
        }
        return count;
    }



    //Taking Indexes of Empty Cells Into a List
    public static void makeEmptyCellList(int[][] array, int[][] emptyCells){
        int counter=0;
        for (int i = 0; i < array.length; i++){
            for (int j = 0; j < array.length; j++){
                if (array[i][j] == 0){
                    //Store for Row Indexes
                    emptyCells[counter][0] = i;
                    //Store for Column Indexes
                    emptyCells[counter][1] = j;
                    counter++;
                }
            }
        }
    }



    //Adding '1' Value Tile to a Random Place of Grid via Taking the Indexes from List
    public static void randomTile(int[][] grid, int[][] emptyCellList){
        Random rand = new Random();
        int temp = rand.nextInt(emptyCellList.length);
        grid[emptyCellList[temp][0]][emptyCellList[temp][1]] = 1;
    }



    //PRINT SET
    //Printing the Upper and Lower Lines
    public static void printTopDownLines(int range){
        System.out.print("\n-");
        for (int i = 0; i < range; i++) System.out.print("-------");
        System.out.println();
    }

    //Printing Lines of Grid With Assistance of Previous Print Method
    public static void printLines(int number, int range){
        if (number != (range - 1)){
            System.out.print("\n-");
            for (int j = 0; j < range; j++){
                if (j != (range - 1)) System.out.print("------+");
                else System.out.print("-------");
            }
            System.out.println();
        } else printTopDownLines(range);
    }

    //Printing Filled Grid with Assistance of Previous Print Methods
    public static void printCurrentState(int[][] grid, int[] score){
        //Printing Score on top of the grid
        System.out.print("\n-----[SCORE = " + score[0] + "]-----");
        //Printing Starting lines
        printTopDownLines(grid.length);
        for (int i = 0; i < grid.length; i++){
            System.out.print("|");
            for (int j = 0; j < grid.length; j++){
                //Printing tile values
                if (grid[i][j] == 0 ) System.out.print("      |");
                else if (grid[i][j] < 10 ) System.out.printf("    %d |", grid[i][j]);
                else if (grid[i][j] < 100) System.out.printf("   %d |", grid[i][j]);
                else if (grid[i][j] < 1000) System.out.printf("  %d |", grid[i][j]);
                else System.out.printf(" %d |", grid[i][j]);
            }
            //Printing in-between lines & ending line
            printLines(i,grid.length);
        }
    }



    //Taking Move and Making Them Uppercase then Return for Control
    public static char takeMove(){
        Scanner input = new Scanner(System.in);
        char cha;
        System.out.println("Now, enter your move direction.");
        cha = input.next().charAt(0);
        cha = Character.toUpperCase(cha);
        return cha;
    }



    //Controlling Upper Cased Move Input from Previous Method
    public static boolean controlMove(char cha){
        if (cha == 'W' || cha == 'A' || cha == 'S' || cha == 'D'  || cha == '1' || cha == '2' || cha == '3' || cha == '4') return true;
        else {
            //Printing Error and Printing Instructions Again
            System.out.print("===YOU ENTERED THE WRONG INPUT===\n");
            System.out.print("Let me show you one more time, Here the instructions for input\n");
            instructions();
            return false;
        }
    }


    //Calculating the Results of Move
    public static boolean calculateMove(char move, int[][] grid, int[] score){
        int[][] controlArr = new int[grid.length][grid.length];
        boolean isValidTurn = false;
        int scoreTemp;

        //Cloning Array Before Move So We Can Compare for Valid Turn
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid.length; j++){
               controlArr[i][j] = grid[i][j];
            }
        }

        //Applying the Move
        if (move == 'W' || move == '1') scoreTemp =  moveUp(grid);
        else if (move == 'A' || move == '2') scoreTemp =  moveLeft(grid);
        else if (move == 'S' || move == '3') scoreTemp =  moveDown(grid);
        else scoreTemp =  moveRight(grid);


        //Controlling for Valid Turn
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid.length; j++){
                if (grid[i][j] != controlArr[i][j]) isValidTurn = true;
            }
        }

        //Looking for '1024' Valued Tile for First Time
        if (isWin(grid) && !isWin(controlArr)){
            System.out.print("\n---*** CONGRATS! YOU WIN ***---");
            System.out.println("\nBut the game continues, let's show them what you are capable of!!!");
        }

        //Ending Process
        if (!isValidTurn){
            if (isGameOver(grid,countEmptyCells(grid))) return true;

            System.out.printf("\n***No changes has occured in the grid, please try a different move***");
            return isValidTurn;
        }else {
             score[0] = score[0] + scoreTemp;
             return isValidTurn;
        }
    }

    //MOVE SET: Merging and Sliding Processes
    public static int moveUp(int[][] grid){
        int score = 0;
        //Calculating(Merging) Mergeable Tiles
        for (int i = 0; i < grid.length; i++ ){
            for (int j = 0; j < grid.length; j++){
                int counter = 1;
                while ((j+counter) < grid.length) {
                    if (grid[j][i] != 0 && grid[j + counter][i] != 0){
                        if (grid[j][i] == grid[j + counter][i]){
                            grid[j][i] = grid[j][i] * 2;
                            score = score + grid[j][i];
                            grid[j + counter][i] = 0;
                            break;
                        }else break;
                    }else if (grid[j][i] == 0) break;
                    else counter++;
                }
            }
        }

        //Sliding Tiles
        for (int i = 0; i < grid.length; i++){
            int count = 0;
            for (int j = 0; j < grid.length; j++){
                int temp;
                if (grid[j][i] != 0){
                    temp = grid[j][i];
                    grid[j][i] = 0;
                    grid[count][i] = temp;
                    count++;
                }
            }
        }

        return score;
    }

    public static int moveDown(int[][] grid){
        int score = 0;
        //Calculating(Merging) Mergeable Tiles
        for (int i = 0; i < grid.length; i++ ){
            for (int j = (grid.length - 1); j > 0; j--){
                int counter = 1;
                while ((j-counter) >= 0){
                    if (grid[j][i] != 0 && grid[j - counter] [i]!= 0){
                        if (grid[j][i] == grid[j - counter][i]){
                            grid[j][i] = grid[j][i] * 2;
                            score = score + grid[j][i];
                            grid[j - counter][i] = 0;
                            break;
                        }else break;
                    }else if(grid[j][i] == 0) break;
                    else counter++;
                }
            }
        }

        //Sliding Tiles
        for (int i = 0; i < grid.length; i++) {
            int count = 0;
            for (int j = (grid.length - 1); j >= 0; j--) {
                int temp;
                if (grid[j][i] != 0) {
                    temp = grid[j][i];
                    grid[j][i] = 0;
                    grid[grid.length - count - 1][i] = temp;
                    count++;
                }
            }
        }
        return score;
    }

    public static int moveLeft(int[][] grid){
        int score = 0;
        //Calculating(Merging) Mergeable Tiles
        for (int i = 0; i < grid.length; i++ ){
            for (int j = 0; j < grid.length; j++){
                int counter = 1;
                while ((j+counter) < grid.length){
                    if (grid[i][j] != 0 && grid[i][j + counter] != 0){
                        if (grid[i][j] == grid[i][j + counter]){
                            grid[i][j] = grid[i][j] * 2;
                            score = score + grid[i][j];
                            grid[i][j + counter] = 0;
                            break;
                        }else break;
                    } else if (grid[i][j] == 0) break;
                    else counter++;
                }
            }
        }


        //Sliding Tiles
        for (int i = 0; i < grid.length; i++){
            int count = 0;
            for (int j = 0; j < grid.length; j++){
                int temp;
                if (grid[i][j] != 0){
                    temp = grid[i][j];
                    grid[i][j] = 0;
                    grid[i][count] = temp;
                    count++;
                }
            }
        }
        return score;
    }

    public static int moveRight(int[][] grid){
        int score = 0;
        //Calculating(Merging) Mergeable Tiles
        for (int i = 0; i < grid.length; i++ ){
            for (int j = (grid.length - 1); j > 0; j--){
                int counter = 1;
                while ((j-counter) >= 0){
                    if (grid[i][j] != 0 && grid[i][j - counter] != 0){
                        if (grid[i][j] == grid[i][j - counter]){
                            grid[i][j] = grid[i][j] * 2;
                            score = score + grid[i][j];
                            grid[i][j - counter] = 0;
                            break;
                        }else break;
                    }else if(grid[i][j] == 0) break;
                    else counter++;
                }
            }
        }

        //Sliding Tiles
        for (int i = 0; i < grid.length; i++){
            int count = 0;
            for (int j = (grid.length - 1); j >= 0; j--){
                int temp;
                if (grid[i][j] != 0){
                    temp = grid[i][j];
                    grid[i][j] = 0;
                    grid[i][grid.length - count - 1] = temp;
                    count++;
                }
            }
        }
        return score;
    }



    //Controlling for '1024' Valued Tile
    public static boolean isWin(int[][] grid){
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid.length; j++){
                if (grid[i][j] == 1024) return true;
            }
        }
        return false;
    }


    //Controlling for Mergeable and Empty Tiles
    public static boolean isGameOver(int[][] grid, int emptyTiles){
        boolean isOver = true;
        //Control of Mergeable Tiles
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[i].length; j++){
                if (i != (grid.length - 1) && j != (grid[i].length - 1)){
                    //Control of Non-Terminal Row and Column
                    if (grid[i][j] == grid[i+1][j] || grid[i][j] == grid[i][j+1]){
                        isOver = false;
                        break;
                    }
                }else if (i == (grid.length - 1) && j != (grid.length -1)){
                    //Control of Last Row
                    if (grid[i][j] == grid[i][j+1]){
                        isOver = false;
                        break;
                    }
                }else if (j == (grid.length - 1) && i != (grid.length -1)){
                    //Control of Last Column
                    if (grid[i][j] == grid[i+1][j]){
                        isOver = false;
                        break;
                    }
                }
            }
            //Exit the Loop When Find Mergeable Tiles
            if (!isOver) break;
        }

        if (isOver && emptyTiles != 0) isOver = false;
        return isOver;
    }



    //Printing 'Game Over' and Latest State of Grid
    public static void outcome(int[][] grid, int[] score){
        System.out.print("\n======= GAME IS OVER ========");
        printCurrentState(grid,score);
    }

}