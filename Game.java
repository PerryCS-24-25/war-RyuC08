/**
 * 
 *
 * @author 
 * @version 
 */
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;

import javax.swing.event.MouseInputAdapter;


public class Game
{
    private final Canvas canvas;
    private final List<Card> cards;
    private List<Card> p1Cards;
    private List<Card> p2Cards;
    private List<Card> p1PlayedCards;
    private List<Card> p2PlayedCards;
    private List<Card> discard;
    Rect button;
    Text dealT;
    private boolean roundReady = false;
    /**
     * Create a window that will display and allow the user to play the game
     */
    public Game() {
        cards = Card.loadCards();
        p1Cards = new ArrayList<>();
        p2Cards = new ArrayList<>();
        // Prepare the canvas
        canvas = Canvas.getCanvas();
        canvas.setBackgroundColor("#35654D");
        canvas.clear();
        canvas.setTitle("MyGame");
        
        buildDisplay();
        canvas.redraw();     
        // Add a mouse handler to deal with user input
        canvas.addMouseHandler(new MouseInputAdapter() {
            public void mouseClicked(MouseEvent e) {
                onClick(e.getButton(), e.getX(), e.getY());
            }
            
            public void mouseMoved(MouseEvent e) {
                onMove(-1, e.getX(), e.getY());
            }

            public void mouseDragged(MouseEvent e) {
                onMove(e.getButton(), e.getX(), e.getY());
            }
        });
    }
    
    /**
     * Reset the Game and display to the beginning state
     */
    public void reset() {
        canvas.clear();
        
        buildDisplay();
    }
    /**
     * Setup the display for the game
     */
    private void buildDisplay() {
        int x = 300;
        int y = 200;
        for (Card card : cards) {
            card.setPosition(x, y);
            card.turnFaceDown();
            card.makeVisible();
            x++;
            y++;
        }
       startB();

    }
    /*
     * Create start button
     */
    private void startB(){
        button = new Rect(550, 275, 150, 75, "red", true);
        dealT = new Text("Deal", 573, 330, 50, "white", true);
    }
    /**
     * Handle the user clicking in the window
     * @param button the button that was pressed
     * @param x the x coordinate of the mouse position
     * @param y the y coordinate of the mouse position
     */
    private void onClick(int btn, int x, int y) {
        System.out.println("Mouse clicked at " + x + ", " + y + " with button " + btn);
        if( (button.isVisible()) &&
            (x >= button.getX()) && (x <= (button.getX() + button.getWidth())) &&
            (y >= button.getY()) && (y <= (button.getY() + button.getHeight())))
        {
            button.makeInvisible();
            dealT.makeInvisible();
            deal();
        }
        if(roundReady){
            play();
        }

    }
    /**
     * Deal all cards to the players
     */
    private void deal(){
        while(cards.size() > 0){
            int randomizer1 = (int)(cards.size()*Math.random());
            p1Cards.add(cards.get(randomizer1));
            cards.remove(randomizer1);
            int randomizer2 = (int)(cards.size()*Math.random());
            p2Cards.add(cards.get(randomizer2));
            cards.remove(randomizer2);
        }
        int x = 50;
        for(Card card : p1Cards){
            card.setPosition(x, 600 - card.getHeight() - 10);
            //x++;
        }
        x = 50;
        for(Card card : p2Cards){
            card.setPosition(x, 10);
            //x++;
        }
        canvas.redraw();
        roundReady = true;
    }
    /**
     * Play the game
     */
    private void play(){
        p1PlayedCards.add(p1Cards.get(p1Cards.size()));
        p1Cards.remove(p1Cards.get(p1Cards.size()));
        p2PlayedCards.add(p1Cards.get(p1Cards.size()));
        p2Cards.remove(p1Cards.get(p1Cards.size()));
            
        p1PlayedCards.get(0).setPosition(250, 10);
        p2PlayedCards.get(0).setPosition(250, 600 - p2PlayedCards.get(0).getHeight() - 10);
        p1PlayedCards.get(0).setFaceUp(true);
        p2PlayedCards.get(0).setFaceUp(true);

        canvas.redraw();
        
        discardPC();
        giveWC();
    }
    /**
     * In case of a tie
     */
    private void tie(){
        boolean winner = false;
        while(winner == false){
            p1PlayedCards.add(p1Cards.get(p1Cards.size()));
            p1Cards.remove(p1Cards.get(p1Cards.size()));
            p1PlayedCards.add(p1Cards.get(p1Cards.size()));
            p1Cards.remove(p1Cards.get(p1Cards.size()));
            p2PlayedCards.add(p1Cards.get(p1Cards.size()));
            p2Cards.remove(p1Cards.get(p1Cards.size()));
            p2PlayedCards.add(p1Cards.get(p1Cards.size()));
            p2Cards.remove(p1Cards.get(p1Cards.size()));
            
            p1PlayedCards.get(0).setPosition(250, 10);
            p1PlayedCards.get(1).setPosition(350, 10);
            p2PlayedCards.get(0).setPosition(250, 600 - p2PlayedCards.get(0).getHeight() - 10);
            p2PlayedCards.get(1).setPosition(350, 600 - p2PlayedCards.get(0).getHeight() - 10);
            
            discardPC();
            if(p1PlayedCards.get(0).getValue() > p2PlayedCards.get(0).getValue()){
                for(int i = 0; i < discard.size(); i++){
                    p1Cards.add(discard.get(i));
                    discard.remove(discard.get(i));
                    p1Cards.get(p1Cards.size() - 1).setPosition(250, 10);
                    p1Cards.get(p1Cards.size() - 1).setFaceUp(false);
                    canvas.redraw();
                }
            }
            else if (p1PlayedCards.get(0).getValue() < p2PlayedCards.get(0).getValue()){
                for(int i = 0; i < discard.size(); i++){
                    p2Cards.add(discard.get(i));
                    discard.remove(discard.get(i));
                    p2Cards.get(p2Cards.size() - 1).setPosition(250, 600 - p2PlayedCards.get(0).getHeight() - 10);
                    p2Cards.get(p2Cards.size() - 1).setFaceUp(false);
                    canvas.redraw();
                }
            }
    }
    /**
     * Give winners all played cards
     */
    private void giveWC(){
        if(p1PlayedCards.get(0).getValue() > p2PlayedCards.get(0).getValue()){
            for(int i = 0; i < discard.size(); i++){
                p1Cards.add(discard.get(i));
                discard.remove(discard.get(i));
                p1Cards.get(p1Cards.size() - 1).setPosition(250, 10);
                p1Cards.get(p1Cards.size() - 1).setFaceUp(false);
                canvas.redraw();
            }
        }
        else if (p1PlayedCards.get(0).getValue() < p2PlayedCards.get(0).getValue()){
            for(int i = 0; i < discard.size(); i++){
                p2Cards.add(discard.get(i));
                discard.remove(discard.get(i));
                p2Cards.get(p2Cards.size() - 1).setPosition(250, 600 - p2PlayedCards.get(0).getHeight() - 10);
                p2Cards.get(p2Cards.size() - 1).setFaceUp(false);
                canvas.redraw();
            }
        }
        else{
            tie();
        }
    }
    /**
     * Place unneeded cards in discard list
     */
    private void discardPC(){
        for(int i = 0; i < p1PlayedCards.size(); i++){
            discard.add(p1PlayedCards.get(0));
            p1PlayedCards.remove(0);
        }
        for(int i = 0; i < p2PlayedCards.size(); i++){
            discard.add(p2PlayedCards.get(0));
            p2PlayedCards.remove(0);
        }
    }
    /**
     * Handle the user moving the mouse in the window
     * @param button the button that was pressed, or -1 if no button was pressed
     * @param x the x coordinate of the mouse position
     * @param y the y coordinate of the mouse position
     */
    private void onMove(int button, int x, int y) {
        // if (button == -1) {
        //     System.out.println("Mouse moved to " + x + ", " + y);
        // }
        // else {
        //     System.out.println("Mouse dragged to " + x + ", " + y + " with button " + button);
        // }
    }
    
    /**
     * Quickly flash the window background red
     */
    public void flashRed() {
        // The user messed up, show them they made a mistake
        canvas.setBackgroundColor("red");
        canvas.redraw();
        wait(500);
        
        canvas.setBackgroundColor("white");
        canvas.redraw();
    }
    
    /**
     * Wait for a specified number of milliseconds before finishing. This
     * provides an easy way to specify a small delay which can be used when
     * producing animations.
     *
     * @param milliseconds the number
     */
    public static void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            // ignoring exceptions at the moment
        }
    }

    /**
     * Run the game
     */
    public static void main(String[] args) {
        // Start the game
        new Game();
    }
}
