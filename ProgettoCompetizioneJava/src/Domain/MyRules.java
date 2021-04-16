package Domain;

import java.util.List;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;

public interface MyRules {
	State movePawn(State state, Action a);

//	State checkMove(State state, Action a) throws BoardException, ActionException, StopException, PawnException, DiagonalException, ClimbingException, ThroneException, OccupitedException, CitadelException, ClimbingCitadelException;

	public List<Action> getNextMovesFromState(State state);
}
