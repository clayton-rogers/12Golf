package ca.claytonrogers.Common;


import java.util.*;

/**
 * Represents a deck of cards.
 * <p>
 * Created by clayton on 2015-07-04.
 */
public class Deck {

    private Deque<Card> cards = new LinkedList<>();

    public Deck() {}

    public Deck (Deck copy) {
        for (Card card : copy.cards) {
            cards.push(card);
        }
    }

    public void push(Card card) {
        cards.push(card);
    }

    public Card peek() {
        return cards.peek();
    }

    public Card pop() {
        return cards.pop();
    }

    public void shuffle (Random seed) {
        // We have to convert the Deque to a list in order to shuffle it.
        // Note that the elements themselves are not copied.
        List<Card> temp = new LinkedList<>(cards);
        Collections.shuffle(temp, seed);
        cards = new LinkedList<>(temp);
    }

    public static Deck getShuffledDeck() {
        Deck deck = new Deck();

        for (int i = 1; i <= 13; i++) {
            for (int j = 0; j < 4; j++) {
                deck.push(new Card(i, false));
            }
        }
        //deck.add(new Card(14));   // TODO FUTURE And the jokers
        //deck.add(new Card(14));

        deck.shuffle(Constants.random);

        return deck;
    }

    public int size() {
        return cards.size();
    }
}
