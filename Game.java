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
    Rect pButton;
    Text playT;
    Text cardsT;
    Text p1Counter;
    Text p2Counter;
    Text p1RWinnerText;// = new Text("You Win The Round", 350, 250, 100, "white", false);
    Text p2RWinnerText;// = new Text("You Lose The Round", 350, 250, 100, "white", false);
    Text tieRText;// = new Text("War!", 350, 250, 100, "orange", false);
    Text winnerText;
    private boolean roundReady = false;
    private boolean tieRound= false;
    private boolean roundEnd = false;
    private boolean prepRound = false;
    /**
     * Create a window that will display and allow the user to play the game
     */
    public Game() {
        cards = Card.loadCards();
        p1Cards = new ArrayList<>();
        p2Cards = new ArrayList<>();
        p1PlayedCards = new ArrayList<>();
        p2PlayedCards = new ArrayList<>();
        discard = new ArrayList<>();
        // Prepare the canvas
        canvas = Canvas.getCanvas();
        canvas.setBackgroundColor("#35654D");
        canvas.clear();
        canvas.setTitle("MyGame");

        p1RWinnerText = new Text("You Won The Round", 150, 250, 50, "white", false);
        p2RWinnerText = new Text("You Lost The Round", 150, 250, 50, "white", false);
        tieRText = new Text("War!", 350, 250, 100, "orange", false);
    
        
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
    /*
     * Create play cards button
     */
    private void playB(){
        pButton = new Rect(640, 440, 150, 150, "green", true);
        playT = new Text("Play", 673, 500, 45, "white", true);
        cardsT = new Text("Cards", 657, 550, 45, "white", true);
    }
    /*
     * Create card counters
     */
    private void counter(){
        p1Counter = new Text("" + p1Cards.size(), 5, (600 - p1Cards.get(0).getHeight() - 10) + 18 + (p2Cards.get(0).getHeight()/2), 35, "white", true);
        p2Counter = new Text("" + p2Cards.size(), 5, 28 + (p2Cards.get(0).getHeight()/2), 35, "white", true);
    }
    private void updateCounter(){
        p1Counter.setText("" + p1Cards.size());
        p2Counter.setText("" + p2Cards.size());
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
            playB();
            counter();
        }
        else if(tieRound){
            tie();
            }
        else if(roundEnd){
            giveWC(); 
        }
        else if(roundReady){
            if((pButton.isVisible()) &&
            (x >= pButton.getX()) && (x <= (pButton.getX() + pButton.getWidth())) &&
            (y >= pButton.getY()) && (y <= (pButton.getY() + pButton.getHeight()))){
                 play();
            }
        }
        else if(prepRound){
            pButton.makeVisible();
            playT.makeVisible();
            cardsT.makeVisible();
            p1RWinnerText.makeInvisible();
            p2RWinnerText.makeInvisible();
            tieRText.makeInvisible();
            prepRound = false;
            roundReady = true;
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
        giveCard();
        
        roundReady = true;
    }
    /**
     * Play the game
     */
    private void play(){
        pButton.makeInvisible();
        playT.makeInvisible();
        cardsT.makeInvisible();

        p1PlayedCards.add(p1Cards.get(p1Cards.size() - 1));
        p1Cards.remove(p1Cards.get(p1Cards.size() - 1));
        p2PlayedCards.add(p2Cards.get(p2Cards.size() - 1));
        p2Cards.remove(p2Cards.get(p2Cards.size() - 1));
          
        p1PlayedCards.get(0).setPosition(250, 600 - p1PlayedCards.get(0).getHeight() - 10);
        p2PlayedCards.get(0).setPosition(250, 10);
        p1PlayedCards.get(0).turnFaceUp();
        p2PlayedCards.get(0).turnFaceUp();

        updateCounter();
        canvas.redraw();
        
        roundReady = false;
        roundEnd = true;
    }
    /**
     * In case of a tie
     */
    private void tie(){
            p1PlayedCards.add(p1Cards.get(p1Cards.size() - 1));
            p1Cards.remove(p1Cards.get(p1Cards.size() - 1));
            p1PlayedCards.add(p1Cards.get(p1Cards.size() - 1));
            p1Cards.remove(p1Cards.get(p1Cards.size() - 1));
            p2PlayedCards.add(p1Cards.get(p1Cards.size() - 1));
            p2Cards.remove(p1Cards.get(p1Cards.size() - 1));
            p2PlayedCards.add(p1Cards.get(p1Cards.size() - 1));
            p2Cards.remove(p1Cards.get(p1Cards.size() - 1));
            
            p1PlayedCards.get(0).setPosition(250, 600 - p2PlayedCards.get(0).getHeight() - 10);
            p1PlayedCards.get(1).setPosition(350, 600 - p2PlayedCards.get(0).getHeight() - 10);
            p2PlayedCards.get(0).setPosition(250, 10);
            p2PlayedCards.get(1).setPosition(350, 10);
            
            discardPC();

            canvas.redraw();

            if(p1PlayedCards.get(0).getValue() > p2PlayedCards.get(0).getValue()){
                for(int i = 0; i < discard.size(); i++){
                    p1Cards.add(discard.get(i));
                    discard.remove(discard.get(i));
                    p1Cards.get(p1Cards.size() - 1).setPosition(250, 10);
                    p1Cards.get(p1Cards.size() - 1).setFaceUp(false);
                    canvas.redraw();
                    tieRound = false;
                }
            }
            else if (p1PlayedCards.get(0).getValue() < p2PlayedCards.get(0).getValue()){
                for(int i = 0; i < discard.size(); i++){
                    p2Cards.add(discard.get(i));
                    discard.remove(discard.get(i));
                    p2Cards.get(p2Cards.size() - 1).setPosition(250, 600 - p2PlayedCards.get(0).getHeight() - 10);
                    p2Cards.get(p2Cards.size() - 1).setFaceUp(false);
                    canvas.redraw();
                    tieRound = false;
                }
            }
            updateCounter();
    }
    /**
     * Give winners all played cards
     */
    private void giveWC(){
        
        System.out.println("p1 " + p1PlayedCards.toString());
        System.out.println("p2 " + p2PlayedCards.toString());
        roundEnd = false;
        if(p1PlayedCards.get(0).getValue() > p2PlayedCards.get(0).getValue()){
            discardPC();
            p1RWinnerText.makeVisible();
            for(int i = 0; i < discard.size(); i++){
                Card c = discard.get(i);
                p1Cards.add(0, c);
                c.setPosition(50, 600 - c.getHeight() - 10);
                c.turnFaceDown();
                
                
                System.out.println("p1 added: " + c);
                prepRound = true;
                
            }
        }
        else if (p1PlayedCards.get(0).getValue() < p2PlayedCards.get(0).getValue()){
            discardPC();
            p2RWinnerText.makeVisible();
            for(int i = 0; i < discard.size(); i++){
                Card c = discard.get(i);
                p2Cards.add(0, c);
                c.setPosition(50, 10);
                c.turnFaceDown();
                
                System.out.println("p2 added: " + c);
                prepRound = true;
            }
        }
        else{
            discardPC();
            tieRText.makeVisible();
            tieRound = true;
        }
        for(int i = 0; i < discard.size(); ){
            discard.remove(0);
        }
        updateCounter();
        canvas.redraw();
        
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
     * Put all cards back to players' decks
     */
    private void giveCard(){
        for(Card card : p1Cards){
            card.setPosition(50, 600 - card.getHeight() - 10);
        }
        for(Card card : p2Cards){
            card.setPosition(50, 10);
        }
        canvas.redraw();
    }
    /*
     * End game when someone wins
     */
    private void winCondition(){
        if(p1Cards.size() == 52){
            //winnerText = new Text("You win!", 250, );
        }
        if(p2Cards.size() == 52){
            
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
