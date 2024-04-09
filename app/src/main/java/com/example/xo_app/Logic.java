package com.example.xo_app;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Logic {
    private int turn = 0; // Contorizează rândul curent al jocului
    public static String firstMark = "X"; // Simbolul primului jucător
    public static String secondMark = "O"; // Simbolul celui de-al doilea jucător
    public final int SIZE = 3; // Dimensiunea matricei de joc (3x3)
    private String[][] matrix = new String[SIZE][SIZE]; // Matricea care reprezintă starea jocului

    public Logic() {
    }

   // Verifică dacă matricea jocului este completată (toate celulele sunt ocupate)
    public boolean isFilled() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (matrix[i][j] != firstMark && matrix[i][j] != secondMark) return false;
            }
        }
        return true;
    }

    // Verifică dacă un jucător specificat a câștigat jocul
    public boolean checkWin(String XorO) {
        String expect = null;
        for (int index = 0; index < SIZE; index++) {
            expect += XorO;
        }
        String result = null;

        // Verifică liniile și coloanele pentru o victorie
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (matrix[i][j] != null) {
                    result += matrix[i][j];
                }
            }
            if (expect.equals(result)) {
                return true;
            } else result = null;
        }

        // Verifică coloanele pentru o victorie
        for (int j = 0; j < SIZE; j++) {
            for (int i = 0; i < SIZE; i++) {
                if (matrix[i][j] != null) {
                    result += matrix[i][j];
                }
            }
            if (expect.equals(result)) {
                return true;
            } else result = null;
        }

        // Verifică diagonala principală pentru o victorie
        for (int j = 0, i = 0; j < SIZE; j++, i++) {
            if (matrix[i][j] != null) {
                result += matrix[i][j];
            }
        }
        if (expect.equals(result)) {
            return true;
        } else result = null;

        // Verifică diagonala secundară pentru o victorie
        for (int i = SIZE - 1, j = 0; j < SIZE; j++, i--) {
            if (matrix[i][j] != null) {
                result += matrix[i][j];
            }
        }
        return expect.equals(result);
    }

    // Click-ul pe un buton din interfața grafică
    public void clickOnButton(String buttonTag) {
        if (matrix[Character.digit(buttonTag.charAt(0), 10)][Character.digit(buttonTag.charAt(1), 10)] == null
                && !checkWin(firstMark)
                && !checkWin(secondMark)) {
            matrix[Character.digit(buttonTag.charAt(0), 10)][Character.digit(buttonTag.charAt(1), 10)] = getValue();
            setTurn(getTurn() + 1);
        }
    }

    // Implementarea logicii pentru AI
    public int clickOnButtonWithAI() {
        List<Integer> temp = new ArrayList<>();
        // Parcurgem fiecare celulă din matrice
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                // Verificăm dacă celula este liberă
                if (!(Objects.equals(matrix[i][j], firstMark)) && !(Objects.equals(matrix[i][j], secondMark))) {
                    // Dacă este liberă, temporar o marcăm cu simbolul AI-ului și verificăm dacă acesta poate câștiga
                    matrix[i][j] = secondMark;
                    if (checkWin(secondMark)) {
                        // Dacă AI-ul poate câștiga dacă ar alege această celulă, o returnăm
                        return i * SIZE + j;
                    }
                    // Dacă nu poate câștiga, eliberăm celula și o adăugăm la lista temporară de posibile mișcări
                    matrix[i][j] = null;
                    temp.add(i * SIZE + j);
                }
            }
        }
        // Parcurgem din nou fiecare celulă
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                // Verificăm dacă celula este liberă
                if (!(Objects.equals(matrix[i][j], firstMark)) && !(Objects.equals(matrix[i][j], secondMark))) {
                    // Dacă este liberă, temporar o marcăm cu simbolul jucătorului și verificăm dacă acesta poate câștiga
                    matrix[i][j] = firstMark;
                    if (checkWin(firstMark)) {
                        // Dacă jucătorul poate câștiga dacă ar alege această celulă, o marcăm cu simbolul AI-ului și o returnăm
                        matrix[i][j] = secondMark;
                        return i * SIZE + j;
                    }
                    // Dacă nu poate câștiga, eliberăm celula
                    matrix[i][j] = null;
                }
            }
        }
        // Dacă niciun jucător nu poate câștiga într-un singur pas, alegem o mișcare aleatoare din lista temporară
        int random = temp.get(new Random().nextInt(temp.size()));
        // Marcăm acea celulă cu simbolul AI-ului și o returnăm
        matrix[random / 3][random % 3] = secondMark;
        return random;
    }

    // Schimbă părțile/jucătorii în funcție de switch
    public void changeSide() {
        if (turn == 0) {
            firstMark = firstMark.equals("X") ? "O" : "X";
            secondMark = secondMark.equals("O") ? "X" : "O";
        }
    }

    // Schimbă părțile/jucătorii cu simbolul specificat
    public void changeSide(String side) {
        firstMark = side;
        secondMark = firstMark.equals("O") ? "X" : "O";
    }

    // Returnează matricea jocului
    public String[][] getMatrix() {
        return matrix;
    }

    // Resetează matricea jocului
    public void clearMatrix() {
        matrix = new String[SIZE][SIZE];
        setTurn(0);
    }

    // Returnează simbolul jucătorului în funcție de rândul curent
    public String getValue() {
        return turn % 2 == 0 ? firstMark : secondMark;
    }

    // Returnează rândul curent al jocului
    public int getTurn() {
        return this.turn;
    }

    // Setează rândul curent al jocului
    public void setTurn(int turn) {
        this.turn = turn;
    }
}
