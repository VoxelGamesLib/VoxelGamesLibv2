package com.voxelgameslib.voxelgameslib.components.scoreboard;

import com.google.common.base.Splitter;

import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

public class BukkitStringScoreboardLine extends StringScoreboardLine {

    private Team team;
    private String entry = "";

    public BukkitStringScoreboardLine(String value, Team team) {
        super(value);
        this.team = team;
        setValue(value);
    }

    public String getScore() {
        return entry;
    }

    public String setScore(int score) {
        return entry = ChatColor.values()[score].toString();
    }

    @Override
    // thanks to this random gist https://gist.github.com/mkotb/d99eccdcc78a43ffb707
    public void setValue(String value) {
        super.setValue(value);

        Iterator<String> iterator = Splitter.fixedLength(16).split(value).iterator();
        String prefix = iterator.next();

        team.setPrefix(prefix);

        if (!team.hasEntry(entry)) {
            team.addEntry(entry);
        }

        if (value.length() > 16) {
            String prefixColor = ChatColor.getLastColors(prefix);
            String suffix = iterator.next();

            if (prefix.endsWith(String.valueOf(ChatColor.COLOR_CHAR))) {
                prefix = prefix.substring(0, prefix.length() - 1);
                team.setPrefix(prefix);
                prefixColor = ChatColor.getByChar(suffix.charAt(0)).toString();
                suffix = suffix.substring(1);
            }

            if (prefixColor == null) {
                prefixColor = "";
            }

            if (suffix.length() > 15) {
                suffix = suffix.substring(0, (16 - Math.max(prefixColor.length(), ChatColor.RESET.toString()
                        .length()))); // cut off suffix, done if text is over 30 characters
            }

            team.setSuffix((prefixColor.equals("") ? ChatColor.RESET : prefixColor) + suffix);
        }
    }
}
