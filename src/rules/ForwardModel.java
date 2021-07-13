package rules;

import actions.Action;
import game.GameState;

public interface ForwardModel
{
    boolean step(GameState gs, Action a);
}
