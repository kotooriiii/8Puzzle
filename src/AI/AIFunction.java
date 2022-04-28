package AI;

import java.util.function.Function;

/**
 * AIFunction represents an Operator Function you can define. The same utility as java.util.function.Function but with the chance to set a name.
 * @param <T>
 */
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
