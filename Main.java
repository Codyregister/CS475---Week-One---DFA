import javax.swing.*;
import java.io.*;
import java.util.*;

class FiniteAutomata {
    private String startState;
    private ArrayList<String> acceptStates;
    private ArrayList<String> states;
    private ArrayList<Transition> transitions;
    private boolean debug;

    public FiniteAutomata(String startState, ArrayList<String> acceptStates, ArrayList<String> states, boolean debug) {
        this.startState = startState;
        this.acceptStates = acceptStates;
        this.states = states;
        this.transitions = new ArrayList<>();
        this.debug = debug;
    }

    public ArrayList<Character> alphabet() {
        ArrayList<Character> alphabet = new ArrayList<>();
        for (Transition t : transitions) {
            if (!alphabet.contains(t.getLabel())) {
                alphabet.add(t.getLabel());
            }
        }
        return alphabet;
    }

    public boolean run(String input) {
        String currentState = startState;
        for (char c : input.toCharArray()) {
            boolean moved = false;
            for (Transition t : transitions) {
                if (t.getFromState().equals(currentState) && t.getLabel() == c) {
                    currentState = t.getToState();
                    moved = true;
                    if (debug) {
                        System.out.println("On Symbol: " + c + " | Moving to State: " + currentState);
                    }
                    break;
                }
            }
            if (!moved) {
                if (debug) {
                    System.out.println("On Symbol: " + c + " | No valid transition. Rejecting.");
                }
                return false;
            }
        }
        if (debug) {
            System.out.println("Final State: " + currentState + " | Input is " + (acceptStates.contains(currentState) ? "accepted" : "not accepted"));
        }
        return acceptStates.contains(currentState);
    }

    public ArrayList<String> states() {
        return states;
    }

    public void addTransition(Transition t) {
        transitions.add(t);
    }
}

class Transition {
    private String fromState;
    private char label;
    private String toState;

    public Transition(String fromState, char label, String toState) {
        this.fromState = fromState;
        this.label = label;
        this.toState = toState;
    }

    public String getFromState() {
        return fromState;
    }

    public char getLabel() {
        return label;
    }

    public String getToState() {
        return toState;
    }
}
public class Main {
    public static void main(String[] args) throws IOException {
        boolean debug = true; 

        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);
        if (returnVal != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        Scanner scanner = new Scanner(file);

        String startState = scanner.nextLine();
        ArrayList<String> acceptStates = new ArrayList<>(Arrays.asList(scanner.nextLine().split(" ")));
        ArrayList<String> statesList = new ArrayList<>(); 
        statesList.add(startState);
        statesList.addAll(acceptStates);
        
        FiniteAutomata automata = new FiniteAutomata(startState, acceptStates, new ArrayList<>(), debug);

        while (scanner.hasNext()) {
            String[] parts = scanner.nextLine().split(" ");
            automata.addTransition(new Transition(parts[0], parts[1].charAt(0), parts[2]));
            
            if (!statesList.contains(parts[0])) {
                statesList.add(parts[0]);
            }
            if (!statesList.contains(parts[2])) {
                statesList.add(parts[2]);
            }
        }
        automata.states().addAll(statesList); 
        
        scanner.close();

        JOptionPane.showMessageDialog(null, "Alphabet of the automaton: " + automata.alphabet());

        while (true) {
            String input = JOptionPane.showInputDialog("Enter an input string:");
            if (input == null) break;
            JOptionPane.showMessageDialog(null, "Input string is " + 
                (automata.run(input) ? "accepted" : "not accepted") + " by the automaton.");
        }
    }
}




