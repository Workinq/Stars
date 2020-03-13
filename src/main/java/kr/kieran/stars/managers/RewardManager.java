package kr.kieran.stars.managers;

import kr.kieran.stars.StarsPlugin;
import kr.kieran.stars.objects.Reward;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RewardManager {

    private StarsPlugin plugin;
    private List<Reward> rewards;

    public RewardManager(StarsPlugin plugin)
    {
        this.plugin = plugin;
        this.rewards = new ArrayList<>();
        firstTime();
        registerRewards();
    }

    private void registerRewards()
    {

    }

    private void firstTime()
    {
        if (!plugin.getRewards().getBoolean("first")) return;
        rewards.add(new Reward(list("silkspawners give %player% silverfish 1", "msg %player% &aYou won a silverfish spawner from breaking the star!"), 0.03D));
        rewards.add(new Reward(list("silkspawners give %player% spider 1", "msg %player% &aYou won a spider spawner from breaking the star!"), 0.10D));
        rewards.add(new Reward(list("silkspawners give %player% zombie 1", "msg %player% &aYou won a zombie spawner from breaking the star!"), 0.10D));
        rewards.add(new Reward(list("silkspawners give %player% blaze 1", "msg %player% &aYou won a blaze spawner from breaking the star!"), 0.05D));
        rewards.add(new Reward(list("silkspawners give %player% creeper 1", "msg %player% &aYou won a creeper spawner from breaking the star!"), 0.03D));
        rewards.add(new Reward(list("silkspawners give %player% enderman 1", "msg %player% &aYou won an enderman spawner from breaking the star!"), 0.05D));
        rewards.add(new Reward(list("silkspawners give %player% villager 1", "msg %player% &aYou won a villager spawner from breaking the star!"), 0.03D));
        rewards.add(new Reward(list("silkspawners give %player% skeleton 1", "msg %player% &aYou won a skeleton spawner from breaking the star!"), 1.0D));
        saveRewards();
    }

    private void saveRewards()
    {
        List<String> rewards = new ArrayList<>();
        for (Reward reward : this.rewards)
        {
            rewards.add(reward.serialize());
        }
        plugin.getRewards().set("rewards", rewards);
        plugin.getRewards().set("first", false);
        plugin.saveRewards();
    }

    @SafeVarargs
    private final <T> List<T> list(T... items)
    {
        List<T> list = new ArrayList<>(items.length);
        Collections.addAll(list, items);
        return list;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

}
