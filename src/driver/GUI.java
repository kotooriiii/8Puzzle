package driver;

import AI.AITree;
import WhiteSpacePuzzle.WhiteSpacePuzzle;
import algorithms.AStarManhattanCostAlgorithm;
import algorithms.AStarMisplacedTileCostAlgorithm;
import algorithms.Algorithm;
import algorithms.UniformCostSearch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class GUI
{
    //The state which we keep updating for input
    int[][] initialState;

    //Current index and count of initial state
    private int count, row, col;

    //Size of puzzle
    private int SIZE;

    //Scanning keyboard input
    Scanner scanner;

    /**
     * The size of the puzzle.
     * @param size size of puzzle. size*size board
     */
    public GUI(int size)
    {
        this.SIZE = size;
        this.initialState = new int[size][size];
        this.scanner = new Scanner(System.in);


        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                initialState[i][j] = -1; //initialize to negative number for better display
            }
        }
    }

    /**
     * Sends the menu to the user
     */
    public void sendMenu()
    {

        //Complete the initial state
        while (count <= 8)
        {
            System.out.println("-- Type Your 8 Puzzle --");

            System.out.println(toString());
            final int input = getInput();
            sendInput(input);
            row = count / SIZE;
            col = count % SIZE;
        }


        //Set the initial and goal states.
        WhiteSpacePuzzle puzzle = new WhiteSpacePuzzle(SIZE);
        puzzle.setInitialState(initialState);
        puzzle.setDefaultGoalState();

        //Choose algorithm
        final Algorithm algorithm = getAlgorithmInput(puzzle);

        //Create the AI tree given the algorithms we are using.
        AITree<WhiteSpacePuzzle.State> tree = new AITree<WhiteSpacePuzzle.State>(puzzle.getInitState(), puzzle.getGoalState(), algorithm, puzzle.getOperatorManager().getOperators());

        System.out.println("\n\nStarting to compute...\n\n");

        final AITree<WhiteSpacePuzzle.State>.Node solution = tree.findSolution();

        //Print solution and a text file
        if (solution != null)
        {
            System.out.println("Solution found using " + algorithm.getClass().getSimpleName() + "!!!\n");
            System.out.println("Amount of Moves: " + solution.getMoves() + "\n");
            System.out.println(solution);
            FileWriter fw = null;
            try
            {
                File file = new File("solutions");
                file.mkdir();
                fw = new FileWriter("solutions/" + puzzle.getClass().getSimpleName() + "-" + algorithm.getClass().getSimpleName() + ".txt", false);
                fw.write(solution.toString());
                fw.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }


        }

        scanner.close();

    }

    /**
     * Chooses the algorithm to compute the puzzle
     * @param puzzle
     * @return
     */
    public Algorithm getAlgorithmInput(WhiteSpacePuzzle puzzle)
    {
        int algorithmChosen = -1;
        while(algorithmChosen < 0 || algorithmChosen > 2)
        {
            System.out.println("-- Algorithm to Choose Menu -- ");
            System.out.println("[0]\t\tA* Manhattan Cost");
            System.out.println("[1]\t\tA* Misplaced Tile Cost");
            System.out.println("[2]\t\tUniform Cost");
            algorithmChosen = scanner.nextInt();
        }

        switch (algorithmChosen)
        {
            case 0:
                return new AStarManhattanCostAlgorithm(puzzle);
            case 1:
                return new AStarMisplacedTileCostAlgorithm(puzzle);
            case 2:
                return new UniformCostSearch(puzzle);
            default:
                return null;
        }
    }

    /**
     * Gets the next input for the next index in the initial state
     * @return
     */
    public int getInput()
    {
        System.out.println("'_'\tmeans numbers that have not yet been typed.");
        System.out.println("'#'\tis the current number you are inputting.\n");
        System.out.println("Type in a number (0 represents white-space): ");

        final int i = scanner.nextInt();

        count++;
        return i;
    }

    /**
     * Sets the input to the initial state.
     * @param input The number to add
     */
    public void sendInput(int input)
    {
        initialState[row][col] = input;

    }

    /**
     * Displays the puzzle board
     * @return
     */
    @Override
    public String toString()
    {

        String s = "";
        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                if (i == row && j == col)
                {
                    s += "# ";
                    continue;
                }

                if (initialState[i][j] >= 0)
                {
                    s += initialState[i][j] + " ";

                } else
                {
                    s += "_ ";
                }
            }
            s += "\n";
        }

        if (s.isEmpty())
            return s;
        else
            return s.substring(0, s.length() - 1);

    }
}
