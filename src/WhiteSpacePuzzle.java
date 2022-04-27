import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

public class WhiteSpacePuzzle
{
    private int SIZE;
    private State initState;
    private OperatorManager operatorManager;

    private static State goalState;


    public WhiteSpacePuzzle(int size)
    {
        SIZE = size;
        this.operatorManager = new OperatorManager();
        setGoalState();
    }

    public int getSize()
    {
        return SIZE;
    }

    public void setInitialState(int[][] initState)
    {
        State state = new State(initState);
        this.initState = state;
    }

    public State getInitState()
    {
        return initState;
    }

    private void setGoalState()
    {

        int[][] localGoalState = new int[SIZE][SIZE];

        int counter = 1;
        for(int rows = 0; rows < SIZE; rows++)
        {
            for(int cols = 0; cols < SIZE; cols++)
            {
                localGoalState[rows][cols] = counter++;
            }
        }
        localGoalState[0][0] = State.SPACE;
        goalState = new State(localGoalState);
    }

    public OperatorManager getOperatorManager()
    {
        return operatorManager;
    }

    public class State implements Comparable
    {
        private int[][] state;

        int rowIndex = -1;
        int colIndex = -1;

        private static final int SPACE = -1;

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
        }

        public int[][] getState()
        {
            return state;
        }

        public void setState(int[][] state)
        {
            this.state = state;
            findSpaceIndexes();

        }

        public boolean isGoalState()
        {
            return this.state.equals(goalState);
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

        @Override
        public boolean equals(Object obj)
        {
            if(obj != null)
            {
                if(obj instanceof State)
                {
                    State otherState = (State) obj;

                    for(int i = 0; i < SIZE; i++)
                    {
                        for(int j = 0; j < SIZE; j++)
                        {
                            if(this.state[i][j] != otherState.state[i][j])
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

        @Override
        public int compareTo(Object o2)
        {


            if (o2 != null)
            {
                if (o2 instanceof State)
                {
                    State s1 = (State) this;
                    State s2 = (State) o2;


//todo do we need this?
                }


            }

            return 0;
        }
    }

    public class OperatorManager
    {
        private List<Function<State, State>> operators;

        public OperatorManager()
        {
            this.operators = new ArrayList<>();
            this.operators.add(getMoveLeftFunction());
            this.operators.add(getMoveDownFunction());
            this.operators.add(getMoveRightFunction());
            this.operators.add(getMoveUpFunction());
        }

        public List<Function<State, State>> getOperators()
        {
            return operators;
        }

        public Function<State, State> getMoveLeftFunction()
        {


            Function<State, State> function = new Function<State, State>()
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

            return function;
        }

        public Function<State, State> getMoveDownFunction()
        {


            Function<State, State> function = new Function<State, State>()
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

            return function;
        }

        public Function<State, State> getMoveRightFunction()
        {


            Function<State, State> function = new Function<State, State>()
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

            return function;
        }

        public Function<State, State> getMoveUpFunction()
        {


            Function<State, State> function = new Function<State, State>()
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

            return function;
        }

    }


}
