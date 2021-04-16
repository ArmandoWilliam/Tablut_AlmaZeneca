package strategy;

import it.unibo.ai.didattica.competition.tablut.domain.State;

public interface Heuristic {
	public double heuristic(State state);
}
