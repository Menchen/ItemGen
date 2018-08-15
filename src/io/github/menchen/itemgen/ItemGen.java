package io.github.menchen.itemgen;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemGen extends JavaPlugin {
	public String version;
	public String[] check = new String[2];
    private Material SpawnerMat;
    private NMSRefle Refle;
    public static Metrics metrics;

	@Override
	public void onEnable() {
        metrics = new Metrics(this);


		getLogger().info("ItemGen setuping.............. ");
        SpawnerMat = Material.values()[52];
		if (setupNMS()) {
			getLogger().info("Server version:" + version);
			getLogger().info("The plugin setup process is complete!");

		} else {
			getLogger().severe("Failed to setup ItemGen!");
			getLogger().severe("Your server version is not compatible with this plugin!");
			getLogger().severe("Server version:" + version);
			getLogger().severe("Debug info:");
			getLogger().severe((new StringBuilder()).append(ChatColor.DARK_PURPLE)
					.append("============================================").toString());
			getLogger().severe(
					(new StringBuilder()).append(ChatColor.DARK_PURPLE).append("Server version:" + version).toString());
			getLogger().severe((new StringBuilder()).append(ChatColor.DARK_PURPLE)
					.append("Plugin version:" + getDescription().getVersion()).toString());
			getLogger().severe((new StringBuilder()).append(ChatColor.DARK_PURPLE)
					.append("Author:" + getDescription().getAuthors()).toString());
			getLogger().severe((new StringBuilder()).append(ChatColor.UNDERLINE).append(ChatColor.AQUA)
					.append("Aks Dev if you are sure all is right :D").toString());
			getLogger().severe((new StringBuilder()).append(ChatColor.DARK_PURPLE)
					.append("============================================").toString());
			getLogger().severe("Try update plugin or your server to least version!!!");
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
		if (cmd.getName().equalsIgnoreCase("itemgen")) {
			if (sender instanceof ConsoleCommandSender) {
				sender.sendMessage("You need to be a player to use this command!");
				return true;
			}
			if (!sender.hasPermission("itemgen.use")) {
				sender.sendMessage((new StringBuilder()).append(ChatColor.RED)
						.append("You're not allowed to use this command!").toString());
				return true;
			}
			if (args.length > 3) {
				sender.sendMessage((new StringBuilder()).append(ChatColor.RED)
                        .append("Usage: /ig <Delay> <Player Range> <Spawn Range>").toString());
				return true;
			}
			int delay = -1;
            int range = 1;
            int spawnRange = 1;
			Player player = (Player) sender;
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("help")) {

					sender.sendMessage((new StringBuilder()).append(ChatColor.DARK_GREEN)
							.append("============================================").toString());
					sender.sendMessage((new StringBuilder()).append(ChatColor.BLUE)
                            .append("Usage: /ig          Use /ig to set spawner item data ").toString());
					sender.sendMessage((new StringBuilder()).append(ChatColor.BLUE)
                            .append("Usage: /ig <Delay> <Player Range> <Spawn Range>          Change spawner's delay, Player Range, and Spawner Range")
							.toString());
					sender.sendMessage((new StringBuilder()).append(ChatColor.BLUE)
                            .append("Usage: /ig give          Give you a mob spawner").toString());
					sender.sendMessage((new StringBuilder()).append(ChatColor.BLUE)
                            .append("Usage: /ig help          Display this message").toString());
					sender.sendMessage((new StringBuilder()).append(ChatColor.BLUE)
                            .append("Usage: /ig info          Display info message about this plugin").toString());
                    sender.sendMessage((new StringBuilder()).append(ChatColor.DARK_PURPLE)
							.append("**********************************************").toString());
					sender.sendMessage((new StringBuilder()).append(ChatColor.BLUE)
                            .append("Delay (seconds),this is the time needed for each item spawn.").toString());
					sender.sendMessage((new StringBuilder()).append(ChatColor.BLUE)
                            .append("Player Range (blocks), the radios that player need be for spawner to start spawning").toString());
					sender.sendMessage((new StringBuilder()).append(ChatColor.BLUE)
                            .append("Spawn Range (blocks), the radios that items will spawn in").toString());
					sender.sendMessage((new StringBuilder()).append(ChatColor.DARK_GREEN)
							.append("============================================").toString());
					return true;
				} else if (args[0].equalsIgnoreCase("info")) {
					sender.sendMessage((new StringBuilder()).append(ChatColor.DARK_PURPLE)
							.append("============================================").toString());
					sender.sendMessage((new StringBuilder()).append(ChatColor.DARK_PURPLE)
							.append("Server version:" + version).toString());
					sender.sendMessage((new StringBuilder()).append(ChatColor.DARK_PURPLE)
							.append("Plugin version:" + getDescription().getVersion()).toString());
					sender.sendMessage((new StringBuilder()).append(ChatColor.DARK_PURPLE)
							.append("Author:" + getDescription().getAuthors()).toString());
					sender.sendMessage((new StringBuilder()).append(ChatColor.UNDERLINE).append(ChatColor.AQUA)
							.append("Thank for using this plugin :D").toString());
					sender.sendMessage((new StringBuilder()).append(ChatColor.DARK_PURPLE)
							.append("============================================").toString());
					return true;
				}
				if (args[0].equalsIgnoreCase("give")) {
                    ItemStack item = new ItemStack(SpawnerMat);
					sender.sendMessage((new StringBuilder()).append(ChatColor.GREEN)
							.append("Given to " + sender.getName() + " a Mob Spawner.").toString());
					player.getInventory().addItem(item);
					return true;
				}
			}

			if (args.length >= 1) {
				try {
					delay = Integer.parseInt(args[0]);
				} catch (Exception e) {

					sender.sendMessage((new StringBuilder()).append(ChatColor.RED)
                            .append("Wrong Delay, must be int (seconds)").toString());
					return true;
				}
			}
			if (args.length >= 2) {
				try {
                    range = Integer.parseInt(args[1]);
				} catch (Exception e) {
					sender.sendMessage((new StringBuilder()).append(ChatColor.RED)
                            .append("Wrong Ranger, must be int (blocks)").toString());
					return true;
				}
			}
			if (args.length == 3) {
				try {
                    spawnRange = Integer.parseInt(args[2]);
				} catch (Exception e) {
					sender.sendMessage((new StringBuilder()).append(ChatColor.RED)
                            .append("Wrong SpawnRanger, must be int (blocks)").toString());
					return true;
				}
			}

			
			//Check player hand.
			if (delay != -1){
				if (player.getItemInHand().getType() == Material.AIR) {
					
					sender.sendMessage((new StringBuilder()).append(ChatColor.RED)
							.append("You need to be holding an item to use this command!").toString());
					return true;
				}
			}
			
			Block block = player.getWorld().getBlockAt(player.getLocation().getBlockX(),
					player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ());
            if (block == null || block.getType() != SpawnerMat) {
				sender.sendMessage((new StringBuilder()).append(ChatColor.GRAY)
						.append("Standing Material:" + block.getType()).toString());
				sender.sendMessage((new StringBuilder()).append(ChatColor.RED)
						.append("You need to be standing at a spawner to use this command!").toString());
				return true;
			}
            check = Refle.setspawner(block, player, delay, range, spawnRange);
            //}
            //else if (version.equals("v1_8_R3")) {
            //NMS18_R3 Setspawner18R3 = new NMS18_R3();
            //check = Setspawner18R3.setspawner(block, player, delay, range, spawnRange);
            //} else if (version.equals("v1_7_R3")) {
            //NMS17_R3 Setspawner17R3 = new NMS17_R3();
            //check = Setspawner17R3.setspawner(block, player, delay, range, spawnRange);
            //} else if (version.equals("v1_7_R4")) {
            //NMS17_R4 Setspawner17R4 = new NMS17_R4();
            //check = Setspawner17R4.setspawner(block, player, delay, range, spawnRange);
            //} else if (version.equals("v1_9_R1")){
            //NMS19_R1 Setspawner19R1 = new NMS19_R1();
            //check = Setspawner19R1.setspawner(block, player, delay, range, spawnRange);
            //} else {
            //sender.sendMessage(
            //(new StringBuilder()).append(ChatColor.RED).append("Not support version!!!").toString());
            //return true;
            //}
			if (check[1] == "false") {
				if (check[0] == null) {
					check[0] = "NO CHANGE";
				} else {
                    sender.sendMessage("");
                    sender.sendMessage("");
					sender.sendMessage((new StringBuilder()).append(ChatColor.DARK_GREEN)
							.append("============================================").toString());
					sender.sendMessage((new StringBuilder()).append(ChatColor.GREEN)
							.append("Properties were successfully edited!").toString());
					sender.sendMessage(
                            (new StringBuilder()).append(ChatColor.GREEN).append("ItemType: " + check[0]).toString());
					sender.sendMessage((new StringBuilder()).append(ChatColor.GREEN)
							.append("Delay was:" + delay + "s, " + delay * 20 + "ticks").toString());
					sender.sendMessage((new StringBuilder()).append(ChatColor.GREEN)
                            .append("Ranger was:" + range + "Block(s)").toString());
					sender.sendMessage((new StringBuilder()).append(ChatColor.GREEN)
                            .append("SpawnRanger was:" + spawnRange + "Block(s)").toString());
					if (delay == -1) {
						sender.sendMessage((new StringBuilder()).append(ChatColor.GREEN)
                                .append("Type /ig <Delay> <Player range> <SpawnRange> to configure!!!").toString());
					}
					sender.sendMessage((new StringBuilder()).append(ChatColor.DARK_GREEN)
							.append("============================================").toString());
				}
			} else {
				sender.sendMessage((new StringBuilder()).append(ChatColor.RED)
						.append("Something went wrong!,check the log to debug.").toString());
			}
			return true;
		} else {
			return false;
		}
	}

	private boolean setupNMS() {
		try {

			version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

		} catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException) {
			return false;
		}

		getLogger().info("Your server is running version " + version);
        //if (version.equals("v1_8_R3")) {
        //return true;
        //}else if(version.equals("v1_9_R1")){
        //return true;
        //} else if (version.equals("v1_7_R3")) {
        //return true;
        //} else if (version.equals("v1_7_R4")) {
        //return true;
		// server version that have NMS(Support)
        if (NMSRefle.Init()) {
            Refle = new NMSRefle();
			return true;
		}else{
            metrics.addCustomChart(new Metrics.SimplePie("notworkingversion", () -> version));
        }
		return false;

    }
}

