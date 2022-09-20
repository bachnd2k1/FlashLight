package com.flashlight.demo.morse;

import java.util.ArrayList;
import java.util.List;

public class TextToMorseCode {
    private String text;

    /**
     * @param text set text need convert to morse code
     */
    public TextToMorseCode(String text) {
        this.text = text;
    }

    /**
     * @param character character need to convert to morse code
     *                  {@link com.flashlight.demo.morse.MorseCode} to show all morse code convert
     * @return morse code of character
     */
    private String charactersToMorseCode(String character) {
        switch (character) {
            case "A":
            case "a":
                return MorseCode.MORSE_CODE.MORSE_CODE_A;

            case "B":
            case "b":
                return MorseCode.MORSE_CODE.MORSE_CODE_B;

            case "C":
            case "c":
                return MorseCode.MORSE_CODE.MORSE_CODE_C;

            case "D":
            case "d":
                return MorseCode.MORSE_CODE.MORSE_CODE_D;

            case "E":
            case "e":
                return MorseCode.MORSE_CODE.MORSE_CODE_E;

            case "F":
            case "f":
                return MorseCode.MORSE_CODE.MORSE_CODE_F;

            case "G":
            case "g":
                return MorseCode.MORSE_CODE.MORSE_CODE_G;

            case "H":
            case "h":
                return MorseCode.MORSE_CODE.MORSE_CODE_H;

            case "I":
            case "i":
                return MorseCode.MORSE_CODE.MORSE_CODE_I;

            case "J":
            case "j":
                return MorseCode.MORSE_CODE.MORSE_CODE_J;

            case "K":
            case "k":
                return MorseCode.MORSE_CODE.MORSE_CODE_K;

            case "L":
            case "l":
                return MorseCode.MORSE_CODE.MORSE_CODE_L;

            case "M":
            case "m":
                return MorseCode.MORSE_CODE.MORSE_CODE_M;

            case "N":
            case "n":
                return MorseCode.MORSE_CODE.MORSE_CODE_N;

            case "O":
            case "o":
                return MorseCode.MORSE_CODE.MORSE_CODE_O;

            case "P":
            case "p":
                return MorseCode.MORSE_CODE.MORSE_CODE_P;

            case "Q":
            case "q":
                return MorseCode.MORSE_CODE.MORSE_CODE_Q;

            case "R":
            case "r":
                return MorseCode.MORSE_CODE.MORSE_CODE_R;

            case "S":
            case "s":
                return MorseCode.MORSE_CODE.MORSE_CODE_S;

            case "T":
            case "t":
                return MorseCode.MORSE_CODE.MORSE_CODE_T;

            case "U":
            case "u":
                return MorseCode.MORSE_CODE.MORSE_CODE_U;

            case "V":
            case "v":
                return MorseCode.MORSE_CODE.MORSE_CODE_V;

            case "W":
            case "w":
                return MorseCode.MORSE_CODE.MORSE_CODE_W;

            case "X":
            case "x":
                return MorseCode.MORSE_CODE.MORSE_CODE_X;

            case "Y":
            case "y":
                return MorseCode.MORSE_CODE.MORSE_CODE_Y;

            case "Z":
            case "z":
                return MorseCode.MORSE_CODE.MORSE_CODE_Z;

            case "0":
                return MorseCode.MORSE_CODE.MORSE_CODE_0;

            case "1":
                return MorseCode.MORSE_CODE.MORSE_CODE_1;

            case "2":
                return MorseCode.MORSE_CODE.MORSE_CODE_2;

            case "3":
                return MorseCode.MORSE_CODE.MORSE_CODE_3;

            case "4":
                return MorseCode.MORSE_CODE.MORSE_CODE_4;

            case "5":
                return MorseCode.MORSE_CODE.MORSE_CODE_5;

            case "6":
                return MorseCode.MORSE_CODE.MORSE_CODE_6;

            case "7":
                return MorseCode.MORSE_CODE.MORSE_CODE_7;

            case "8":
                return MorseCode.MORSE_CODE.MORSE_CODE_8;

            case "9":
                return MorseCode.MORSE_CODE.MORSE_CODE_9;


            default:
                return "";
        }
    }

    /**
     * @return morse code of {@link #text}
     */
    public List<String> convertTextToMorseCode() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < text.trim().length(); i++) {
            char[] chars = text.toCharArray();
                list.add(String.valueOf(charactersToMorseCode(String.valueOf(chars[i]))));
        }
        return list;
    }
}
