package tests;

import components.*;

public class TestCardCollection
{
    public static void main(String[] args)
    {
        // Some cards
        NumberCard c1 = new NumberCard(1, 1);
        NumberCard c2 = new NumberCard(2, 2);
        NumberCard c3 = new NumberCard(3, 3);
        NumberCard c4 = new NumberCard(4, 4);
        NumberCard c5 = new NumberCard(5, 5);
        SpecialCard c6 = new SpecialCard(6, Card.CardType.Mult2);
        SpecialCard c7 = new SpecialCard(7, Card.CardType.Div2);

        // Add to CardCollection
        CardCollection cards = new CardCollection();

        cards.add(c1);
        cards.add(c2);
        cards.add(c3);
        cards.add(c4);
        cards.add(c5);
        cards.add(c6);
        cards.add(c7);

        System.out.println("A card collection with 7 cards:");
        System.out.println(cards);

        System.out.println("Removing the card id 3 (number 3):");
        cards.remove(3);
        System.out.println(cards);

        System.out.println("A new card collection with just one card (8):");

        CardCollection cards2 = new CardCollection();
        NumberCard c8 = new NumberCard(8, 8);
        cards2.add(c8);
        System.out.println(cards2);

        System.out.println("Adding the cards of the first card collection:");
        cards2.addAll(cards);
        System.out.println(cards2);
    }
}
