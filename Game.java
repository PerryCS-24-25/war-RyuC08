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
    private List<Card> p1Discard;
    private List<Card> p2Discard;
    Rect button;
    Text dealT;
    /**
     * Create a window that will display and allow the user to play the game
     */
    public Game() {
        cards = Card.loadCards();
        p1Cards = new ArrayList<>();
        p2Cards = new ArrayList<>();
        // Prepare the canvas
        canvas = Canvas.getCanvas();
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
