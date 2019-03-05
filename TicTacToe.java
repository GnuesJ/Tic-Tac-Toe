package com.company;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.*;
import javafx.scene.layout.VBox;
import javafx.geometry.*;
import javafx.scene.text.*;
import java.util.Random;
import java.util.ArrayList;

public class TicTacToe extends Application implements EventHandler<ActionEvent>{
    Stage displayStage;
    Button[] buttonMain = new Button[2];
    Button[] buttonGame = new Button[9];
    Button buttonP, buttonB, buttonI;
    Scene sceneMain, scenePlay;

    Integer[][] player = new Integer[2][9];
    Integer[] computer = new Integer[9];

    Boolean[] board = new Boolean[9];
    int moves;

    AlertBox alertBox;
    GridPane gridGame;
    boolean verseComputer, smartComputer;
    Text text;

    ArrayList<Integer> aiMove;
    int[] mark;

    public static void main(String[] args)
    {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception
    {
        displayStage = primaryStage;
        displayStage.setTitle("Tic Tac Toe");
        menu();

    }

    public void menu()
    {
        verseComputer = false;
        smartComputer = false;
        mark = new int[9];

        buttonP = new Button();
        buttonP.setText("Player vs Player");
        buttonP.setFont(Font.font(40));
        buttonP.setOnAction((ActionEvent)->
        {
            gameStart();
        });
        buttonB = new Button();
        buttonB.setText("Player vs Computer");
        buttonB.setFont(Font.font(40));
        buttonB.setOnAction((ActionEvent)->
        {
            verseComputer = true;
            gameStart();
        });

        buttonI = new Button();
        buttonI.setText("Player vs SMART Computer");
        buttonI.setFont(Font.font(32));
        buttonI.setOnAction((ActionEvent)->
        {
            aiMove = new ArrayList<>();
            for(int i=0; i<9 ; i++)
            {
                mark[i]=0;
            }
            verseComputer = true;
            smartComputer = true;
            gameStart();
        });
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(buttonP,buttonB,buttonI);
        vbox.setAlignment(Pos.CENTER);
        sceneMain = new Scene(vbox,500,550);
        displayStage.setScene(sceneMain);
        displayStage.show();

    }


    private void gameStart()
    {


        alertBox = new AlertBox();
        moves =0;
        displayStage.setTitle("Tic Tac Toe");


        for(int i=0; i<9; i++) {
            buttonGame[i] = new Button();
            buttonGame[i].setText("  ");
            buttonGame[i].setFont(Font.font(50));
            buttonGame[i].setPrefSize(150, 150);
            buttonGame[i].setOnAction(this);
            board[i] = false;
        }

        GridPane gridGame = new GridPane();
        for(int i=0; i<2; i++) {
            for (int j=0; j<9; j++) {

                player[i][j] = 0;
            }
        }

        int count = 0;
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<3; j++)
            {
                gridGame.setConstraints(buttonGame[count], j, i);
                ++count;
            }
        }

        for(int i=0; i<9; i++)
        {
            gridGame.getChildren().add(buttonGame[i]);
        }

        gridGame.setPrefSize(400,400);
        gridGame.setVgap(1);
        gridGame.setHgap(1);
        gridGame.setAlignment(Pos.CENTER);

        text = new Text(5,10, "Player 1 Turn");
        text.setFont(Font.font(20));
        gridGame.setConstraints(text,1,3);
        gridGame.getChildren().add(text);

        Button buttonMenu = new Button();
        buttonMenu.setText("Menu");
        buttonMenu.setOnAction((ActionEvent)->{menu();});
        gridGame.setConstraints(buttonMenu, 2,3);
        gridGame.getChildren().add(buttonMenu);

        scenePlay = new Scene(gridGame, 500, 550);
        displayStage.setScene(scenePlay);
        displayStage.show();
    }


    @Override
    public void handle(ActionEvent eventPressed)
    {
        for(int i = 0; i<9; i++)
        {
            if(eventPressed.getSource() == buttonGame[i])
            {
                if(board[i] == true) {
                    AlertBox.displayInvalid("Move Error" , "Invalid Move");
                    break;
                }
                if(verseComputer == true) {
                    buttonGame[i].setText("O");
                    player[0][i] =1;
                    board[i] = true;
                    if(checkBoard(0,i)) {
                        alertBox.display("Match Result" , "Player 1 Wins");
                        if(AlertBox.playAgain == true) menu();
                        else displayStage.close();
                        break;
                    }
                    if(moves == 8) {
                        alertBox.display("Match Result", "Tie");
                        if(AlertBox.playAgain == true) menu();
                        else displayStage.close();
                        break;
                    }
                    mark[i]=1;

                    int j =0;
                    if(smartComputer)
                    {
                        aiMove.clear();
                        if(moves == 2)
                        {
                            if((player[0][0]==1 && player[0][8]==1))

                                aiMove.add(1);
                            else if((player[0][2]==1 && player[0][6]==1))
                                aiMove.add(3);
                        }
                        aiComputeWin();
                        aiComputePlayer();
                        aiComputeOptic(i);

                        for(int k=0; k<aiMove.size(); k++)
                        {
                            j = aiMove.get(k);
                            if(board[j] == false) break;
                        }
                    }
                    else
                    {
                        do {
                            Random rand = new Random();
                            j = rand.nextInt(9);
                        } while (board[j] == true);
                    }
                    board[j] = true;
                    mark[j] = -1;
                    buttonGame[j].setText("X");
                    player[1][j] = 1;
                    if(checkBoard(1,j)) {
                        alertBox.display("Match Result", "Computer Wins");
                        if(AlertBox.playAgain == true) menu();
                        else displayStage.close();
                        break;
                    }

                    moves+=2;
                    break;
                }
                else if(moves%2== 0) {
                    buttonGame[i].setText("O");
                    player[0][i] = 1;
                    board[i] = true;
                    if(checkBoard(0,i)) {
                        alertBox.display("Match Result" , "Player 1 Wins");
                        if(AlertBox.playAgain == true) menu();
                        else displayStage.close();
                        break;
                    }
                    if(moves == 8) {
                        alertBox.display("Match Result", "Tie");
                        if(AlertBox.playAgain == true) menu();
                        else displayStage.close();
                        break;
                    }
                    text.setText("Player 2 Turn");
                    ++moves;
                }
                else if(moves%2==1) {
                    buttonGame[i].setText("X");
                    player[1][i] = 1;
                    board[i] = true;
                    if(checkBoard(1,i)) {
                        alertBox.display("Match Result", "Player 2 Wins");
                        if(AlertBox.playAgain == true) menu();
                        else displayStage.close();
                        break;
                    }
                    text.setText("Player 1 Turn");
                    ++moves;
                }

            }
        }
    }




    public void clearBoard()
    {

        for(int i=0; i<9; i++) {
            buttonGame[i].setText("  ");
            board[i] = false;
        }
        for(int i=0; i<2; i++) {
            for (int j=0; j<9; j++) {

                player[i][j] = 0;
            }
        }
        moves = 0;
        AlertBox.playAgain = false;
    }


    public boolean checkBoard(int t,int n){
        int num=n;
        int turn=t;
        switch(num){
            case 0:
                if(player[turn][0]+player[turn][1]+player[turn][2]==3) return true;
                if(player[turn][0]+player[turn][3]+player[turn][6]==3) return true;
                if(player[turn][0]+player[turn][4]+player[turn][8]==3) return true;
                return false;

            case 1:
                if(player[turn][1]+player[turn][0]+player[turn][2]==3) return true;
                if(player[turn][1]+player[turn][4]+player[turn][7]==3) return true;
                return false;

            case 2:
                if(player[turn][2]+player[turn][0]+player[turn][1]==3) return true;
                if(player[turn][2]+player[turn][5]+player[turn][8]==3) return true;
                if(player[turn][2]+player[turn][4]+player[turn][6]==3) return true;
                return false;

            case 3:
                if(player[turn][3]+player[turn][4]+player[turn][5]==3) return true;
                if(player[turn][3]+player[turn][0]+player[turn][6]==3) return true;
                return false;

            case 4:
                if(player[turn][4]+player[turn][3]+player[turn][5]==3) return true;
                if(player[turn][4]+player[turn][1]+player[turn][7]==3) return true;
                if(player[turn][4]+player[turn][2]+player[turn][6]==3) return true;
                if(player[turn][4]+player[turn][0]+player[turn][8]==3) return true;
                return false;

            case 5:
                if(player[turn][5]+player[turn][3]+player[turn][4]==3) return true;
                if(player[turn][5]+player[turn][2]+player[turn][8]==3) return true;
                return false;

            case 6:
                if(player[turn][6]+player[turn][7]+player[turn][8]==3) return true;
                if(player[turn][6]+player[turn][0]+player[turn][3]==3) return true;
                if(player[turn][6]+player[turn][4]+player[turn][2]==3) return true;
                return false;

            case 7:
                if(player[turn][7]+player[turn][6]+player[turn][8]==3) return true;
                if(player[turn][7]+player[turn][1]+player[turn][4]==3) return true;
                return false;

            case 8:
                if(player[turn][8]+player[turn][6]+player[turn][7]==3) return true;
                if(player[turn][8]+player[turn][2]+player[turn][5]==3) return true;
                if(player[turn][8]+player[turn][0]+player[turn][4]==3) return true;
                return false;
            default: return false;
        }
    }

    boolean aiComputeWin(){
        if(mark[0]+mark[1]+mark[2]==-2){
            aiMove.add(0);
            aiMove.add(1);
            aiMove.add(2);
            return false;
        }
        else if(mark[3]+mark[4]+mark[5]==-2){
            aiMove.add(3);
            aiMove.add(4);
            aiMove.add(5);
            return false;
        }
        else if(mark[6]+mark[7]+mark[8]==-2){
            aiMove.add(6);
            aiMove.add(7);
            aiMove.add(8);
            return false;
        }
        else if(mark[0]+mark[3]+mark[6]==-2){
            aiMove.add(0);
            aiMove.add(3);
            aiMove.add(6);
            return false;
        }
        else if(mark[1]+mark[4]+mark[7]==-2){
            aiMove.add(1);
            aiMove.add(4);
            aiMove.add(7);
            return false;
        }
        else if(mark[2]+mark[5]+mark[8]==-2){
            aiMove.add(2);
            aiMove.add(5);
            aiMove.add(8);
            return false;
        }
        else if(mark[0]+mark[4]+mark[8]==-2){
            aiMove.add(0);
            aiMove.add(4);
            aiMove.add(8);
            return false;
        }
        else if(mark[2]+mark[4]+mark[6]==-2){
            aiMove.add(2);
            aiMove.add(4);
            aiMove.add(6);
            return false;
        }
        return true;
    }

    boolean aiComputePlayer(){
        if(mark[0]+mark[1]+mark[2]==2){
            aiMove.add(0);
            aiMove.add(1);
            aiMove.add(2);
            return false;
        }
        else if(mark[3]+mark[4]+mark[5]==2){
            aiMove.add(3);
            aiMove.add(4);
            aiMove.add(5);
            return false;
        }
        else if(mark[6]+mark[7]+mark[8]==2){
            aiMove.add(6);
            aiMove.add(7);
            aiMove.add(8);
            return false;
        }
        else if(mark[0]+mark[3]+mark[6]==2){
            aiMove.add(0);
            aiMove.add(3);
            aiMove.add(6);
            return false;
        }
        else if(mark[1]+mark[4]+mark[7]==2){
            aiMove.add(1);
            aiMove.add(4);
            aiMove.add(7);
            return false;
        }
        else if(mark[2]+mark[5]+mark[8]==2){
            aiMove.add(2);
            aiMove.add(5);
            aiMove.add(8);
            return false;
        }
        else if(mark[0]+mark[4]+mark[8]==2){
            aiMove.add(0);
            aiMove.add(4);
            aiMove.add(8);
            return false;
        }
        else if(mark[2]+mark[4]+mark[6]==2){
            aiMove.add(2);
            aiMove.add(4);
            aiMove.add(6);
            return false;
        }
        else if(mark[1]+mark[3]==2 && mark[0]==0 && mark[2]==0 && mark[6]==0){
            aiMove.add(0);
            return false;
        }
        else if(mark[1]+mark[5]==2 && mark[2]==0 && mark[0]==0 && mark[8]==0){
            aiMove.add(2);
            return false;
        }
        else if(mark[3]+mark[7]==2 && mark[6]==0 && mark[0]==0 && mark[8]==0){
            aiMove.add(6);
            return false;
        }
        else if(mark[7]+mark[5]==2 && mark[8]==0 && mark[6]==0 && mark[2]==0){
            aiMove.add(8);
            return false;
        }
        return true;
    }

    void aiComputeOptic(int turn){
        ArrayList<Integer> opticMoves = new ArrayList<>();
        Random rand = new Random();
        int j;
        int n = rand.nextInt(2);
        if(turn==2 && mark[4]==0){
            if(mark[0]==1 || mark[2]==1 || mark[6]==1 || mark[8]==1)
                aiMove.add(4);
        }
        else if((n==0 || n==1) && mark[4]==0){
            if(mark[4]==0){
                aiMove.add(4);
            }
        }
        else if(turn==4){
            if(mark[0]==1 && mark[5]==1 && mark[2]==0) aiMove.add(2);
            else if(mark[0]==1 && mark[7]==1 && mark[6]==0) aiMove.add(6);
            else if(mark[2]==1 && mark[3]==1 && mark[0]==0) aiMove.add(0);
            else if(mark[2]==1 && mark[7]==1 && mark[8]==0) aiMove.add(8);
            else if(mark[6]==1 && mark[1]==1 && mark[0]==0) aiMove.add(0);
            else if(mark[6]==1 && mark[5]==1 && mark[8]==0) aiMove.add(8);
            else if(mark[8]==1 && mark[1]==1 && mark[2]==0) aiMove.add(2);
            else if(mark[8]==1 && mark[3]==1 && mark[6]==0) aiMove.add(6);
            else if(mark[0]==0 || mark[2]==0 || mark[6]==0 || mark[8]==0){
                if(mark[0]==0) opticMoves.add(0);
                if(mark[2]==0) opticMoves.add(2);
                if(mark[6]==0) opticMoves.add(6);
                if(mark[8]==0) opticMoves.add(8);
                j=rand.nextInt(opticMoves.size());
                aiMove.add(opticMoves.get(j));
            }
            else {
                if(mark[1]==0) opticMoves.add(1);
                if(mark[3]==0) opticMoves.add(3);
                if(mark[5]==0) opticMoves.add(5);
                if(mark[7]==0) opticMoves.add(7);
                j=rand.nextInt(opticMoves.size());
                aiMove.add(opticMoves.get(j));
            }
        }
        else if(mark[0]==0 || mark[2]==0 || mark[6]==0 || mark[8]==0){
            if(mark[0]==0) opticMoves.add(0);
            if(mark[2]==0) opticMoves.add(2);
            if(mark[6]==0) opticMoves.add(6);
            if(mark[8]==0) opticMoves.add(8);
            j=rand.nextInt(opticMoves.size());
            aiMove.add(opticMoves.get(j));
        }
        else {
            if(mark[1]==0) opticMoves.add(1);
            if(mark[3]==0) opticMoves.add(3);
            if(mark[5]==0) opticMoves.add(5);
            if(mark[7]==0) opticMoves.add(7);
            j=rand.nextInt(opticMoves.size());
            aiMove.add(opticMoves.get(j));
        }
    }

}

class AlertBox{
    static boolean playAgain;
    public AlertBox() {
        playAgain =false;
    }
    public static void displayInvalid(String title, String message)
    {
        Button closeButton;
        Stage window;
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);


        closeButton = new Button("Close");
        closeButton.setOnAction(new EventHandler<ActionEvent>()
                                {
                                    public void handle(ActionEvent event)
                                    {
                                        window.close();
                                    }
                                }
        );

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER); //set to center

        Scene scene = new Scene(layout, 270, 100);
        window.setScene(scene);
        window.showAndWait();
    }


    public void display(String title, String message)
    {
        Button replay;
        Button closeButton;
        Stage window;
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);

        replay = new Button("Play Again?");
        replay.setOnAction(new EventHandler<ActionEvent>()
                           {
                               public void handle(ActionEvent event)
                               {
                                   playAgain = true;
                                   window.close();
                               }
                           }

        );

        closeButton = new Button("End Game");
        closeButton.setOnAction(new EventHandler<ActionEvent>()
                                {
                                    public void handle(ActionEvent event)
                                    {
                                        window.close();
                                    }
                                }
        );

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, replay,closeButton);
        layout.setAlignment(Pos.CENTER); //set to center

        Scene scene = new Scene(layout,270,100);
        window.setScene(scene);
        window.showAndWait();
    }

}
