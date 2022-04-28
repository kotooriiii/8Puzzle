package algorithms;

import AI.AbstractState;
import WhiteSpacePuzzle.WhiteSpacePuzzle;
import driver.AITree;

public class UniformCostSearch<T extends AITree<AbstractState>.Node> extends Algorithm<T>
{
    public UniformCostSearch(WhiteSpacePuzzle puzzle)
    {
        super(puzzle);
    }

    @Override
    public int compare(T o1, T o2)
    {
        if (o1 != null && o2 != null)
        {
            AITree.Node state1 = (AITree.Node) o1;
            AITree.Node state2 = (AITree.Node) o2;


            return (int) (((float) state1.getGetToThisStateOperator().getValue()) - ((float) state2.getGetToThisStateOperator().getValue()));


        }

        throw new NullPointerException("Could not compare objects. Are they null or not an instance of AITree.Node?");
    }
}
