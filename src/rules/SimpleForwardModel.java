package rules;

import actions.Action;
import components.Board;
import components.Card;
import components.Hand;
import game.GameState;

public class SimpleForwardModel implements ForwardModel
{
    // Actualize the game state gs playing action a
    @Override
    public void step(GameState gs, Action a)
    {
        Hand h;
        if (gs.getTurn() == 1) h = gs.getP1Hand();
        else                   h = gs.getP2Hand();

        Board b = gs.getBoard();

        Card card_to_play = h.getCard(a.GetPlayerCardId());

        if (a.GetOnPlayerCardId() != -1)
        {
            Card card_on_play = b.getCard(a.GetOnPlayerCardId());

            int    n1    = card_to_play.getNumber();
            int    n2    = card_on_play.getNumber();
            double score = n1 - n2;

            if (gs.getFactor() != 1.0)
            {
                score = score * gs.getFactor();
                gs.resetFactor();
            }

            if (gs.getTurn() == 1) gs.addP1Score(score);
            else                   gs.addP2Score(score);

            gs.getDiscardDeck().add(card_to_play);
            gs.getDiscardDeck().add(card_on_play);
            h.remove(a.GetPlayerCardId());
            b.remove(a.GetOnPlayerCardId());
        }
        else
        {
            if (card_to_play.getType() == Card.CardType.Mult2) gs.updateFactor(2.0);
            else                                               gs.updateFactor(0.5);

            gs.getDiscardDeck().add(card_to_play);
            h.remove(a.GetPlayerCardId());
        }
    }
}
