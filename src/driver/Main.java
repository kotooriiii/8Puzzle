package driver;

import AI.AITree;
import WhiteSpacePuzzle.WhiteSpacePuzzle;
import algorithms.UniformCostSearch;

import java.io.FileWriter;
import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        int SIZE = 3;


        WhiteSpacePuzzle puzzle = new WhiteSpacePuzzle(SIZE);

        int[][] initState = new int[puzzle.getSize()][puzzle.getSize()];

        initState[0][0] = 1;
        initState[0][1] = 2;
        initState[0][2] = 5;
        initState[1][0] = 3;
        initState[1][1] = 4;
        initState[1][2] = 0;
        initState[2][0] = 6;
        initState[2][1] = 7;
        initState[2][2] = 8;

        puzzle.setInitialState(initState);
        puzzle.setDefaultGoalState();

        AITree<WhiteSpacePuzzle.State> tree = new AITree<WhiteSpacePuzzle.State>(puzzle.getInitState(), puzzle.getGoalState(), new UniformCostSearch(puzzle), puzzle.getOperatorManager().getOperators());
        final AITree<WhiteSpacePuzzle.State>.Node solution = tree.findSolution();
        if(solution != null)
        {
            System.out.println("Solution found!!!\n");
            System.out.println("Amount of Moves: " + solution.getMoves() + "\n");
            System.out.println(solution);
            FileWriter fw = new FileWriter("solution.txt", false);
            fw.write(solution.toString());
            fw.close();

        }





    }
}
