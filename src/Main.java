public class Main
{
    public static void main(String[] args)
    {
        int SIZE = 3;

        WhiteSpacePuzzle puzzle = new WhiteSpacePuzzle(SIZE);

        int[][] initState = new int[puzzle.getSize()][puzzle.getSize()];

        initState[0][0] = -1;
        initState[0][1] = 1;
        initState[0][2] = 2;
        initState[1][0] = 3;
        initState[1][1] = 4;
        initState[1][2] = 5;
        initState[2][0] = 6;
        initState[2][1] = 7;
        initState[2][2] = 8;

        puzzle.setInitialState(initState);

        Tree<WhiteSpacePuzzle.State> tree = new Tree<WhiteSpacePuzzle.State>(puzzle.getInitState(), puzzle.getOperatorManager().getOperators());
        tree.startBuilding();


    }
}
