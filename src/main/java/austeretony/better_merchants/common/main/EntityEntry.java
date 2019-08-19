package austeretony.better_merchants.common.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.UUID;

import austeretony.better_merchants.common.util.PacketBufferUtils;
import austeretony.better_merchants.common.util.StreamUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public class EntityEntry {

    public static final int 
    MAX_NAME_LENGTH = 20,
    MAX_PROFESSION_LENGTH = 20;

    public final long entryId, profileId;

    public final UUID entityUUID;

    public final String name, profession;

    public EntityEntry(UUID entityUUID, long entryId, long profileId, String name, String profession) {
        this.entityUUID = entityUUID;
        this.entryId = entryId;
        this.profileId = profileId;
        this.name = name;
        this.profession = profession;
    }

    public void write(BufferedOutputStream bos) throws IOException {
        StreamUtils.write(this.entityUUID, bos);
        StreamUtils.write(this.entryId, bos);
        StreamUtils.write(this.profileId, bos);
        StreamUtils.write(this.name, bos);
        StreamUtils.write(this.profession, bos);
    }

    public static EntityEntry read(BufferedInputStream bis) throws IOException {
        return new EntityEntry(StreamUtils.readUUID(bis), StreamUtils.readLong(bis), StreamUtils.readLong(bis), StreamUtils.readString(bis), StreamUtils.readString(bis));
    }

    public void write(PacketBuffer buffer) {
        PacketBufferUtils.writeUUID(this.entityUUID, buffer);
        buffer.writeLong(this.entryId);
        buffer.writeLong(this.profileId);
        PacketBufferUtils.writeString(this.name, buffer);
        PacketBufferUtils.writeString(this.profession, buffer);
    }

    public static EntityEntry read(PacketBuffer buffer) {
        return new EntityEntry(PacketBufferUtils.readUUID(buffer), buffer.readLong(), buffer.readLong(), PacketBufferUtils.readString(buffer), PacketBufferUtils.readString(buffer));
    }

    public NBTTagCompound toTagCompound() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setUniqueId("uuid", this.entityUUID);
        tag.setLong("e_id", this.entryId);
        tag.setLong("p_id", this.profileId);
        tag.setString("name", this.name);
        tag.setString("prof", this.profession);
        return tag;
    }

    public static EntityEntry fromTagCompound(NBTTagCompound tag) {
        return new EntityEntry(tag.getUniqueId("uuid"), tag.getLong("e_id"), tag.getLong("p_id"), tag.getString("name"), tag.getString("prof"));
    }
}
