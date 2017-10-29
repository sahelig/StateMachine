import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class StateMachineManager {
    public static void main(String args[]) throws IOException {

        HashMap<String, State> stateListNew = new HashMap<>();
        stateListNew.put("0S",new State("0S",false, true));
        stateListNew.put("1S",new State("1S", false, false));
        stateListNew.put("2S",new State("2S", false, false));
        stateListNew.put("3S",new State("3S", false, false));
        stateListNew.put("4S",new State("4S", false, false));
        stateListNew.put("CANCELLED",new State("CANCELLED", true, false));
        stateListNew.put("COMPLETED",new State("COMPLETED", true, false));

        ArrayList<Transition> transitionList = new ArrayList<>();
        transitionList.add(new Transition("0S", "1R", "1S"));
        transitionList.add(new Transition("0S", "2R", "2S"));
        transitionList.add(new Transition("1S", "1R", "2S"));
        transitionList.add(new Transition("1S", "2R", "3S"));
        transitionList.add(new Transition("2S", "2R", "4S"));
        transitionList.add(new Transition("3S", "1R", "4S"));
        transitionList.add(new Transition("*", "CANCEL", "CANCELLED"));
        transitionList.add(new Transition("4S", "COMPLETE", "COMPLETED"));

        for(Transition transition : transitionList) {
            if(transition.currentName.equalsIgnoreCase("*")) {
                stateListNew.values().forEach(state -> {if(!state.isTerminalState) {state.setStateTransitions(new Event(transition.eventName),stateListNew.get(transition.destName));}});
            }
            else if(!transition.currentName.equalsIgnoreCase("*"))
            stateListNew.get(transition.currentName).setStateTransitions(new Event(transition.eventName),stateListNew.get(transition.destName));
        }

        for(State state: stateListNew.values()) {
            if(!state.validateSelf() && !state.isTerminalState) {
                System.out.println("State "+ state.name + " does not lead to a terminal state!!");
                return;
            }
            System.out.println(state + "List of terminal states is: " + state.listOfReachableTerminalStates);
        }

        ArrayList<State> terminalStates = new ArrayList<>();

        for(State state: stateListNew.values()) {
            terminalStates.removeAll(state.listOfReachableTerminalStates);
            terminalStates.addAll(state.listOfReachableTerminalStates);
        }

        System.out.println("Final list of terminal states: "+terminalStates);

        for(State state: stateListNew.values()) {
            if(state.isTerminalState && !terminalStates.contains(state)) {
                System.out.println("This terminal state isn't reachacble!! Terminating program now " + state);
                return;
            }
        }

        System.out.println("State machine has been validated!! Now please enter your inputs for state transitions");
        State currentState = null;
        boolean isTerminalStateReached = false;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while(!isTerminalStateReached) {
            System.out.print(">");
            String input = br.readLine();

            if(currentState == null) {
                if(stateListNew.containsKey(input) && stateListNew.get(input).isInitialState) {
                    currentState = stateListNew.get(input);
                    System.out.println("<"+currentState);
                    continue;
                }
                else {
                    System.out.println("Enter a valid initial state name!!");
                    continue;
                }
            }

            if(!currentState.isValidEventInput(new Event(input))) {
                System.out.println("Invalid event input, try again!!");
                continue;
            }

            currentState = currentState.doStateTransition(new Event(input));


            String output;
            if(currentState.isTerminalState) {
                System.out.println("<!" + currentState);
                return;
            }

            System.out.println("<" + currentState);
        }
    }
}