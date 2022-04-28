package algorithms;

import AI.AbstractState;
import WhiteSpacePuzzle.WhiteSpacePuzzle;
import AI.AITree;

import java.util.Comparator;

/**
 * An abstract class that defines what an Algorithm is.
 * @param <T>
 */
public abstract class Algorithm<T extends AITree<AbstractState>.Node> implements Comparator<T>
{

    private WhiteSpacePuzzle puzzle;

    public Algorithm(WhiteSpacePuzzle puzzle)
    {
        this.puzzle = puzzle;
    }

    public WhiteSpacePuzzle getPuzzle()
    {
        return puzzle;
    }
}
