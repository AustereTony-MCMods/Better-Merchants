package austeretony.better_merchants.client.command;

import austeretony.better_merchants.client.ClientReference;
import austeretony.better_merchants.client.MerchantsManagerClient;
import austeretony.better_merchants.client.gui.CurrencyHelper;
import austeretony.better_merchants.client.gui.settings.GUISettings;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

public class CommandBetterMerchantsClient extends CommandBase {

    @Override
    public String getName() {
        return "bmclient";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/bmclient <reload-settings, reset-data, load-currency>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0 || args.length > 1)
            throw new WrongUsageException(this.getUsage(sender));   
        if (args[0].equals("reload-settings")) {
            GUISettings.instance().loadSettings();
            ClientReference.showMessage("better_merchants.command.reloadSettings");
        } else if (args[0].equals("reset-data")) {
            MerchantsManagerClient.instance().reset();
            ClientReference.showMessage("better_merchants.command.resetData");
        } else if (args[0].equals("load-currency")) {
            CurrencyHelper.instance().load();
            ClientReference.showMessage("better_merchants.command.loadCurrency");
        } else
            throw new WrongUsageException(this.getUsage(sender));   
    }
}