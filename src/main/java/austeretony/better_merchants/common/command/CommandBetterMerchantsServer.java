package austeretony.better_merchants.common.command;

import austeretony.better_merchants.common.CommonReference;
import austeretony.better_merchants.common.main.BetterMerchantsMain;
import austeretony.better_merchants.common.network.client.CPSyncValidProfileIds;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandBetterMerchantsServer extends CommandBase {

    @Override
    public String getName() {
        return "bmserver";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/bmserver management";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender instanceof EntityPlayerMP && CommonReference.isOpped((EntityPlayerMP) sender);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0 || args.length > 1)
            throw new WrongUsageException(this.getUsage(sender));   
        if (args[0].equals("management")) {
            BetterMerchantsMain.network().sendTo(new CPSyncValidProfileIds(), getCommandSenderAsPlayer(sender));
        } else
            throw new WrongUsageException(this.getUsage(sender));   
    }
}