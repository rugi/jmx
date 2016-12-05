/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.sps.mbeans;

import java.util.List;

/**
 *
 * @author RuGI (S&P Solutions)
 */
public class Control implements ControlMBean {

    private List<String> words;

    public Control(List<String> words) {
        super();
        this.words = words;
    }

    @Override
    public String lastMessage() {
        return (this.words.size()>0)?this.words.get(this.words.size()-1):null;
    }

    @Override
    public int attempts() {
       return this.words.size();
    }

    @Override
    public void clear() {
        System.out.println("Que suerte!. Se ha reiniciado su contador de intentos. ");
        this.words.clear();
    }

}
