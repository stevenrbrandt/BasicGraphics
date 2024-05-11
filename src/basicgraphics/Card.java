/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package basicgraphics;

/**
 *
 * @author steve
 */
public class Card extends BasicContainer {    
    
    final String cardName;
    public Card(String name) {
        cardName = name;
    }

    public void showCard() {
        BasicFrame.cards.show(getParent(), cardName);
    }
}
