package dev.devous.vandium.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

public class ScoreboardEntry {

    private final Scoreboard scoreboard;
    private final String text, identifier;
    private Team team;

    public ScoreboardEntry(Scoreboard scoreboard, String text, int position) {
        this.scoreboard = scoreboard;
        this.text = text;
        this.identifier = scoreboard.getUniqueIdentifier(position);

        this.setup();
    }

    public void setup() {
        final org.bukkit.scoreboard.Scoreboard scoreboard = this.scoreboard.getScoreboard();

        if (scoreboard == null) {
            return;
        }

        String teamName = identifier;

        if (teamName.length() > 16)
            teamName = teamName.substring(0, 16);

        Team team = scoreboard.getTeam(teamName);

        if (team == null)
            team = scoreboard.registerNewTeam(teamName);

        if (team.getEntries() == null || team.getEntries().isEmpty() || !team.getEntries().contains(identifier))
            team.addEntry(identifier);

        this.scoreboard.addEntry(this);

        this.team = team;
    }

    public void send(int position) {
        if (text.length() > 16) {
            String prefix = text.substring(0, 16);
            String suffix;

            if (prefix.charAt(15) == ChatColor.COLOR_CHAR) {
                prefix = prefix.substring(0, 15);
                suffix = text.substring(15);

            } else if (prefix.charAt(14) == ChatColor.COLOR_CHAR) {
                prefix = prefix.substring(0, 14);
                suffix = text.substring(14);

            } else {
                if (ChatColor.getLastColors(prefix).equalsIgnoreCase(ChatColor.getLastColors(identifier))) {
                    suffix = text.substring(16);
                } else {
                    suffix = ChatColor.getLastColors(prefix) + text.substring(16);
                }
            }

            if (suffix.length() > 16)
                suffix = suffix.substring(0, 16);

            team.setPrefix(prefix);
            team.setSuffix(suffix);

        } else {
            team.setPrefix(text);
            team.setSuffix("");
        }

        Score score = scoreboard.getObjective().getScore(identifier);
        score.setScore(position);
    }

    public void remove() {
        scoreboard.removeIdentifier(identifier);
        scoreboard.getScoreboard().resetScores(identifier);
    }

}
