/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.sps.juegos;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import mx.sps.mbeans.Control;

/**
 *
 * @author RuGI (S&P Solutions)
 */
public class Adivina {

    private List<String> words;
    private StringBuffer endControl;

    public Adivina(List<String> words, String endWord) {
        super();
        this.words = words;
        this.endControl = new StringBuffer(endWord);
    }

    public void addWord(String word) {
        this.words.add(word);
    }

    public int getNumberWords() {
        return this.words.size();
    }

    public List<String> getWords() {
        return this.words;
    }

    public static void main(String[] args) throws Exception {
        Adivina adivina = new Adivina(new ArrayList<String>(), "END");
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("mx.com.spsolutions.jmxtutorial:type=Control");
        Control mbean = new Control(adivina.getWords());
        mbs.registerMBean(mbean, name);
        Scanner keyboard = new Scanner(System.in);
        String input;
        do {
            input = keyboard.nextLine();
            System.out.println("Escribiste:" + input);
            adivina.addWord(input);
        } while (!input.equals(adivina.endControl.toString()));
        System.out.println("Adivinaste en [" + adivina.getNumberWords() + "] intentos.");
    }//main

}//class
