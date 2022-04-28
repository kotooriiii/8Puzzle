package WhiteSpacePuzzle;

import AI.AbstractState;
import WhiteSpacePuzzle.operators.AIFunction;

import java.util.HashMap;
import java.util.function.Function;

public class WhiteSpacePuzzle
{

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
    public WhiteSpacePuzzle(int size)
    {
        SIZE = size;
        this.operatorManager = new OperatorManager();
        setDefaultGoalState();
    }

    /**
     * Gets the size of the puzzle board
     *
     * @return
     */
    public int getSize()
    {
        return SIZE;
    }

    /**
     * Sets the initial state.
     *
     * @param initState
     */
    public void setInitialState(int[][] initState)
    {
        State state = new State(initState);
        this.initState = state;
    }

    /**
     * Gets the initial state
     *
     * @return
     */
    public State getInitState()
    {
        return initState;
    }

    /**
     * Sets the goal state
     */
    public void setDefaultGoalState()
    {

        int[][] localGoalState = new int[SIZE][SIZE];

        int counter = 0;
        for (int rows = 0; rows < SIZE; rows++)
        {
            for (int cols = 0; cols < SIZE; cols++)
            {
                localGoalState[rows][cols] = counter++;
            }
        }
        localGoalState[0][0] = State.SPACE;
        this.goalState = new State(localGoalState, true);
    }

    public void setGoalState(int[][] goalState)
    {
        State state = new State(goalState, true);
        this.goalState = state;
    }

    /**
     * Gets the goal state
     *
     * @return
     */
    public State getGoalState()
    {
        return goalState;
    }

    /**
     * Returns the operator manager
     *
     * @return
     */
    public OperatorManager getOperatorManager()
    {
        return operatorManager;
    }

    public class State extends AbstractState
    {
        private int[][] state;

        int rowIndex = -1;
        int colIndex = -1;

        private static final int SPACE = 0;

        private int manhattanDistanceCost;
        private int misplacedTileCost;

        public State()
        {
            this.state = new int[SIZE][SIZE];
        }

        public State(int[][] oldState)
        {
            this.state = new int[SIZE][SIZE];

            for (int i = 0; i < SIZE; i++)
            {
                for (int j = 0; j < SIZE; j++)
                {
                    state[i][j] = oldState[i][j];
                }
            }
            findSpaceIndexes();
            setCosts();
        }

        private State(int[][] oldState, boolean isGoal)
        {
            this.state = new int[SIZE][SIZE];

            for (int i = 0; i < SIZE; i++)
            {
                for (int j = 0; j < SIZE; j++)
                {
                    state[i][j] = oldState[i][j];
                }
            }
            findSpaceIndexes();

            if(!isGoal)
            {
                setCosts();

            }
        }

        public int[][] getState()
        {
            return state;
        }

        public void setState(int[][] state)
        {
            this.state = state;
            findSpaceIndexes();
            setCosts();

        }

        @Override
        public boolean isGoalState()
        {
            return this.state.equals(getGoalState());
        }


        public int getColIndex()
        {
            return colIndex;
        }

        public int getRowIndex()
        {
            return rowIndex;
        }

        private void findSpaceIndexes()
        {
            for (int i = 0; i < SIZE; i++)
            {
                for (int j = 0; j < SIZE; j++)
                {
                    if (state[i][j] == SPACE)
                    {
                        rowIndex = i;
                        colIndex = j;
                        return;
                    }
                }
            }
        }

        private void setCosts()
        {

            int misplacedCost = 0;
            int manhattanDistanceCost = 0;

            for (int i = 0; i < SIZE; i++)
            {
                for (int j = 0; j < SIZE; j++)
                {

                    if (state[i][j] != SPACE)
                    {
                        if (state[i][j] != goalState.state[i][j])
                        {
                            misplacedCost++;


                            findGoalState:
                            for (int findGoalStateRow = 0; findGoalStateRow < SIZE; findGoalStateRow++)
                            {
                                for (int findGoalStateCol = 0; findGoalStateCol < SIZE; findGoalStateCol++)
                                {
                                    if (state[i][j] == goalState.state[findGoalStateRow][findGoalStateCol])
                                    {

                                        manhattanDistanceCost += Math.abs(findGoalStateRow - i) + Math.abs(findGoalStateCol - j);
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


        }

        public int getMisplacedTileCost()
        {
            return this.misplacedTileCost;
        }

        public int getManhattanDistanceCost()
        {
            return manhattanDistanceCost;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj != null)
            {
                if (obj instanceof State)
                {
                    State otherState = (State) obj;

                    for (int i = 0; i < SIZE; i++)
                    {
                        for (int j = 0; j < SIZE; j++)
                        {
                            if (this.state[i][j] != otherState.state[i][j])
                                return false;
                        }
                    }

                    return true;

                }
            }
            return false;
        }

        public String toString()
        {
            String s = "";
            for (int i = 0; i < SIZE; i++)
            {
                for (int j = 0; j < SIZE; j++)
                {
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

    public class OperatorManager
    {
        private HashMap<AIFunction<State>, Float> operators;

        public OperatorManager()
        {
            this.operators = new HashMap<>();
            this.operators.put(getMoveLeftFunction(), (float) 1);
            this.operators.put(getMoveDownFunction(), (float) 1);
            this.operators.put(getMoveRightFunction(), (float) 1);
            this.operators.put(getMoveUpFunction(), (float) 1);

        }

        public HashMap<AIFunction<State>, Float> getOperators()
        {
            return operators;
        }

        public AIFunction<State> getMoveLeftFunction()
        {


            AIFunction<State> function = new AIFunction<State>()
            {
                @Override
                public State apply(State state)
                {
                    final int[][] oldStateArr = state.getState();
                    State newState = new State(oldStateArr);
                    int[][] newStateArr = newState.getState();

                    if (newState.getColIndex() != 0)
                    {
                        newStateArr[newState.getRowIndex()][newState.getColIndex()] = newStateArr[newState.getRowIndex()][newState.getColIndex() - 1];
                        newStateArr[newState.getRowIndex()][newState.getColIndex() - 1] = State.SPACE;
                        return newState;

                    } else
                    {
                        return null;
                    }
                }
            };

            function.setName("MoveLeftFunction");

            return function;
        }

        public AIFunction<State> getMoveDownFunction()
        {


            AIFunction<State> function = new AIFunction<State>()
            {
                @Override
                public State apply(State state)
                {
                    final int[][] oldStateArr = state.getState();
                    State newState = new State(oldStateArr);
                    int[][] newStateArr = newState.getState();

                    if (newState.getRowIndex() != SIZE - 1)
                    {
                        newStateArr[newState.getRowIndex()][newState.getColIndex()] = newStateArr[newState.getRowIndex() + 1][newState.getColIndex()];
                        newStateArr[newState.getRowIndex() + 1][newState.getColIndex()] = State.SPACE;
                        return newState;

                    } else
                    {
                        return null;
                    }
                }
            };
            function.setName("MoveDownFunction");

            return function;
        }

        public AIFunction<State> getMoveRightFunction()
        {


            AIFunction<State> function = new AIFunction<State>()
            {
                @Override
                public State apply(State state)
                {
                    final int[][] oldStateArr = state.getState();
                    State newState = new State(oldStateArr);
                    int[][] newStateArr = newState.getState();

                    if (newState.getColIndex() != SIZE - 1)
                    {
                        newStateArr[newState.getRowIndex()][newState.getColIndex()] = newStateArr[newState.getRowIndex()][newState.getColIndex() + 1];
                        newStateArr[newState.getRowIndex()][newState.getColIndex() + 1] = State.SPACE;
                        return newState;

                    } else
                    {
                        return null;
                    }
                }
            };
            function.setName("MoveRightFunction");

            return function;
        }

        public AIFunction<State> getMoveUpFunction()
        {


            AIFunction<State> function = new AIFunction<State>()
            {
                @Override
                public State apply(State state)
                {
                    final int[][] oldStateArr = state.getState();
                    State newState = new State(oldStateArr);
                    int[][] newStateArr = newState.getState();

                    if (newState.getRowIndex() != 0)
                    {
                        newStateArr[newState.getRowIndex()][newState.getColIndex()] = newStateArr[newState.getRowIndex() - 1][newState.getColIndex()];
                        newStateArr[newState.getRowIndex() - 1][newState.getColIndex()] = State.SPACE;
                        return newState;

                    } else
                    {
                        return null;
                    }
                }
            };
            function.setName("MoveUpFunction");

            return function;
        }

    }


}
