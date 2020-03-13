package kr.kieran.stars.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Reward {

    private List<String> commands;
    private double chance;

    public Reward(List<String> commands, double chance)
    {
        this.commands = commands;
        this.chance = chance;
    }

    public List<String> getCommands() {
        return commands;
    }

    public double getChance() {
        return chance;
    }

    public String serialize()
    {
        StringBuilder simple = new StringBuilder();
        for (String command : commands)
        {
            simple.append(command).append(",");
        }
        return simple.substring(0, simple.length() - 1) + ":" + chance;
    }

    public static Reward deserialize(String string)
    {
        String[] split = string.split(":");
        List<String> commands = new ArrayList<>(Arrays.asList(split[0].split(",")));
        return new Reward(commands, Double.parseDouble(split[1]));
    }

}
