package algorithms;

import AI.AbstractState;
import WhiteSpacePuzzle.WhiteSpacePuzzle;
import AI.AITree;

import java.util.Comparator;

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
