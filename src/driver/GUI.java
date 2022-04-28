package driver;

import AI.AITree;
import WhiteSpacePuzzle.WhiteSpacePuzzle;
import algorithms.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class GUI
{
    //The state which we keep updating for input
    int[][] initialState;
    int[][] goalState;


    //Current index and count of initial state
    private int count, row, col;

    //Size of puzzle
    private int SIZE;

    //Scanning keyboard input
    Scanner scanner;

    /**
     * The size of the puzzle.
     *
     * @param size size of puzzle. size*size board
     */
    public GUI(int size)
    {
        this.SIZE = size;
        this.initialState = new int[size][size];
        this.goalState = new int[size][size];
        this.scanner = new Scanner(System.in);


        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                initialState[i][j] = -1; //initialize to negative number for better display
                goalState[i][j] = -1;

            }
        }
    }

    public String getBasicInitialState(int[][] initialState)
    {
        String s = "";
        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                s += initialState[i][j];
            }

        }
        return s;
    }

    private void populateState(int[][] state)
    {
        //Complete the initial state
        count = 0;
        row = 0;
        col = 0;
        while (count <= 8)
        {
            System.out.println("-- Type Your 8 Puzzle --");

            System.out.println(toString(state));
            final int input = getInput();
            sendInput(state, input);
            row = count / SIZE;
            col = count % SIZE;
        }

        clearScreen();
    }

    private void goalAsk(WhiteSpacePuzzle puzzle)
    {

        System.out.println("--Goal State Below--");
        System.out.println(puzzle.getGoalState().toString());
        System.out.println("Do you want the default goal state? ('1' for yes OR '0' for no): ");

        final int input = getInput(false);
        if(input == 1)
        {
        }
        else
        {
            populateState(goalState);
            puzzle.setGoalState(goalState);

        }
    }

    /**
     * Sends the menu to the user
     */
    public void sendMenu()
    {


        populateState(initialState);

        //Set the initial and goal states.
        WhiteSpacePuzzle puzzle = new WhiteSpacePuzzle(SIZE);
        puzzle.setInitialState(initialState);
        puzzle.setDefaultGoalState();

        goalAsk(puzzle);



clearScreen();

        //Choose algorithm
        final Algorithm algorithm = getAlgorithmInput(puzzle);

        //Create the AI tree given the algorithms we are using.
        AITree<WhiteSpacePuzzle.State> tree = new AITree<WhiteSpacePuzzle.State>(puzzle.getInitState(), algorithm, puzzle.getOperatorManager().getOperators());

        clearScreen();

        System.out.println("\n\nStarting to compute...\n\n");

        final AITree<WhiteSpacePuzzle.State>.Node solution = tree.findSolution();

        //Print solution and a text file
        if (solution != null)
        {
            System.out.println("Solution found using " + algorithm.getClass().getSimpleName() + "!!!\n");
            System.out.println("Size of Explored Set (# of visited nodes): " + tree.getVisitedNodes().size() + "\n");
            System.out.println("Total # of visited nodes (counting duplicates): " + tree.getVisitedNumber() + "\n");
            System.out.println("Amount of Moves (depth): " + solution.getMoves() + "\n");
            System.out.println(solution);
            FileWriter fw = null;
            try
            {
                File file = new File("solutions");
                file.mkdir();


                fw = new FileWriter("solutions/" + puzzle.getClass().getSimpleName() + "-" + algorithm.getClass().getSimpleName() + "-" + getBasicInitialState(initialState) + ".txt", false);

                fw.write("Size of Explored Set (# of visited nodes): " + tree.getVisitedNodes().size() + "\n");
                fw.write("Total # of visited nodes (counting duplicates): " + tree.getVisitedNumber() + "\n");
                fw.write("Amount of Moves (depth): " + solution.getMoves() + "\n" + solution.toString());
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
     *
     * @param puzzle
     * @return
     */
    public Algorithm getAlgorithmInput(WhiteSpacePuzzle puzzle)
    {
        int algorithmChosen = -1;
        while (algorithmChosen < 0 || algorithmChosen > 2)
        {
            System.out.println("-- Algorithm to Choose Menu -- ");
            System.out.println("[0]\t\tA* Manhattan Cost");
            System.out.println("[1]\t\tA* Euclidean Cost");
            System.out.println("[2]\t\tA* Misplaced Tile Cost");
            System.out.println("[3]\t\tUniform Cost");
            algorithmChosen = scanner.nextInt();
        }

        switch (algorithmChosen)
        {
            case 0:
                return new AStarManhattanCostAlgorithm(puzzle);
            case 1:
                return new AStarEuclideanCostAlgorithm(puzzle);
            case 2:
                return new AStarMisplacedTileCostAlgorithm(puzzle);
            case 3:
                return new UniformCostSearch(puzzle);
            default:
                return null;
        }
    }

    /**
     * Gets the next input for the next index in the initial state
     *
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

    public int getInput(boolean message)
    {
        if(message)
        {
            System.out.println("'_'\tmeans numbers that have not yet been typed.");
            System.out.println("'#'\tis the current number you are inputting.\n");
            System.out.println("Type in a number (0 represents white-space): ");
        }
        final int i = scanner.nextInt();

        count++;
        return i;
    }

    /**
     * Sets the input to the initial state.
     *
     * @param input The number to add
     */
    public void sendInput(int[][] state, int input)
    {
        state[row][col] = input;

    }

    /**
     * Displays the puzzle board
     *
     * @return
     */
    public String toString(int[][] state)
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

                if (state[i][j] >= 0)
                {
                    s += state[i][j] + " ";

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

    public static void clearScreen() {
        for(int i = 0; i < 50; i++)
        System.out.println();
    }
}
