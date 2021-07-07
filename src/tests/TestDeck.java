package tests;

import components.Card;
import components.Deck;
import components.NumberCard;
import components.SpecialCard;

public class TestDeck
{
    public static void main(String[] args)
    {
        Deck d = new Deck();

        NumberCard c1 = new NumberCard(1);
        NumberCard c2 = new NumberCard(2);
        NumberCard c3 = new NumberCard(3);
        NumberCard c4 = new NumberCard(4);
        NumberCard c5 = new NumberCard(5);
        SpecialCard c6 = new SpecialCard(Card.CardType.Mult2);
        SpecialCard c7 = new SpecialCard(Card.CardType.Div2);

        System.out.println("We are going to insert the next cards into a deck:");
        System.out.println(c1);
        System.out.println(c2);
        System.out.println(c3);
        System.out.println(c4);
        System.out.println(c5);
        System.out.println(c6);
        System.out.println(c7);

        System.out.println("\nDeck:");

        d.add(c1);
        d.add(c2);
        d.add(c3);
        d.add(c4);
        d.add(c5);
        d.add(c6);
        d.add(c7);

        System.out.println(d);

        System.out.println("\nShuffle the deck");
        d.shuffle();
        System.out.println(d);

        System.out.println("\nPop a card:");
        Card c = d.pop();
        System.out.println(c);

        System.out.println("\nDeck after poping:");
        System.out.println(d);
    }
}
