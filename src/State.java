import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

class State {
    String name;
    boolean isInitialState;
    boolean isTerminalState = false;
    HashMap<Event,State> stateTransitions = new HashMap<>();
    ArrayList<State> listOfReachableTerminalStates = new ArrayList<>();

    public void setStateTransitions(Event event, State state) {
        stateTransitions.put(event,state);
    }

    public boolean isValidEventInput(Event event) {
        if(stateTransitions.containsKey(event))
            return true;
        return false;
    }

    public State(String name,boolean isTerminalState, boolean isInitialState) {
        this.name = name;
        this.isTerminalState = isTerminalState;
        this.isInitialState = isInitialState;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean validateSelf() {
        System.out.println(stateTransitions);

        if(isTerminalState && (stateTransitions.size() != 0)) {
            // It is a terminal state and we have transitions from here!
            return false;
        }
        if(isInitialState && (stateTransitions.size() == 0)) {
            // It is an initial state and we have don't have transitions from here!
            return false;
        }

         constructTerminalStateList();
        if(listOfReachableTerminalStates.isEmpty() && !isTerminalState) {
            //No terminal states were reachable from this state and this isnt a terminal state itself
            return false;
        }

        return true;
    }

    private void constructTerminalStateList() {
        Stack<State> statesParsed = new Stack<>();
        Stack<State> statesRemaining = new Stack<>();

        for(State s : stateTransitions.values()) {
            // Push all the states reachable from this state into the statesRemaining
            statesRemaining.push(s);
        }
        statesParsed.push(this);


        while(!statesRemaining.empty()) {

            State s = statesRemaining.pop();

            if(s.isTerminalState)
            {
                // If we find a terminal state, move it to both the statesParsed and listOfReachableTerminalStates
                listOfReachableTerminalStates.add(s);
            }

            for(State reachableState : s.stateTransitions.values()) {
                //This state is in either stack already, don't process duplicates!
                if(statesParsed.search(reachableState) >= 1) {
                    continue;
                }
                if(statesRemaining.search(reachableState) >= 1) {
                    continue;
                }
                statesRemaining.push(reachableState);
            }

            statesParsed.push(s);
        }
        return;
    }


    public State doStateTransition(Event event) {
        return this.stateTransitions.get(event);
    }
}