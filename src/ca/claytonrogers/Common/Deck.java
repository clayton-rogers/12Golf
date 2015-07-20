package ca.claytonrogers.Common;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

/**
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

    private void shuffle() {
        // We have to convert the Deque to a list in order to shuffle it.
        // Note that the elements themselves are not copied.
        List<Card> temp = new LinkedList<>(cards);
        Collections.shuffle(temp);
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

        deck.shuffle();

        return deck;
    }

    public int size() {
        return cards.size();
    }

    public static Deck read(DataInputStream in) throws IOException {

        int numberOfCards = in.readByte();
        LinkedList<Card> cards = new LinkedList<>();

        for (int i = 0; i < numberOfCards; i++) {
            Card card = new Card(in.readByte(), false);
            cards.add(card);
        }

        Deck deck = new Deck();
        deck.cards = cards;
        return deck;
    }

    public static void write(DataOutputStream out, Deck deck) throws IOException {

        List<Card> cardList = new LinkedList<>(deck.cards);
        out.writeByte(cardList.size());

        for (int i = 0; i < cardList.size(); i++) {
            out.writeByte(cardList.get(i).value);
        }
    }
}
