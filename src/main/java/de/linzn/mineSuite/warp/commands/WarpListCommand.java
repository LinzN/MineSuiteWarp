package de.linzn.mineSuite.warp.commands;

import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import de.linzn.mineSuite.warp.Warpplugin;
import de.linzn.mineSuite.warp.database.WarpSqlActions;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WarpListCommand implements CommandExecutor {
    public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public WarpListCommand(Warpplugin instance) {
    }

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
        final Player player = (Player) sender;
        if (player.hasPermission("mineSuite.warp.warps")) {
            this.executorServiceCommands.submit(() -> {
                if (sender instanceof Player) {
                    int visible = 1;
                    if (player.hasPermission("mineSuite.bypass")) {
                        visible = 0;
                    }
                    HashMap<String, UUID> list = WarpSqlActions.getWarps(visible);
                    List<String> warpname = new ArrayList<>();

                    for (Map.Entry<String, UUID> s : list.entrySet()) {
                        warpname.add(s.getKey());
                    }

                    int pageNumb = 0;
                    try {
                        if (args.length == 1) {
                            int number = Integer.valueOf(args[0]);
                            if (number < 1) {
                                pageNumb = 0;
                            } else {
                                pageNumb = Integer.valueOf(args[0]) - 1;
                            }

                        } else {
                            pageNumb = 0;
                        }
                    } catch (Exception e) {
                        player.sendMessage("No number");
                        return;
                    }
                    // int counter = pageNumb * 10 + 1;
                    int rgCount = list.size();
                    if ((pageNumb * 6 + 1) > rgCount) {
                        sender.sendMessage("So viele Seiten für Warps gibt es nicht!");
                        return;
                    }
                    Collections.sort(warpname);
                    sender.sendMessage("§aDie Warpliste von MineGaming");
                    sender.sendMessage("§9Warpname?   §dBesitzer? ");
                    int counter = 1;
                    List<String> warplist = warpname.subList(pageNumb * 6,
                            pageNumb * 6 + 6 > rgCount ? rgCount : pageNumb * 6 + 6);
                    for (String wl : warplist) {
                        sender.sendMessage("§aWName: §9" + wl + " §agehört §d"
                                + Bukkit.getOfflinePlayer(list.get(wl)).getName());
                        counter++;
                    }

                    if (counter >= 7) {

                        int pageSeite;
                        if (pageNumb == 0) {
                            pageSeite = 2;
                        } else {
                            pageSeite = (pageNumb + 2);
                        }
                        sender.sendMessage("§aMehr auf Seite §e" + pageSeite + " §amit §e/warps " + pageSeite);
                    }

                    if (counter <= 6 && pageNumb != 0) {
                        int pageSeite = (pageNumb);

                        sender.sendMessage("§aZurück auf Seite §e" + pageSeite + "§a mit §e/warps " + pageSeite);
                    }

                }
            });
        } else {
            player.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.NO_PERMISSIONS);
        }
        return false;
    }
}