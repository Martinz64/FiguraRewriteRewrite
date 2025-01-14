package org.moon.figura.lua.api;

import com.mojang.blaze3d.platform.Window;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.world.phys.Vec3;
import org.luaj.vm2.LuaError;
import org.moon.figura.FiguraMod;
import org.moon.figura.lua.LuaNotNil;
import org.moon.figura.lua.LuaWhitelist;
import org.moon.figura.lua.docs.LuaFunctionOverload;
import org.moon.figura.lua.docs.LuaMethodDoc;
import org.moon.figura.lua.docs.LuaTypeDoc;
import org.moon.figura.math.vector.FiguraVec2;
import org.moon.figura.math.vector.FiguraVec3;
import org.moon.figura.utils.MathUtils;
import org.moon.figura.utils.TextUtils;

@LuaWhitelist
@LuaTypeDoc(
        name = "ClientAPI",
        description = "client"
)
public class ClientAPI {

    public static final ClientAPI INSTANCE = new ClientAPI();

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_fps")
    public static int getFPS() {
        String s = getFPSString();
        if (s.length() == 0)
            return 0;
        return Integer.parseInt(s.split(" ")[0]);
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_fps_string")
    public static String getFPSString() {
        return Minecraft.getInstance().fpsString;
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.is_paused")
    public static boolean isPaused() {
        return Minecraft.getInstance().isPaused();
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_version")
    public static String getVersion() {
        return Minecraft.getInstance().getLaunchedVersion();
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_version_type")
    public static String getVersionType() {
        return Minecraft.getInstance().getVersionType();
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_server_brand")
    public static String getServerBrand() {
        if (Minecraft.getInstance().player == null)
            return null;

        return Minecraft.getInstance().getSingleplayerServer() == null ? Minecraft.getInstance().player.getServerBrand() : "Integrated";
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_chunk_statistics")
    public static String getChunkStatistics() {
        return Minecraft.getInstance().levelRenderer.getChunkStatistics();
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_entity_statistics")
    public static String getEntityStatistics() {
        return Minecraft.getInstance().levelRenderer.getEntityStatistics();
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_sound_statistics")
    public static String getSoundStatistics() {
        return Minecraft.getInstance().getSoundManager().getDebugString();
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_entity_count")
    public static int getEntityCount() {
        if (Minecraft.getInstance().level == null)
            return 0;

        return Minecraft.getInstance().level.getEntityCount();
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_particle_count")
    public static String getParticleCount() {
        return Minecraft.getInstance().particleEngine.countParticles();
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_current_effect")
    public static String getCurrentEffect() {
        if (Minecraft.getInstance().gameRenderer.currentEffect() == null)
            return null;

        return Minecraft.getInstance().gameRenderer.currentEffect().getName();
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_java_version")
    public static String getJavaVersion() {
        return System.getProperty("java.version");
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_used_memory")
    public static long getUsedMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_max_memory")
    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_allocated_memory")
    public static long getAllocatedMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.is_window_focused")
    public static boolean isWindowFocused() {
        return Minecraft.getInstance().isWindowActive();
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.is_hud_enabled")
    public static boolean isHudEnabled() {
        return Minecraft.renderNames();
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.is_debug_overlay_enabled")
    public static boolean isDebugOverlayEnabled() {
        return Minecraft.getInstance().options.renderDebug;
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_window_size")
    public static FiguraVec2 getWindowSize() {
        Window window = Minecraft.getInstance().getWindow();
        return FiguraVec2.of(window.getWidth(), window.getHeight());
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_fov")
    public static double getFOV() {
        return Minecraft.getInstance().options.fov().get();
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_system_time")
    public static long getSystemTime() {
        return System.currentTimeMillis();
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_mouse_pos")
    public static FiguraVec2 getMousePos() {
        MouseHandler mouse = Minecraft.getInstance().mouseHandler;
        return FiguraVec2.of(mouse.xpos(), mouse.ypos());
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_scaled_window_size")
    public static FiguraVec2 getScaledWindowSize() {
        Window window = Minecraft.getInstance().getWindow();
        return FiguraVec2.of(window.getGuiScaledWidth(), window.getGuiScaledHeight());
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_gui_scale")
    public static double getGuiScale() {
        return Minecraft.getInstance().getWindow().getGuiScale();
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_camera_pos")
    public static FiguraVec3 getCameraPos() {
        Vec3 pos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        return FiguraVec3.of(pos.x, pos.y, pos.z);
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_camera_rot")
    public static FiguraVec3 getCameraRot() {
        double f = 180d / Math.PI;
        return MathUtils.quaternionToYXZ(Minecraft.getInstance().gameRenderer.getMainCamera().rotation()).multiply(f, -f, f); //degrees, and negate y
    }

    @LuaWhitelist
    @LuaMethodDoc(
            overloads = @LuaFunctionOverload(
                    argumentTypes = String.class,
                    argumentNames = "text"
            ),
            description = "client.get_text_width"
    )
    public static int getTextWidth(String text) {
        return TextUtils.getWidth(TextUtils.splitText(TextUtils.tryParseJson(text), "\n"), Minecraft.getInstance().font);
    }

    @LuaWhitelist
    @LuaMethodDoc(
            overloads = @LuaFunctionOverload(
                    argumentTypes = String.class,
                    argumentNames = "text"
            ),
            description = "client.get_text_height"
    )
    public static int getTextHeight(String text) {
        return Minecraft.getInstance().font.lineHeight * TextUtils.splitText(TextUtils.tryParseJson(text), "\n").size();
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_active_lang")
    public static String getActiveLang() {
        return Minecraft.getInstance().options.languageCode;
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.has_iris")
    public static boolean hasIris() {
        return FabricLoader.getInstance().isModLoaded("iris");
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.has_iris_shader")
    public static boolean hasIrisShader() {
        return hasIris() && net.irisshaders.iris.api.v0.IrisApi.getInstance().isShaderPackInUse();
    }

    @LuaWhitelist
    @LuaMethodDoc(description = "client.get_figura_version")
    public static String getFiguraVersion() {
        return FiguraMod.VERSION;
    }

    @LuaWhitelist
    @LuaMethodDoc(
            overloads = @LuaFunctionOverload(
                    argumentTypes = {String.class, String.class},
                    argumentNames = {"version1", "version2"}
            ),
            description = "client.compare_versions")
    public static int compareVersions(@LuaNotNil String ver1, @LuaNotNil String ver2) {
        try {
            return FiguraMod.compareVersions(ver1, ver2);
        } catch (Exception e) {
            throw new LuaError(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "ClientAPI";
    }
}
