package strategy;

import it.unibo.ai.didattica.competition.tablut.domain.State;

public interface Heuristics {
	public double heuristic(State state);
}
