package WhiteSpacePuzzle;

import AI.AbstractState;
import AI.AIFunction;

import java.util.HashMap;


/**
 * Defines what the 8 Puzzle is. Defines operators and states! REQUIRED: Make abstract class
 */
public class WhiteSpacePuzzle {

    //Square Size
    private int SIZE;

    //Initial and Goal state
    private State initState;


    private State goalState;

    //Manages operators
    private OperatorManager operatorManager;

    /**
     * Constructor to create size of puzzle.
     *
     * @param size
     */
    public WhiteSpacePuzzle(int size) {
        SIZE = size;
        this.operatorManager = new OperatorManager();
        setDefaultGoalState();
    }

    /**
     * Gets the size of the puzzle board
     *
     * @return
     */
    public int getSize() {
        return SIZE;
    }

    /**
     * Sets the initial state.
     *
     * @param initState
     */
    public void setInitialState(int[][] initState) {
        State state = new State(initState);
        this.initState = state;
    }

    /**
     * Gets the initial state
     *
     * @return
     */
    public State getInitState() {
        return initState;
    }

    /**
     * Sets the default goal state
     */
    public void setDefaultGoalState() {

        int[][] localGoalState = new int[SIZE][SIZE];

        int counter = 1;
        for (int rows = 0; rows < SIZE; rows++) {
            for (int cols = 0; cols < SIZE; cols++) {
                localGoalState[rows][cols] = counter++;
            }
        }
        localGoalState[SIZE - 1][SIZE - 1] = State.SPACE;
        this.goalState = new State(localGoalState, true);
    }


    public void setGoalState(int[][] goalState) {
        State state = new State(goalState, true);
        this.goalState = state;
    }

    /**
     * Gets the goal state
     *
     * @return
     */
    public State getGoalState() {
        return goalState;
    }

    /**
     * Returns the operator manager
     *
     * @return
     */
    public OperatorManager getOperatorManager() {
        return operatorManager;
    }

    /**
     * Defines the states for the puzzle. The state object is used to track what state we are on in the 8 puzzle.
     */
    public class State extends AbstractState {
        //The current state
        private int[][] state;

        //The location of the white space
        int rowIndex = -1;
        int colIndex = -1;

        //The number that is used to define the white-space
        private static final int SPACE = 0;

        //Variables the keep the cost of this state.
        private int manhattanDistanceCost;
        private int misplacedTileCost;
        private int euclideanDistanceCost;

        /**
         * Default constructor that creates a 2D state array.
         */
        public State() {
            this.state = new int[SIZE][SIZE];
        }

        /**
         * Copies the oldState (deep copy, no references)
         *
         * @param oldState The old state. Use this node to copy the parent nodes without worry of reference copy object.
         */
        public State(int[][] oldState) {
            this.state = new int[SIZE][SIZE];

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    state[i][j] = oldState[i][j];
                }
            }
            findSpaceIndexes();
            setCosts();
        }

        /**
         * Creates the goal state
         *
         * @param oldState The goal state
         * @param isGoal   true to set the goal state (and skip costs), false to not set the goal state
         */
        private State(int[][] oldState, boolean isGoal) {
            this.state = new int[SIZE][SIZE];

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    state[i][j] = oldState[i][j];
                }
            }
            findSpaceIndexes();

            if (!isGoal) {
                setCosts();
            }
        }

        /**
         * Returns this state data
         *
         * @return the 2D state array
         */
        public int[][] getState() {
            return state;
        }

        /**
         * Sets the state
         *
         * @param state the state data
         */
        public void setState(int[][] state) {
            this.state = state;
            findSpaceIndexes();
            setCosts();

        }

        /**
         * Checks if this state is the goal state
         *
         * @return true if this is the goal state, false otherwise
         */
        @Override
        public boolean isGoalState() {
            return equals(getGoalState());
        }

        /**
         * The column index of the white-space.
         *
         * @return an integer that is 0 or higher that represents the column index of the white space in the 2D array.
         */
        public int getColIndex() {
            return colIndex;
        }

        /**
         * The row index of the white-space.
         *
         * @return an integer that is 0 or higher that represents the row index of the white space in the 2D array.
         */
        public int getRowIndex() {
            return rowIndex;
        }

        /**
         * Finds the white space in the 2D array which updates col index and row index.
         */
        private void findSpaceIndexes() {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (state[i][j] == SPACE) {
                        rowIndex = i;
                        colIndex = j;
                        return;
                    }
                }
            }
        }

        /**
         * Sets the costs for misplaced tiles and manhattan distance to reduce computation time. (increases memory space in return)
         */
        private void setCosts() {

            int misplacedCost = 0;
            int manhattanDistanceCost = 0;
            int euclideanDistanceCost = 0;

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {

                    if (state[i][j] != SPACE) //If this is not the space
                    {
                        if (state[i][j] != goalState.state[i][j]) //If not the goal state
                        {
                            misplacedCost++; //This tile is misplaced, increase the cost by 1.


                            findGoalState:
                            //This loop computes the manhattan distance to find the goal state.
                            for (int findGoalStateRow = 0; findGoalStateRow < SIZE; findGoalStateRow++) {
                                for (int findGoalStateCol = 0; findGoalStateCol < SIZE; findGoalStateCol++) {
                                    if (state[i][j] == goalState.state[findGoalStateRow][findGoalStateCol]) {

                                        manhattanDistanceCost += Math.abs(findGoalStateRow - i) + Math.abs(findGoalStateCol - j);
                                        euclideanDistanceCost += Math.sqrt(Math.pow(findGoalStateRow - i, 2) + Math.pow(findGoalStateCol - j, 2));
                                        break findGoalState;
                                    }
                                }
                            }

                        }
                    }
                }
            }

            this.manhattanDistanceCost = manhattanDistanceCost;
            this.misplacedTileCost = misplacedCost;
            this.euclideanDistanceCost = euclideanDistanceCost;


        }

        /**
         * Gets the misplaced tile cost
         *
         * @return the cost of misplaced tiles
         */
        public int getMisplacedTileCost() {
            return this.misplacedTileCost;
        }

        /**
         * Gets the manhattan distance cost
         *
         * @return the manhattan distance cost
         */
        public int getManhattanDistanceCost() {
            return manhattanDistanceCost;
        }

        /**
         * Gets the euclidean distance cost
         *
         * @return
         */
        public int getEuclideanDistanceCost() {
            return euclideanDistanceCost;
        }

        /**
         * Defines the equal operator by comparing the elements in the indexes i,j.
         *
         * @param obj the object in comparison
         * @return true if equal, false otherwise
         */
        @Override
        public boolean equals(Object obj) {
            if (obj != null) {
                if (obj instanceof State) {
                    State otherState = (State) obj;

                    for (int i = 0; i < SIZE; i++) {
                        for (int j = 0; j < SIZE; j++) {
                            if (this.state[i][j] != otherState.state[i][j])
                                return false;
                        }
                    }

                    return true;

                }
            }
            return false;
        }

        /**
         * Prints the state data in a readable format
         *
         * @return
         */
        public String toString() {
            String s = "";
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    s += state[i][j] + " ";
                }
                s += "\n";
            }

            if (s.isEmpty())
                return s;
            else
                return s.substring(0, s.length() - 1);
        }
    }

    /**
     * Defines the manager which handles all operators and their costs
     */
    public class OperatorManager {
        //A map that contains the function and their cost
        private HashMap<AIFunction<State>, Float> operators;

        /**
         * Default constructor that adds operators and their costs
         */
        public OperatorManager() {
            this.operators = new HashMap<>();
            this.operators.put(getMoveLeftFunction(), (float) 1);
            this.operators.put(getMoveDownFunction(), (float) 1);
            this.operators.put(getMoveRightFunction(), (float) 1);
            this.operators.put(getMoveUpFunction(), (float) 1);

        }

        /**
         * Gets the operators in a map (function and cost)
         *
         * @return
         */
        public HashMap<AIFunction<State>, Float> getOperators() {
            return operators;
        }

        /**
         * The MoveLeft operator
         *
         * @return the function which defines what moving left does
         */
        public AIFunction<State> getMoveLeftFunction() {


            AIFunction<State> function = new AIFunction<State>() {
                @Override
                public State apply(State state) {
                    final int[][] oldStateArr = state.getState();
                    State newState = new State(oldStateArr);
                    int[][] newStateArr = newState.getState();

                    //If this is the most far left you can go
                    if (newState.getColIndex() != 0) {
                        //Update the array by swapping white space with the other value
                        newStateArr[newState.getRowIndex()][newState.getColIndex()] = newStateArr[newState.getRowIndex()][newState.getColIndex() - 1];
                        newStateArr[newState.getRowIndex()][newState.getColIndex() - 1] = State.SPACE;
                        return newState;

                    } else {
                        return null;
                    }
                }
            };

            function.setName("MoveLeftFunction");

            return function;
        }

        /**
         * The MoveDown operator
         *
         * @return the function which defines what moving down does
         */
        public AIFunction<State> getMoveDownFunction() {


            AIFunction<State> function = new AIFunction<State>() {
                @Override
                public State apply(State state) {
                    final int[][] oldStateArr = state.getState();
                    State newState = new State(oldStateArr);
                    int[][] newStateArr = newState.getState();

                    //If this is the most far down you can go
                    if (newState.getRowIndex() != SIZE - 1) {
                        //Update the array by swapping white space with the other value
                        newStateArr[newState.getRowIndex()][newState.getColIndex()] = newStateArr[newState.getRowIndex() + 1][newState.getColIndex()];
                        newStateArr[newState.getRowIndex() + 1][newState.getColIndex()] = State.SPACE;
                        return newState;

                    } else {
                        return null;
                    }
                }
            };
            function.setName("MoveDownFunction");

            return function;
        }

        /**
         * The MoveRight operator
         *
         * @return the function which defines what moving right does
         */
        public AIFunction<State> getMoveRightFunction() {


            AIFunction<State> function = new AIFunction<State>() {
                @Override
                public State apply(State state) {
                    final int[][] oldStateArr = state.getState();
                    State newState = new State(oldStateArr);
                    int[][] newStateArr = newState.getState();

                    //If this is the most far right you can go
                    if (newState.getColIndex() != SIZE - 1) {
                        //Update the array by swapping white space with the other value
                        newStateArr[newState.getRowIndex()][newState.getColIndex()] = newStateArr[newState.getRowIndex()][newState.getColIndex() + 1];
                        newStateArr[newState.getRowIndex()][newState.getColIndex() + 1] = State.SPACE;
                        return newState;

                    } else {
                        return null;
                    }
                }
            };
            function.setName("MoveRightFunction");

            return function;
        }

        /**
         * The MoveUp operator
         *
         * @return the function which defines what moving up does
         */
        public AIFunction<State> getMoveUpFunction() {


            AIFunction<State> function = new AIFunction<State>() {
                @Override
                public State apply(State state) {
                    final int[][] oldStateArr = state.getState();
                    State newState = new State(oldStateArr);
                    int[][] newStateArr = newState.getState();

                    //If this is the most far up you can go
                    if (newState.getRowIndex() != 0) {
                        //Update the array by swapping white space with the other value
                        newStateArr[newState.getRowIndex()][newState.getColIndex()] = newStateArr[newState.getRowIndex() - 1][newState.getColIndex()];
                        newStateArr[newState.getRowIndex() - 1][newState.getColIndex()] = State.SPACE;
                        return newState;

                    } else {
                        return null;
                    }
                }
            };
            function.setName("MoveUpFunction");

            return function;
        }

    }


}
