package me.StevenLawson.TotalFreedomMod.Commands;

import me.StevenLawson.TotalFreedomMod.TFM_Admin;
import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
import me.StevenLawson.TotalFreedomMod.TFM_Util;
import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.SENIOR, source = SourceType.BOTH)
@CommandParameters(description = "System Managment", usage = "/<command> <list | clean | <add|del|info> <username> | test <on|off>>")
public class Command_sys extends TFM_Command
{
    @Override
    public boolean run(final CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {

               
        if (args.length == 1)
        {
            if (args[0].equals("list"))
            {
                playerMsg("Superadmins: " + StringUtils.join(TFM_AdminList.getSuperNames(), ", "), ChatColor.GOLD);
                return true;
            }

            else if (args[0].equals("clean"))
            {
                TFM_Util.adminAction(sender.getName(), "Cleaning superadmin list", true);
                TFM_AdminList.cleanSuperadminList(true);
                playerMsg("Superadmins: " + StringUtils.join(TFM_AdminList.getSuperNames(), ", "), ChatColor.YELLOW);
                return true;
            }

            else
            {
                return false;
            }
        }
        else if (args.length == 2)
        {
            if (args[0].equalsIgnoreCase("info"))
            {
                TFM_Admin superadmin = TFM_AdminList.getEntry(args[1].toLowerCase());

                if (superadmin == null)
                {
                    final Player player = getPlayer(args[1]);

                    if (player != null)
                    {
                        superadmin = TFM_AdminList.getEntry(player.getName().toLowerCase());
                    }
                }
                if (superadmin == null)
                {
                    playerMsg("Superadmin not found: " + args[1]);
                    return true;
                }

                playerMsg(superadmin.toString());
                return true;
            }

            if (args[0].equalsIgnoreCase("add"))
            {
            OfflinePlayer player = getPlayer(args[1]);

            if (player == null)
            {
                final TFM_Admin superadmin = TFM_AdminList.getEntry(args[1]);

                if (superadmin == null)
                {
                    playerMsg(TotalFreedomMod.PLAYER_NOT_FOUND);
                    return true;
                }

                player = Bukkit.getOfflinePlayer(superadmin.getLastLoginName());
            }

            TFM_Util.adminAction(sender.getName(), "Adding " + player.getName() + " to the superadmin list", true);
            TFM_AdminList.addSuperadmin(player);

            return true;
        }
            else if (args[0].equalsIgnoreCase("del"))
            {
            if (!TFM_AdminList.isSeniorAdmin(sender))
            {
                playerMsg(TotalFreedomMod.MSG_NO_PERMS);
                return true;
            }

            String targetName = args[1];


            final Player player = getPlayer(targetName);

            if (player != null)
            {
                targetName = player.getName();
            }

            if (!TFM_AdminList.getLowerSuperNames().contains(targetName.toLowerCase()))
            {
                playerMsg("Superadmin not found: " + targetName);
                return true;
            }

            TFM_Util.adminAction(sender.getName(), "Removing " + targetName + " from the superadmin list", true);
            TFM_AdminList.removeSuperadmin(Bukkit.getOfflinePlayer(targetName));
            return true;
        }
            else if (args[0].equalsIgnoreCase("test"))
            {
                if (args[1].equals("on"))
                {
                    TFM_Util.adminAction(ChatColor.RED + "WARNING: " + sender.getName(), "has started Testing on the server", false);
                }

                else if (args[1].equals("off"))
                {
                    TFM_Util.adminAction(ChatColor.RED + "WARNING: " + sender.getName(), "has succesfully ended Testing on the server", false);
                }

                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        return true;
    }
    }