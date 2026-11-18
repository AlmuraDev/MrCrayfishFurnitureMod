package com.mrcrayfish.furniture.client.screen;

import com.mojang.blaze3d.platform.NativeImage;
import com.mrcrayfish.furniture.FurnitureMod;
import com.mrcrayfish.furniture.Reference;
import com.mrcrayfish.furniture.client.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Downloads the pictures TVs and photo frames show (client only), and keeps them as textures. As in 1.12.2 it only downloads from the
 * domains the player trusts (ClientConfig), over http(s), and only PNG/JPEG for frames or GIF for TVs. Redirects aren't followed (they
 * could lead off a trusted domain), and downloads over 8 MB, pictures over 1024 pixels a side and GIFs over 256 frames are refused.
 */
public final class OnlineImageCache
{
    private static final int MAX_BYTES = 8 * 1024 * 1024;
    private static final int MAX_SIZE = 1024;
    private static final int MAX_FRAMES = 256;
    private static final int MAX_ENTRIES = 64;
    private static final HttpClient CLIENT = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).followRedirects(HttpClient.Redirect.NEVER).build();

    private static final Map<String, Picture> CACHE = new LinkedHashMap<>(16, 0.75F, true);
    private static int nextId;

    public enum Status
    {
        LOADING,
        READY,
        FAILED
    }

    /**
     * A downloaded picture: one texture per frame (one for a still picture), and how long each frame shows.
     */
    public static final class Picture
    {
        private Status status = Status.LOADING;
        private String error = "";
        private List<Identifier> frames = List.of();
        private int[] delays = new int[0];
        private int totalDelay;
        private int width;
        private int height;

        public Status status()
        {
            return this.status;
        }

        public String error()
        {
            return this.error;
        }

        public int width()
        {
            return this.width;
        }

        public int height()
        {
            return this.height;
        }

        /**
         * The frame to show now.
         */
        public Identifier frame()
        {
            if(this.frames.size() == 1 || this.totalDelay <= 0)
                return this.frames.getFirst();

            long time = Util.getMillis() % this.totalDelay;
            for(int i = 0; i < this.delays.length; i++)
            {
                time -= this.delays[i];
                if(time < 0)
                    return this.frames.get(i);
            }
            return this.frames.getLast();
        }

        private void fail(String error)
        {
            this.status = Status.FAILED;
            this.error = error;
        }
    }

    private record Decoded(List<BufferedImage> frames, int[] delays) {}

    /**
     * The picture at the address, starting its download the first time it's asked for.
     *
     * @param animated a GIF (for TVs) rather than a PNG or JPEG (for photo frames)
     */
    public static Picture get(String url, boolean animated)
    {
        String key = (animated ? "gif:" : "img:") + url;
        Picture picture = CACHE.get(key);
        if(picture != null)
            return picture;

        picture = new Picture();
        CACHE.put(key, picture);
        trim();

        URI uri = parse(url);
        if(uri == null)
        {
            picture.fail("screen.cfm.error.invalid_url");
        }
        else if(!"http".equalsIgnoreCase(uri.getScheme()) && !"https".equalsIgnoreCase(uri.getScheme()))
        {
            picture.fail("screen.cfm.error.wrong_scheme");
        }
        else if(!hasType(uri, animated ? List.of("gif") : List.of("png", "jpg", "jpeg")))
        {
            picture.fail(animated ? "screen.cfm.error.not_gif" : "screen.cfm.error.not_image");
        }
        else if(!isTrusted(uri))
        {
            picture.fail("screen.cfm.error.untrusted");
        }
        else
        {
            Picture target = picture;
            CompletableFuture.supplyAsync(() -> download(uri, animated), Util.nonCriticalIoPool())
                    .whenCompleteAsync((decoded, error) -> upload(target, decoded, error), Minecraft.getInstance());
        }
        return picture;
    }

    @Nullable
    private static URI parse(String url)
    {
        try
        {
            URI uri = new URI(url.trim());
            return uri.getHost() != null ? uri : null;
        }
        catch(Exception e)
        {
            return null;
        }
    }

    private static boolean hasType(URI uri, List<String> extensions)
    {
        String path = uri.getPath() == null ? "" : uri.getPath().toLowerCase(Locale.ROOT);
        return extensions.stream().anyMatch(extension -> path.endsWith("." + extension));
    }

    private static boolean isTrusted(URI uri)
    {
        String host = uri.getHost().toLowerCase(Locale.ROOT);
        if(host.startsWith("www."))
        {
            host = host.substring(4);
        }
        for(String domain : ClientConfig.TRUSTED_URL_DOMAINS.get())
        {
            if(host.equals(domain.trim().toLowerCase(Locale.ROOT)))
                return true;
        }
        return false;
    }

    private static Decoded download(URI uri, boolean animated)
    {
        try
        {
            HttpRequest request = HttpRequest.newBuilder(uri).timeout(Duration.ofSeconds(20)).header("User-Agent", "Mozilla/5.0 (" + Reference.MOD_ID + ")").GET().build();
            HttpResponse<InputStream> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());
            byte[] bytes;
            try(InputStream body = response.body())
            {
                if(response.statusCode() != 200)
                    throw new ImageException("screen.cfm.error.download");
                bytes = readLimited(body);
            }
            return animated ? decodeGif(bytes) : decodeStill(bytes);
        }
        catch(ImageException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new ImageException("screen.cfm.error.download");
        }
    }

    private static byte[] readLimited(InputStream in) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int read;
        while((read = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, read);
            if(out.size() > MAX_BYTES)
                throw new ImageException("screen.cfm.error.too_big");
        }
        return out.toByteArray();
    }

    private static Decoded decodeStill(byte[] bytes) throws IOException
    {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
        if(image == null)
            throw new ImageException("screen.cfm.error.unreadable");
        checkSize(image.getWidth(), image.getHeight());
        return new Decoded(List.of(image), new int[]{0});
    }

    // Draws each GIF frame over the last, as a browser would, so frames that only change part of the picture come out whole
    private static Decoded decodeGif(byte[] bytes) throws IOException
    {
        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("gif");
        if(!readers.hasNext())
            throw new ImageException("screen.cfm.error.unreadable");

        ImageReader reader = readers.next();
        try(ImageInputStream stream = ImageIO.createImageInputStream(new ByteArrayInputStream(bytes)))
        {
            reader.setInput(stream, false);
            int count = reader.getNumImages(true);
            if(count <= 0)
                throw new ImageException("screen.cfm.error.unreadable");
            if(count > MAX_FRAMES)
                throw new ImageException("screen.cfm.error.too_big");

            int width = reader.getWidth(0);
            int height = reader.getHeight(0);
            Node screen = child(reader.getStreamMetadata() != null ? reader.getStreamMetadata().getAsTree("javax_imageio_gif_stream_1.0") : null, "LogicalScreenDescriptor");
            if(screen != null)
            {
                width = Math.max(width, attribute(screen, "logicalScreenWidth", width));
                height = Math.max(height, attribute(screen, "logicalScreenHeight", height));
            }
            checkSize(width, height);

            List<BufferedImage> frames = new ArrayList<>();
            int[] delays = new int[count];
            BufferedImage canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for(int i = 0; i < count; i++)
            {
                BufferedImage frame = reader.read(i);
                IIOMetadata metadata = reader.getImageMetadata(i);
                Node root = metadata.getAsTree("javax_imageio_gif_image_1.0");
                Node descriptor = child(root, "ImageDescriptor");
                Node control = child(root, "GraphicControlExtension");
                int x = descriptor != null ? attribute(descriptor, "imageLeftPosition", 0) : 0;
                int y = descriptor != null ? attribute(descriptor, "imageTopPosition", 0) : 0;
                String disposal = control != null && control.getAttributes().getNamedItem("disposalMethod") != null ? control.getAttributes().getNamedItem("disposalMethod").getNodeValue() : "none";
                int delay = control != null ? attribute(control, "delayTime", 10) * 10 : 100;
                delays[i] = delay <= 10 ? 100 : delay;

                BufferedImage previous = "restoreToPrevious".equals(disposal) ? copy(canvas) : null;
                Graphics2D graphics = canvas.createGraphics();
                graphics.drawImage(frame, x, y, null);
                graphics.dispose();
                frames.add(copy(canvas));

                if("restoreToBackgroundColor".equals(disposal))
                {
                    Graphics2D clear = canvas.createGraphics();
                    clear.setComposite(java.awt.AlphaComposite.Clear);
                    clear.fillRect(x, y, frame.getWidth(), frame.getHeight());
                    clear.dispose();
                }
                else if(previous != null)
                {
                    canvas = previous;
                }
            }
            return new Decoded(frames, delays);
        }
        finally
        {
            reader.dispose();
        }
    }

    private static void checkSize(int width, int height)
    {
        if(width <= 0 || height <= 0 || width > MAX_SIZE || height > MAX_SIZE)
            throw new ImageException("screen.cfm.error.too_big");
    }

    private static BufferedImage copy(BufferedImage image)
    {
        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = copy.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return copy;
    }

    @Nullable
    private static Node child(@Nullable Node node, String name)
    {
        if(node == null)
            return null;
        for(Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
        {
            if(name.equals(child.getNodeName()))
                return child;
        }
        return null;
    }

    private static int attribute(Node node, String name, int fallback)
    {
        NamedNodeMap attributes = node.getAttributes();
        Node attribute = attributes != null ? attributes.getNamedItem(name) : null;
        if(attribute == null)
            return fallback;
        try
        {
            return Integer.parseInt(attribute.getNodeValue());
        }
        catch(NumberFormatException e)
        {
            return fallback;
        }
    }

    // On the render thread: turn the frames into textures
    private static void upload(Picture picture, @Nullable Decoded decoded, @Nullable Throwable error)
    {
        if(error != null || decoded == null)
        {
            Throwable cause = error instanceof java.util.concurrent.CompletionException && error.getCause() != null ? error.getCause() : error;
            picture.fail(cause instanceof ImageException imageException ? imageException.getMessage() : "screen.cfm.error.download");
            return;
        }

        List<Identifier> frames = new ArrayList<>();
        int id = nextId++;
        for(int i = 0; i < decoded.frames().size(); i++)
        {
            BufferedImage frame = decoded.frames().get(i);
            NativeImage image = new NativeImage(frame.getWidth(), frame.getHeight(), false);
            for(int y = 0; y < frame.getHeight(); y++)
            {
                for(int x = 0; x < frame.getWidth(); x++)
                {
                    image.setPixel(x, y, frame.getRGB(x, y));
                }
            }
            Identifier location = Identifier.fromNamespaceAndPath(Reference.MOD_ID, "online/" + id + "/" + i);
            Minecraft.getInstance().getTextureManager().register(location, new DynamicTexture(location::toString, image));
            frames.add(location);
        }

        picture.frames = frames;
        picture.delays = decoded.delays();
        picture.totalDelay = 0;
        for(int delay : decoded.delays())
        {
            picture.totalDelay += delay;
        }
        picture.width = decoded.frames().getFirst().getWidth();
        picture.height = decoded.frames().getFirst().getHeight();
        picture.status = Status.READY;
    }

    // Keeps the cache small, letting go of the textures of what hasn't been looked at for longest
    private static void trim()
    {
        Iterator<Map.Entry<String, Picture>> iterator = CACHE.entrySet().iterator();
        while(CACHE.size() > MAX_ENTRIES && iterator.hasNext())
        {
            Picture old = iterator.next().getValue();
            iterator.remove();
            for(Identifier frame : old.frames)
            {
                Minecraft.getInstance().getTextureManager().release(frame);
            }
        }
    }

    private static final class ImageException extends RuntimeException
    {
        ImageException(String key)
        {
            super(key, null, false, false);
        }
    }

    static
    {
        ImageIO.setUseCache(false);
        FurnitureMod.LOGGER.debug("Online picture cache ready");
    }
}
