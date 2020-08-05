package club.sk1er.modelfix.hook;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;
import java.util.Map;

public class ItemModelGeneratorHook {
    public static List<BlockPart> generateModel(TextureAtlasSprite sprite, String key, int layer) {
        List<BlockPart> elements = Lists.newArrayList(); // todo: maybe hoistable?
        int width = sprite.getIconWidth();
        int height = sprite.getIconHeight();

        float xRatio = width >> 4;
        float yRatio = height >> 4;

        int size = 0;

        for (int frameCount = 0; frameCount < sprite.getFrameCount(); ++frameCount) {
            int[] textureData = sprite.getFrameTextureData(frameCount)[0];

            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    boolean previous = x - 1 < 0 || isTransparent(textureData, x - 1, y, width, height);
                    boolean current = isTransparent(textureData, x, y, width, height);

                    if (!current) {
                        ++size;
                    }

                    if (!previous && current) {
                        elements.add(horizontalElement(x, y, size, height, xRatio, yRatio, key, layer));
                        size = 0;
                    }
                }

                if (size != 0) {
                    elements.add(horizontalElement(width, y, size, height, xRatio, yRatio, key, layer));
                    size = 0;
                }
            }

            for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++y) {
                    boolean previous = y - 1 < 0 || isTransparent(textureData, x, y - 1, width, height);
                    boolean current = isTransparent(textureData, x, y, width, height);

                    if (!current) {
                        ++size;
                    }

                    if (!previous && current) {
                        elements.add(verticalElement(x, y, size, height, xRatio, yRatio, key, layer));
                        size = 0;
                    }
                }

                if (size != 0) {
                    elements.add(verticalElement(x, height, size, height, xRatio, yRatio, key, layer));
                    size = 0;
                }
            }
        }

        return elements;
    }

    public static BlockPart verticalElement(int x, int y, int size, int height, float xRatio, float yRatio, String key, int layer) {
        Map<EnumFacing, BlockPartFace> map = Maps.newHashMap(); // todo: maybe hoistable?
        map.put(EnumFacing.UP, new BlockPartFace(null, layer, key, new BlockFaceUV(new float[]{x / xRatio, (y - size) / yRatio, (x + 1) / xRatio, (y - size + 1) / yRatio}, 0)));
        map.put(EnumFacing.DOWN, new BlockPartFace(null, layer, key, new BlockFaceUV(new float[]{x / xRatio, (y - 1) / yRatio, (x + 1) / xRatio, y / yRatio}, 0)));
        return new BlockPart(new Vector3f((x - size) / xRatio, (height - (y + 1)) / yRatio, 7.5f), new Vector3f(x / xRatio, (height - y) / yRatio, 8.5F), map, null, true);
    }

    public static BlockPart horizontalElement(int x, int y, int size, int height, float xRatio, float yRatio, String key, int layer) {
        Map<EnumFacing, BlockPartFace> map = Maps.newHashMap(); // todo: maybe hoistable?
        map.put(EnumFacing.NORTH, new BlockPartFace(null, layer, key, new BlockFaceUV(new float[]{x / xRatio, y / yRatio, (x - size) / xRatio, (y + 1) / yRatio}, 0)));
        map.put(EnumFacing.SOUTH, new BlockPartFace(null, layer, key, new BlockFaceUV(new float[]{(x - size) / xRatio, y / yRatio, x / xRatio, (y + 1) / yRatio}, 0)));
        map.put(EnumFacing.WEST, new BlockPartFace(null, layer, key, new BlockFaceUV(new float[]{(x - size) / xRatio, y / yRatio, (x - size + 1) / xRatio, (y + 1) / yRatio}, 0)));
        map.put(EnumFacing.EAST, new BlockPartFace(null, layer, key, new BlockFaceUV(new float[]{(x - 1) / xRatio, y / yRatio, x / xRatio, (y + 1) / yRatio}, 0)));
        return new BlockPart(new Vector3f((x - size) / xRatio, (height - (y + 1)) / yRatio, 7.5f), new Vector3f(x / xRatio, (height - y) / yRatio, 8.5F), map, null, true);
    }

    public static boolean isTransparent(int[] textureData, int x, int y, int width, int height) {
        if (x >= 0 && y >= 0 && x < width && y < height) {
            return (textureData[y * width + x] >> 24 & 255) == 0;
        } else {
            return true;
        }
    }
}
