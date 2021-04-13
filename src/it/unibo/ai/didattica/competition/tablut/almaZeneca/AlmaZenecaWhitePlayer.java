package it.unibo.ai.didattica.competition.tablut.almaZeneca;

import java.io.IOException;
import java.net.UnknownHostException;

public class AlmaZenecaWhitePlayer{

    public static void main(String[] args) throws UnknownHostException, ClassNotFoundException, IOException {
        String[] array = new String[]{"WHITE", "60", "localhost", "debug"};
        if (args.length>0){
            array = new String[]{"WHITE", args[0]};
        }
        AlmaZenecaPlayer.main(array);
    }
}