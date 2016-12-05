/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.sps.mbeans;

/**
 *
 * @author RuGI (S&P Solutions)
 */
public interface ControlMBean {

    public String lastMessage();

    public int attempts();

    public void clear();
}
