package org.moon.figura.lua.api.ping;

import org.luaj.vm2.LuaFunction;
import org.moon.figura.avatars.Avatar;
import org.moon.figura.lua.LuaWhitelist;
import org.moon.figura.lua.docs.LuaTypeDoc;

import java.util.HashMap;

@LuaWhitelist
@LuaTypeDoc(
        name = "PingAPI",
        description = "pings"
)
public class PingAPI {

    private final HashMap<String, PingFunction> map = new HashMap<>();
    private final HashMap<Integer, String> idMap = new HashMap<>();
    private final Avatar owner;

    private int id = 0;

    public PingAPI(Avatar owner) {
        this.owner = owner;
    }

    public PingFunction get(String arg) {
        return map.get(arg);
    }

    public String getName(int id) {
        return idMap.get(id);
    }

    @LuaWhitelist
    public Object __index(String arg) {
        return get(arg);
    }

    @LuaWhitelist
    public void __newindex(String key, LuaFunction value) {
        if (key == null) return;
        PingFunction func = new PingFunction(id, owner, value);
        map.put(key, func);
        idMap.put(id, key);
        id++;
    }

    @Override
    public String toString() {
        return "PingAPI";
    }
}
