package AI;

import algorithms.Algorithm;

import java.util.*;

/**
 * Defines the AI Tree to search nodes (and use an algorithm to choose what frontier node to expand)
 *
 * @param <T>
 */
public class AITree<T extends AbstractState>
{
    // The initial root node
    private Node root;

    //The algorithm function which decides what node to expand/explore.
    private Algorithm algorithm;

    //The list of operators for this tree.
    private HashMap<AIFunction<T>, Float> operators;
    //The list of nodes that are ready to expand/check if goal state.
    private Queue<Node> frontier;
    //The list of nodes that we have visited so we don't make an infinite loop.
    private HashSet<Node> visitedNodes;
    //The amount of nodes we have visited (counting duplicates!)
    private int visitedNumber = 0;
    //The solution node. We need this as a node so we can check what operators/states we made to get here.
    private Node solution = null;

    /**
     * A subclass of Tree to define what a node is (a container for the state).
     */
    public class Node
    {
        //The state
        private T data;
        //The parent of this node
        private Node parent;
        //The operator that was used to reach this state
        private Map.Entry<AIFunction<T>, Float> getToThisStateOperator;
        //The list of children of this node
        private List<Node> children;

        //The amount of moves that were needed to get to this state
        private int moves = 0;

        //The operator costs (cumulative)
        private float operatorCost = 0;


        /**
         * A constructor that creates a (child) node given you have a parent node and the state data.
         *
         * @param parent The parent of *this* node.
         * @param data   The state data
         */
        public Node(Node parent, T data)
        {
            this.parent = parent;
            this.data = data;
            this.children = new ArrayList<>();
        }

        /**
         * A constructor that creates the root node given you have the state data.
         *
         * @param data The state data
         */
        public Node(T data)
        {
            this.parent = null;
            this.data = data;
            this.children = new ArrayList<>();
        }

        /**
         * Gets the state data of this node
         *
         * @return state data of this node
         */
        public T getData()
        {
            return data;
        }

        /**
         * Gets the parent of this node
         *
         * @return parent node
         */
        public Node getParent()
        {
            return parent;
        }

        /**
         * Gets the amount of moves needed to reach this state.
         *
         * @return a positive integer that represents the amount of moves needed to reach *this* node.
         */
        public int getMoves()
        {
            return moves;
        }

        /**
         * Adds a move to the counter.
         */
        public void addMove()
        {
            this.moves = getParent().getMoves() + 1;
        }


        /**
         * The G(n) cumulative cost of operators
         *
         * @return
         */
        public float getGCost()
        {
            return operatorCost;
        }

        /**
         * The cost of this operator
         */
        public void addGCost()
        {
            this.operatorCost = this.parent.getGCost() + getToThisStateOperator.getValue();
        }

        /**
         * Gets the operator that was used to reach this node.
         *
         * @return A Map.Entry object that has the key as the function and the value as the cost of the operator.
         */
        public Map.Entry<AIFunction<T>, Float> getOperatorNeededToReachThis()
        {
            return getToThisStateOperator;
        }

        /**
         * Checks if this node is the root node.
         *
         * @return true if the node is the root node, false otherwise
         */
        public boolean isRoot()
        {
            return parent == null;
        }

        /**
         * Checks if this node is te leaf node (no children).
         *
         * @return true if the node is a leaf node, false otherwise
         */
        public boolean isLeaf()
        {
            return children.size() == 0;
        }

        /**
         * Returns a path from the initial node to this node.
         *
         * @return a String object that contains a path from initial node to this node.
         */
        @Override
        public String toString()
        {
            String path = "Path from Initial to Solution:\n\n";

            Node current = this;

            if (current.getToThisStateOperator == null) //Return early if this is already the solution!
            {
                return path + "No operators needed.";
            }

            Stack<String> stack = new Stack<>(); //stack needed to reverse order from string. if queue, then this node to initial state.


            final String firstOperatorName = current.getOperatorNeededToReachThis().getKey().getName(); //Gets the operator name that was needed to reach this state

            stack.push(firstOperatorName + "\n" + current.data.toString() + "\n\n"); //Push to stack the operator and the current state

            Node parent = current.getParent(); //Get the parent of the current.

            //Loop, If has parent
            while (parent != null)
            {

                String part = "";

                String operatorName = "";


                if (parent.getOperatorNeededToReachThis() == null) //Initial State condition
                {
                    operatorName = "Initial State (no operator)";
                } else //Loops middle nodes and grabs operator name and parent states
                {
                    operatorName = parent.getOperatorNeededToReachThis().getKey().getName();
                }
                part += operatorName + "\n" + parent.data.toString() + "\n\n";

                stack.push(part);


                parent = parent.getParent();
            }

            while (!stack.isEmpty()) //make a string from popping the stack
            {
                path += stack.pop();
            }

            return path;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj != null && obj instanceof AITree.Node)
            {
                return this.data.equals(((Node) obj).data);
            }
            return false;
        }
    }

    /**
     * Creates an AI Tree that traverses nodes given an algorithm.
     *
     * @param initialState The initial state to start
     * @param algorithm    An algorithm like (A* or Uniform Cost Search) that chooses which node in the frontier to expand.
     * @param operators    A collection of operators with their cost
     */
    public AITree(T initialState, Algorithm algorithm, HashMap<AIFunction<T>, Float> operators)
    {
        this.root = new Node(initialState);

        this.algorithm = algorithm;

        this.operators = operators;
        this.frontier = new PriorityQueue<Node>(algorithm);
        this.frontier.add(root);
        this.visitedNodes = new HashSet<>();
    }

    /**
     * Finds the solution starting from the initial node (root).
     *
     * @return a Node object that represents the goal node, otherwise returns null if no solution was found.
     */
    public Node findSolution()
    {

        System.out.println("Searching for state below:\n\n");
        System.out.println(root.data);
        System.out.println();
        System.out.println();

        //Keep looping until the frontier is empty
        while (!frontier.isEmpty())
        {
            //Remove the next node in the frontier (remember this is a priority queue so our algorithm already took care of this!)
            final Node poll = frontier.poll();

            visitedNumber++;

            //If the node is already visited, skip it
            if (visitedNodes.contains(poll))
            {
                continue;
            }

            // If the next node is the goal state
            if (poll.data.isGoalState())
            {
                //If the solution hasnt been found, then update. Otherwise, if a solution is found and has LESS moves, then choose it.
                if (solution == null || poll.getMoves() < solution.getMoves())
                    this.solution = poll;
            }


            consumeOperator(poll);
            visitedNodes.add(poll);
        }
        return solution;
    }

    /**
     * Creates the next following states (by the operators) from the given Node object in arguments.
     *
     * @param node The node to expand with operators
     */
    private void consumeOperator(Node node)
    {

        //If a solution has been found, stop expanding nodes.
        if (solution != null)
            return;

        //Use all operators to expand a node
        for (Map.Entry<AIFunction<T>, Float> operator : operators.entrySet())
        {
            final AIFunction<T> key = operator.getKey();

            final T childData = key.apply(node.data);


            //Add the child node and attach its parents, increase a move, and set the operator needed to move to this node.
            final Node childAdded = add(node, childData);
            if (childAdded != null)
            {
                childAdded.getToThisStateOperator = operator;
                childAdded.addMove();
                childAdded.addGCost();

                frontier.add(childAdded);
            }
        }
    }

    /**
     * Adds a child node to the supplied Node object.
     *
     * @param current The parent node
     * @param data    The new state data
     * @return
     */
    private Node add(Node current, T data)
    {
        assert current != null;
        if (data == null)
            return null;
        Node child = new Node(current, data);
        current.children.add(child);

        return child;
    }

    /**
     * Returns the algorithm that was used to choose the next node.
     *
     * @return
     */
    public Algorithm getAlgorithm()
    {
        return algorithm;
    }

    /**
     * Counts the amount of nodes we've traversed (counting duplicates)
     * @return # of visited nodes
     */
    public int getVisitedNumber()
    {
        return visitedNumber;
    }

    public HashSet<Node> getVisitedNodes()
    {
        return visitedNodes;
    }
}
