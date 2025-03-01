package org.moon.figura.lua.api.ping;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.moon.figura.FiguraMod;
import org.moon.figura.avatars.Avatar;
import org.moon.figura.backend.NetworkManager;
import org.moon.figura.config.Config;
import org.moon.figura.lua.LuaWhitelist;
import org.moon.figura.lua.docs.LuaTypeDoc;

@LuaWhitelist
@LuaTypeDoc(
        name = "PingFunction",
        description = "ping_function"
)
public class PingFunction extends LuaFunction {

    private final int id;
    private final Avatar owner;
    private final boolean isHost;
    public final LuaFunction func;

    public PingFunction(int id, Avatar owner, LuaFunction func) {
        this.id = id;
        this.owner = owner;
        this.isHost = FiguraMod.isLocal(owner.owner);
        this.func = func;
    }

    @Override
    public LuaValue call() {
        invoke(NONE);
        return NIL;
    }

    @Override
    public LuaValue call(LuaValue arg) {
        invoke(arg);
        return NIL;
    }

    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2) {
        invoke(arg1, arg2);
        return NIL;
    }

    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        invoke(arg1, arg2, arg3);
        return NIL;
    }

    @Override
    public Varargs invoke(Varargs args) {
        if (!isHost)
            return NIL;

        boolean sync = Config.SYNC_PINGS.asBool();
        byte[] data = NetworkManager.sendPing(id, sync, new PingArg(args));
        if (!sync) owner.runPing(id, data);

        return NIL;
    }

    @Override
    public String toString() {
        return "PingFunction";
    }
}
