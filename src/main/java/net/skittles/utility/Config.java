package net.skittles.utility;

import net.skittles.Main;
import net.skittles.core.Engine;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

public class Config
{
    private Map<String, Object> data;
    private final String name;

    public Config(String name)
    {
        this.name = name;
        this.load();
    }

    private void load()
    {
        try (var stream = Main.class.getResourceAsStream("/Config/" + this.name + ".yaml"))
        {
            Yaml yaml = new Yaml();
            this.data = yaml.load(stream);
        }
        catch (IOException e)
        {
            Engine.logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public <T> T getValue(String id, Class<T> type)
    {
        Object object = getRawValue(id);

        if (!type.isInstance(object))
        {
            throw new ClassCastException("Expected " + type.getName() + " but got " + object.getClass().getName());
        }

        return type.cast(object);
    }

    public <T> ArrayList<T> getList(String id, Class<T> type)
    {
        Object object = getRawValue(id);

        if (!(object instanceof ArrayList<?> list))
        {
            throw new ClassCastException("Expected a List for key: " + id);
        }

        for (Object element : list)
        {
            if (!type.isInstance(element))
            {
                throw new ClassCastException("List element expected " + type.getName()
                        + " but got " + element.getClass().getName() + " for key: " + id);
            }
        }

        @SuppressWarnings("unchecked")
        ArrayList<T> typed = (ArrayList<T>) list;
        return typed;
    }

    private Object getRawValue(String id)
    {
        String[] idSplit = id.split("\\.");
        Object object = null;
        Map<String, Object> map = this.data;

        for (String s : idSplit)
        {
            if (map.containsKey(s))
            {
                if (map.get(s) instanceof Map<?, ?> subMap)
                {
                    map = castStringMap(subMap);
                }
                else
                {
                    object = map.get(s);
                    break;
                }
                continue;
            }

            break;
        }

        return object;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> castStringMap(Map<?, ?> map)
    {
        return (Map<String, Object>) map;
    }
}
