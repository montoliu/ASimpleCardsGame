package rules;

import actions.Action;
import game.GameState;

public interface ForwardModel
{
    void step(GameState gs, Action a);
}
