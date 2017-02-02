/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TM.controller;
import tm.view.TMView;


public class TMController {
    private TMView tmView;
    
    public TMController(){
        tmView = new TMView(this);
        
    }
    
    public void disposeTMView(){
        tmView.dispose();
    }
    
}
