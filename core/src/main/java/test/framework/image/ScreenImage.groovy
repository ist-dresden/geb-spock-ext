package test.framework.image

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class ScreenImage {

    static ImagePHash PHASH = new ImagePHash();

    private BufferedImage _image
    private File _file

    private String _hash

    ScreenImage(File file) {
        setImage(file)
    }

    ScreenImage(BufferedImage image) {
        setImage(image)
    }

    ScreenImage(InputStream stream) {
        setImage(stream)
    }

    ScreenImage(byte[] bytes) {
        setImage(bytes)
    }

    int distance(ScreenImage other) {
        other ? PHASH.distance(hash, other.hash) : 0
    }

    String getHash() {
        if (!_hash) {
            _hash = PHASH.getHash(image)
        }
        _hash
    }

    BufferedImage getImage() {
        _image
    }

    void setImage(BufferedImage image) {
        _image = image
    }

    void setImage(byte[] bytes) {
        setImage(new ByteArrayInputStream(bytes))
    }

    void setImage(InputStream stream) {
        try {
            setImage(ImageIO.read(stream))
        } finally {
            stream.close()
        }
    }

    void setImage(File file) {
        setImage(new FileInputStream(file))
        setFile(file)
    }

    void load(File file = _file) {
        if (file) {
            setImage(file)
        }
    }

    void save(File file = _file) {
        if (file) {
            file.parentFile.mkdirs()
            if (file.exists()) {
                file.delete()
            }
            FileOutputStream stream = new FileOutputStream(file)
            try {
                ImageIO.write(image, "PNG", stream)
            } finally {
                stream.close()
            }
            setFile(file)
        }
    }

    File getFile() {
        _file
    }

    void setFile(File file) {
        _file = file
    }

    String toString() {
        String path = file?.absolutePath
        path //"<a href=\"file://${path}\">${path}</a>"
    }
}
