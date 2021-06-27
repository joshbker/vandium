package dev.devous.vandium.scoreboard.adapter;

import org.bukkit.entity.Player;

import java.util.List;

public interface ScoreboardAdapter {

    String getTitle(Player player);

    List<String> getLines(Player player);

}
