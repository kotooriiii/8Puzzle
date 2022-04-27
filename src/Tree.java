import java.util.*;
import java.util.function.Function;

public class Tree<T extends Comparable>
{
    private Node root;
    private List<Function<T, T>> operators;
    private Queue<Node> frontier;
    private HashSet<Node> visitedNodes;

    private class Node
    {
        private T data;
        private Node parent;
        private List<Node> children;

        public Node(Node parent, T data)
        {
            this.parent = parent;
            this.data = data;
            this.children = new ArrayList<>();
        }

        public Node(T data)
        {
            this.data = data;
            this.children = new ArrayList<>();
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
            return data.toString();
        }
    }

    public Tree(T rootData, List<Function<T, T>> operators)
    {
        root = new Node(rootData);
        this.operators = operators;
        this.frontier = new LinkedList<>();
        this.visitedNodes = new HashSet<>();
        this.frontier.add(root);
    }

    public void startBuilding()
    {
        while(!frontier.isEmpty())
        {
            final Node poll = frontier.poll();

            if(visitedNodes.contains(poll))
            {
                continue;
            }

            consumeOperator(poll);
            visitedNodes.add(poll);

            System.out.println(poll);
        }

    }

    private void consumeOperator(Node node)
    {
        for (Function<T, T> operator : operators)
        {
            final T childData = operator.apply(node.data);
            final Node childAdded = add(node, childData);
            if (childAdded != null)
            {
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


}
