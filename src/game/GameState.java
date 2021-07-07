package game;

import actions.Action;
import components.*;

import java.util.ArrayList;
import java.util.List;

public class GameState
{
    Hand   p1_hand;
    Hand   p2_hand;
    Board  board;
    Deck   main_deck;
    Deck   discard_deck;
    int    turn;
    double p1_score;
    double p2_score;
    double factor;

    GameParameters gp;

    public GameState() {}

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
        p1_score = 0.0;
        p2_score = 0.0;
        factor   = 1.0;

        createMainDeck();
        drawCards();
    }

    public void nextPlayerTurn()
    {
        if (turn == 1) turn = 2;
        else           turn = 1;
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

    public double getP1Score()     { return p1_score;     }
    public double getP2Score()     { return p2_score;     }
    public int    getTurn()        { return turn;         }
    public Hand   getP1Hand()      { return p1_hand;      }
    public Hand   getP2Hand()      { return p2_hand;      }
    public Board  getBoard()       { return board;        }
    public Deck   getDiscardDeck() { return discard_deck; }
    public Deck   getMainDeck()    { return main_deck;    }
    public double getFactor()      { return factor;       }

    public void setSeed(int seed)
    {
        main_deck.setSeed(seed);
    }

    public void addP1Score(double score) { p1_score += score; }
    public void addP2Score(double score) { p2_score += score; }

    public void updateFactor(double factor) { this.factor = this.factor * factor; }
    public void resetFactor()               { factor = 1.0; }

    // TODO: Repensar
    public boolean isTerminal()  { return p2_hand.isEmpty() || board.isEmpty(); }
    public boolean isP1Turn()    { return turn == 1;                            }


    public GameState getObservation()
    {
        GameState obs = new GameState();

        obs.turn         = turn;
        obs.p1_score     = p1_score;
        obs.p2_score     = p2_score;
        obs.board        = board.deepCopy();
        obs.discard_deck = discard_deck.deepCopy();
        obs.factor       = factor;
        obs.gp           = gp;

        if (isP1Turn()) RandomizeHiddenInformationP1(obs);
        else            RandomizeHiddenInformationP2(obs);
        return obs;
    }

    private void RandomizeHiddenInformationP1(GameState obs)
    {
        obs.p1_hand   = p1_hand.deepCopy();
        obs.p2_hand   = new Hand();
        obs.main_deck = new Deck();

        Deck temp = new Deck();
        temp.addAll(p2_hand.getCards());
        temp.addAll(main_deck.getCards());
        temp.shuffle();

        // draw P2
        int n_cards_on_hand = p2_hand.getNumberOfCards();
        for (int i=0; i<n_cards_on_hand; i++)
        {
            Card c = temp.pop();
            obs.p2_hand.add(c);
        }

        obs.main_deck.addAll(temp.getCards());
    }

    private void RandomizeHiddenInformationP2(GameState obs)
    {
        obs.p1_hand   = p2_hand.deepCopy();
        obs.p2_hand   = new Hand();
        obs.main_deck = new Deck();

        Deck temp = new Deck();
        temp.addAll(p1_hand.getCards());
        temp.addAll(main_deck.getCards());
        temp.shuffle();

        // draw P1
        int n_cards_on_hand = p1_hand.getNumberOfCards();
        for (int i=0; i<n_cards_on_hand; i++)
        {
            Card c = temp.pop();
            obs.p1_hand.add(c);
        }

        obs.main_deck.addAll(temp.getCards());

    }

    // Return a list of all possible actions that can be played given the actual state of the observation (game state)
    public List<Action> getPossibleActions()
    {
        List<Action> actions = new ArrayList<>();
        Hand h;
        if (turn==1) h = p1_hand;
        else         h = p2_hand;

        for (int i=0; i < h.getNumberOfCards(); i++)
        {
            Card player_card = h.getCard(i);
            if (player_card.isNumberCard())
            {
                for (int j =0; j < board.getNumberOfCards(); j++)
                {
                    Card board_card = board.getCard(j);
                    Action a = new Action(i, j);
                    actions.add(a);
                }
            }
            else
            {
                Action a = new Action(i);
                actions.add(a);
            }
        }

        return actions;
    }

    public GameState deepCopy()
    {
        GameState new_object = new GameState();

        new_object.turn     = turn;
        new_object.p1_score = p1_score;
        new_object.p2_score = p2_score;
        new_object.factor   = factor;
        new_object.gp       = gp;

        new_object.p1_hand      = p1_hand.deepCopy();
        new_object.p2_hand      = p2_hand.deepCopy();
        new_object.board        = board.deepCopy();
        new_object.main_deck    = main_deck.deepCopy();
        new_object.discard_deck = discard_deck.deepCopy();

        return new_object;
    }

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
