package AI;

import AI.AbstractState;
import WhiteSpacePuzzle.WhiteSpacePuzzle;

import java.util.function.Function;

public abstract class AIFunction<T extends AbstractState> implements Function<T, T>
{
    private String name;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
