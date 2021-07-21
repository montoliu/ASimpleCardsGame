package actions;

import players.shared.Genome;

public class Action
{
    int played_card_id;
    int on_played_card_id;

    public Action(int played_card_id, int on_played_card_id)
    {
        this.played_card_id    = played_card_id;
        this.on_played_card_id = on_played_card_id;
    }

    public Action(int played_card_id)
    {
        this.played_card_id    = played_card_id;
        this.on_played_card_id = -1;
    }

    public int GetPlayerCardId()   { return played_card_id;    }
    public int GetOnPlayerCardId() { return on_played_card_id; }

    @Override
    public String toString()
    {
        return "Action [" + String.format("%02d", played_card_id) + " " + String.format("%02d", on_played_card_id) + "]";
    }

    @Override
    public int hashCode() {
        return played_card_id * 1000 + on_played_card_id;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj.getClass() == this.getClass())
        {
            Action other = (Action) obj;
            return played_card_id == other.played_card_id && on_played_card_id == other.on_played_card_id;
        }
         return false;
    }
}
