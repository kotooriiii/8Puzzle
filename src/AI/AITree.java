package AI;

import algorithms.Algorithm;

import java.util.*;

public class AITree<T extends AbstractState>
{
    private Node root;
    private T goalState;

    private Algorithm algorithm;

    private HashMap<AIFunction<T>, Float> operators;
    private Queue<Node> frontier;
    private HashSet<Node> visitedNodes;

    private Node solution = null;

    public class Node
    {
        private T data;
        private Node parent;
        private Map.Entry<AIFunction<T>, Float> getToThisStateOperator;
        private List<Node> children;

        private int moves = 0;

        public Node(Node parent, T data)
        {
            this.parent = parent;
            this.data = data;
            this.children = new ArrayList<>();
        }

        public Node(T data)
        {
            this.parent = null;
            this.data = data;
            this.children = new ArrayList<>();
        }

        public T getData()
        {
            return data;
        }

        public Node getParent()
        {
            return parent;
        }

        public int getMoves()
        {
            return moves;
        }

        public void addMove()
        {
            this.moves = getParent().getMoves() + 1;
        }

        public Map.Entry<AIFunction<T>, Float> getGetToThisStateOperator()
        {
            return getToThisStateOperator;
        }

        public boolean isRoot()
        {
            return parent == null;
        }

        public boolean isLeaf()
        {
            return children.size() == 0;
        }

        @Override
        public String toString()
        {
            String path = "Path from Initial to Solution:\n\n";

            Node current = this;

            if (current.getToThisStateOperator == null)
            {
                return path + "No operators needed.";
            }

            Stack<String> stack = new Stack<>();


            final String firstOperatorName = current.getGetToThisStateOperator().getKey().getName();

            stack.push(firstOperatorName + "\n" + current.data.toString() + "\n\n");

            Node parent = current.getParent();


            while (parent != null)
            {

                String part = "";

                String operatorName = "";


                if (parent.getGetToThisStateOperator() == null)
                {
                    operatorName = "Initial State (no operator)";
                } else
                {
                    operatorName = parent.getGetToThisStateOperator().getKey().getName();
                }
                part += operatorName + "\n" + parent.data.toString() + "\n\n";

                stack.push(part);


                parent = parent.getParent();
            }

            while (!stack.isEmpty())
            {
                path += stack.pop();
            }

            return path;
        }
    }

    public AITree(T initialState, T goalState, Algorithm algorithm, HashMap<AIFunction<T>, Float> operators)
    {
        this.root = new Node(initialState);
        this.goalState = goalState;

        this.algorithm = algorithm;

        this.operators = operators;
        this.frontier = new PriorityQueue<Node>(algorithm);
        this.frontier.add(root);
        this.visitedNodes = new HashSet<>();
    }

    public Node findSolution()
    {
        while (!frontier.isEmpty())
        {
            final Node poll = frontier.poll();

            if (visitedNodes.contains(poll))
            {
                continue;
            }

            if (poll.data.equals(goalState))
            {
                if (solution == null || poll.getMoves() < solution.getMoves())
                    this.solution = poll;
            }


            consumeOperator(poll);
            visitedNodes.add(poll);
        }
        return solution;
    }

    private void consumeOperator(Node node)
    {

        if (solution != null)
            return;

        for (Map.Entry<AIFunction<T>, Float> operator : operators.entrySet())
        {
            final AIFunction<T> key = operator.getKey();

            final T childData = key.apply(node.data);
            final Node childAdded = add(node, childData);
            if (childAdded != null)
            {
                childAdded.getToThisStateOperator = operator;
                childAdded.addMove();

                frontier.add(childAdded);
            }
        }
    }

    private Node add(Node current, T data)
    {
        assert current != null;
        if (data == null)
            return null;
        Node child = new Node(current, data);
        current.children.add(child);

        return child;
    }

    public Algorithm getAlgorithm()
    {
        return algorithm;
    }


}
