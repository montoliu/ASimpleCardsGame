package game;

import javax.net.ssl.SNIHostName;
import java.util.List;

public class GameState
{
    Hand  p1_hand;
    Hand  p2_hand;
    Board board;
    Deck  main_deck;
    Deck  discard_deck;
    int   turn;
    int   p1_score;
    int   p2_score;

    GameParameters gp;

    public GameState(GameParameters gp)
    {
        p1_hand      = new Hand();
        p2_hand      = new Hand();
        board        = new Board();
        main_deck    = new Deck();
        discard_deck = new Deck();

        this.gp      = gp;
        main_deck.setSeed(-1);
    }

    public void initialize()
    {
        p1_hand.clear();
        p2_hand.clear();
        board.clear();
        discard_deck.clear();

        turn     = 1;
        p1_score = 0;
        p2_score = 0;

        createMainDeck();
        drawCards();
    }

    private void createMainDeck()
    {
        // Number cards
        for (int n = gp.min_number; n <= gp.max_number; n++)
        {
            if (n == gp.min_number || n == gp.max_number)
                for (int i=0; i<gp.number_cards_limit_number; i++)
                {
                    NumberCard c = new NumberCard(n);
                    main_deck.add(c);
                }
            else
                for (int i=0; i<gp.number_cards_normal_number; i++)
                {
                    NumberCard c = new NumberCard(n);
                    main_deck.add(c);
                }
        }

        // Special cards
        for (int i=0; i<gp.number_cards_mult2; i++)
        {
            SpecialCard c = new SpecialCard(Card.CardType.Mult2);
            main_deck.add(c);
        }

        for (int i=0; i<gp.number_cards_div2; i++)
        {
            SpecialCard c = new SpecialCard(Card.CardType.Div2);
            main_deck.add(c);
        }

        main_deck.shuffle();
    }

    private void drawCards()
    {
        // hands
        for (int i=0; i<gp.number_cards_on_hand; i++)
        {
            Card c1 = main_deck.pop();
            Card c2 = main_deck.pop();

            p1_hand.add(c1);
            p2_hand.add(c2);
        }

        // Board. Only number cards
        for (int i=0; i<gp.number_cards_on_board; i++)
        {
            Card c = main_deck.pop();
            while (!c.isNumberCard())
            {
                main_deck.add(c);
                c = main_deck.pop();
            }
            board.add(c);
        }
    }

    public GameState Clone()
    {
        return null;
    }

    public int   getP1Score()     { return p1_score;     }
    public int   getP2Score()     { return p2_score;     }
    public int   getTurn()        { return turn;         }
    public Hand  getP1Hand()      { return p1_hand;      }
    public Hand  getP2Hand()      { return p2_hand;      }
    public Board getBoard()       { return board;        }
    public Deck  getDiscardDeck() { return discard_deck; }
    public Deck  getMainDeck()    { return main_deck;    }

    public void setSeed(int seed)
    {
        main_deck.setSeed(seed);
    }

    public boolean isTerminal()  { return p2_hand.isEmpty() || board.isEmpty(); }
    public boolean isP1Turn()    { return turn == 1;                            }

    public String toString()
    {
        return "\nP1 score    : "  + getP1Score() +
                "\nP2 score    : " + getP2Score() +
                "\nP1 Hand     : " + p1_hand +
                "\nP2 Hand     : " + p2_hand +
                "\nBoard       : " + board +
                "\nMain deck   : " + main_deck +
                "\nDiscard deck: " + discard_deck;
    }
}
