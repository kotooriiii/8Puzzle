package algorithms;

import AI.AITree;
import AI.AbstractState;
import WhiteSpacePuzzle.WhiteSpacePuzzle;

/**
 * An informed heuristic search algorithm using A* with the Euclidean Cost.
 *
 * @param <T> The AbstractNode which we need for comparisons (to choose what to expand next in the priority queue)
 */
public class AStarEuclideanCostAlgorithm<T extends AITree<AbstractState>.Node> extends Algorithm<T>
{

    public AStarEuclideanCostAlgorithm(WhiteSpacePuzzle puzzle)
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

            if (state1.getData() != null && state2.getData() != null)
            {
                WhiteSpacePuzzle.State data1 = (WhiteSpacePuzzle.State) state1.getData();
                WhiteSpacePuzzle.State data2 = (WhiteSpacePuzzle.State) state2.getData();

                //definition of manhattan cost. a higher number means the first object (o1) is larger. a lower number means the second object (o2) is larger. if the value is 0, the objects are equal.
                return (int) ((state1.getGCost() + data1.getEuclideanDistanceCost()) - (state2.getGCost() + data2.getEuclideanDistanceCost()));
            }

        }

        throw new NullPointerException("Could not compare objects. Are they null or not an instance of AITree.Node?");
    }
}
