package strategy;

import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;

public class MyIterativeDeepeningAlphaBetaSearch extends IterativeDeepeningAlphaBetaSearch<State, Action, State.Turn>{


    public MyIterativeDeepeningAlphaBetaSearch(Game<State, Action, State.Turn> game, double utilMin, double utilMax, int time) {
        super(game, utilMin, utilMax, time);
    }
}
