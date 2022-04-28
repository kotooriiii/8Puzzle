package algorithms;

import AI.AbstractState;
import WhiteSpacePuzzle.WhiteSpacePuzzle;
import AI.AITree;

public class AStarMisplacedTileCostAlgorithm<T extends AITree<AbstractState>.Node> extends Algorithm<T>
{

    public AStarMisplacedTileCostAlgorithm(WhiteSpacePuzzle puzzle)
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

                return (int) (((float) state1.getGetToThisStateOperator().getValue()) + data1.getMisplacedTileCost() - ((float) state2.getGetToThisStateOperator().getValue()) + data2.getMisplacedTileCost());
            }

        }

        throw new NullPointerException("Could not compare objects. Are they null or not an instance of AITree.Node?");
    }
}
