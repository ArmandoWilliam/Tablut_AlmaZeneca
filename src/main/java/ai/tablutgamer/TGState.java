package ai.tablutgamer;

import aima.core.agent.Action;
import aima.core.search.framework.evalfunc.HeuristicFunction;
import aima.core.search.framework.problem.GoalTest;
import aima.core.search.framework.problem.StepCostFunction;

public class  TGState implements GoalTest, StepCostFunction, HeuristicFunction {

// Actions


/*****************************************************************************
* Description of the state of the problem
/*****************************************************************************/




/*****************************************************************************
* CONSTRUCTORS	
/*****************************************************************************/
/*
 * Default constructor.
 * 
 * It initializes the problem in the classic configuration (3,3,1).
 */


/*
 * Generic constructor.
 * 
 * Used for generating new states, as well as for creating different 
 * instances of the problem (with more people, and maybe on different shores)
 */
public TGState( ) {
	
	//do nothing
}







/*****************************************************************************
* Methods for the interface GoalTest
/*****************************************************************************/
public boolean isGoalState(Object state) {
	if(true)
		System.out.println("abbiamo vinto la partita!");
	
	return true;	
};





/*****************************************************************************
* Methods for the interface StepCostFunction
/*****************************************************************************/
/*
	public Double calculateStepCost(Object fromState, Object toState, String action) {
		return new Double(1);
	};
	*/
	
	
	public double c(Object fromState, Action a, Object toState) {
		return 1.0;
	}//	QUESTO CALCOLA IL COSTO
	



/*****************************************************************************
* Methods for the interface HeuristicFunction
/*****************************************************************************/
	// This is the same proposed by Russell-Norvig
public double getHeuristicValue(Object state) {
	return 0;
}

public double h(Object state) {
	return this.getHeuristicValue(state);
}


/*****************************************************************************
* Generic Methods
/*****************************************************************************/
	public int hashCode() {
	return 0;
}

	public boolean equals(Object o1) {
	
			return false;
		
	}

	public String toString() {
		return null;
}
	
	
	

}
