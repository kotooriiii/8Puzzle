package algorithms;

import AI.AbstractState;
import WhiteSpacePuzzle.WhiteSpacePuzzle;
import AI.AITree;

/**
 * An uninformed search algorithm (Dijikstra's Algorithm variant) using Uniform Cost.
 * @param <T> The AbstractNode which we need for comparisons (to choose what to expand next in the priority queue)
 */
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

            //definition of uniform cost. a higher number means the first object (o1) is larger. a lower number means the second object (o2) is larger. if the value is 0, the objects are equal.

            return (int) (((float) state1.getOperatorNeededToReachThis().getValue()) - ((float) state2.getOperatorNeededToReachThis().getValue()));


        }

        throw new NullPointerException("Could not compare objects. Are they null or not an instance of AITree.Node?");
    }
}
