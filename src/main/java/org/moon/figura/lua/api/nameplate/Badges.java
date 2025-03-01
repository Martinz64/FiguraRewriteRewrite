package org.moon.figura.lua.api.nameplate;

import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.moon.figura.FiguraMod;
import org.moon.figura.avatars.Avatar;
import org.moon.figura.config.Config;
import org.moon.figura.utils.ColorUtils;
import org.moon.figura.utils.FiguraText;
import org.moon.figura.utils.TextUtils;

import java.util.BitSet;
import java.util.HashMap;
import java.util.UUID;

public class Badges {

    private static final HashMap<UUID, Pair<BitSet, BitSet>> badgesMap = new HashMap<>();

    public static Component fetchBadges(Avatar avatar) {
        if (avatar == null)
            return Component.empty();

        MutableComponent badges = Component.literal(" ");

        UUID id = avatar.owner;
        Pair<BitSet, BitSet> pair = badgesMap.get(id);
        if (pair == null)
            badgesMap.put(id, pair = empty());

        // -- loading -- //

        if (!avatar.loaded)
            return badges.append(Component.literal(Integer.toHexString(Math.abs(FiguraMod.ticks) % 16)).withStyle(Style.EMPTY.withFont(TextUtils.FIGURA_FONT)));

        // -- mark -- //

        Pride[] pride = Pride.values();

        //error
        if (avatar.scriptError)
            badges.append(Default.ERROR.badge);

        //version
        else if (avatar.versionStatus == 1)
            badges.append(Default.WARNING.badge);

        //egg
        else if (FiguraMod.CHEESE_DAY && Config.EASTER_EGGS.asBool())
            badges.append(Default.CHEESE.badge);

        //mark
        else if (avatar.nbt != null) {
            mark: {
                //pride (mark skins)
                BitSet prideSet = pair.getFirst();
                for (int i = pride.length - 1; i >= 0; i--) {
                    if (prideSet.get(i)) {
                        badges.append(pride[i].badge);
                        break mark;
                    }
                }

                //mark fallback
                badges.append(Default.DEFAULT.badge.copy().withStyle(Style.EMPTY.withColor(ColorUtils.userInputHex(avatar.color))));
            }
        }

        // -- special -- //

        Special[] special = Special.values();

        //special badges
        BitSet specialSet = pair.getSecond();
        for (int i = special.length - 1; i >= 0; i--) {
            if (specialSet.get(i))
                badges.append(special[i].badge);
        }

        return badges.getString().isBlank() ? Component.empty() : badges;
    }

    public static void load(UUID id, BitSet pride, BitSet special) {
        badgesMap.put(id, Pair.of(pride, special));
    }

    public static void set(UUID id, int index, boolean value, boolean pride) {
        Pair<BitSet, BitSet> pair = badgesMap.get(id);
        if (pair == null)
            badgesMap.put(id, pair = empty());

        BitSet set = pride ? pair.getFirst() : pair.getSecond();
        set.set(index, value);
    }

    public static void clear(UUID id) {
        badgesMap.remove(id);
    }

    public static Pair<BitSet, BitSet> empty() {
        return Pair.of(new BitSet(Pride.values().length), new BitSet(Special.values().length));
    }

    private enum Default {
        DEFAULT("△"),
        CHEESE("\uD83E\uDDC0"),
        WARNING("❗"),
        ERROR("❌");

        public final Component badge;

        Default(String unicode) {
            this.badge = Component.literal(unicode).withStyle(Style.EMPTY
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, FiguraText.of("badges.standard." + this.name().toLowerCase())))
                    .withFont(TextUtils.FIGURA_FONT)
                    .withColor(ChatFormatting.WHITE)
            );
        }
    }

    private enum Pride {
        AGENDER("ᚠ"),
        AROACE("ᚡ"),
        AROMANTIC("ᚢ"),
        ASEXUAL("ᚣ"),
        BIGENDER("ᚤ"),
        BISEXUAL("ᚥ"),
        DEMIBOY("ᚦ"),
        DEMIGENDER("ᚧ"),
        DEMIGIRL("ᚨ"),
        DEMIROMANTIC("ᚩ"),
        DEMISEXUAL("ᚪ"),
        DISABILITY("ᚫ"),
        FINSEXUAL("ᚬ"),
        GAYMEN("ᚭ"),
        GENDERFAE("ᚮ"),
        GENDERFLUID("ᚯ"),
        GENDERQUEER("ᚰ"),
        INTERSEX("ᚱ"),
        LESBIAN("ᚲ"),
        NONBINARY("ᚳ"),
        PANSEXUAL("ᚴ"),
        PLURAL("ᚵ"),
        POLYSEXUAL("ᚶ"),
        PRIDE("ᚷ"),
        TRANSGENDER("ᚸ");

        public final Component badge;

        Pride(String unicode) {
            this.badge = Component.literal(unicode).withStyle(Style.EMPTY
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, FiguraText.of("badges.pride." + this.name().toLowerCase())))
                    .withFont(TextUtils.FIGURA_FONT)
                    .withColor(ChatFormatting.WHITE)
            );
        }
    }

    private enum Special {
        DEV("★"),
        DISCORD_STAFF("☆", ColorUtils.Colors.DISCORD.hex),
        CONTEST("☆", ColorUtils.Colors.FRAN_PINK.hex),
        DONATOR("❤", ColorUtils.Colors.FRAN_PINK.hex),
        TRANSLATOR("☄"),

        SHADOW("\uD83C\uDF00"),
        MOON("\uD83C\uDF19"),
        SHRIMP("\uD83E\uDD90"),
        BURGER("\uD83C\uDF54");

        public final Component badge;

        Special(String unicode) {
            this(unicode, null);
        }

        Special(String unicode, Integer color) {
            this.badge = Component.literal(unicode).withStyle(Style.EMPTY
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, FiguraText.of("badges.special." + this.name().toLowerCase())))
                    .withFont(TextUtils.FIGURA_FONT)
                    .withColor(color == null ? 0xFFFFFF : color)
            );
        }
    }
}
