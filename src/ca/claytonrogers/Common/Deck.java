package ca.claytonrogers.Common;


import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by clayton on 2015-07-04.
 */
public class Deck {

    private Deque<Integer> cards = new LinkedList<>();

    public void push(int card) {
        cards.push(card);
    }

    public int peek() {
        return cards.peek();
    }

    public int pop() {
        return cards.pop();
    }

    public void shuffle() {
        // TODO
    }

    public static Deck getShuffledDeck() {
        Deck deck = new Deck();

        for (int i = 1; i <= 13; i++) {
            for (int j = 0; j < 4; j++) {
                int card = i;
                deck.push(card);
            }
        }
        //deck.add(new Card(14));   // TODO FUTURE And the jokers
        //deck.add(new Card(14));

        deck.shuffle();

        return deck;
    }
}
