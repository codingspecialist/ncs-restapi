package shop.mtcoding.blog._core.utils;

import java.util.Base64;

public class Base64Util {
    // image/jpeg 중에 jpeg만 return
    public static String getMimeType(String imgBase64) {
        int beginIndex = imgBase64.indexOf("/") + 1;
        int endIndex = imgBase64.indexOf(";");
        String mimeType = imgBase64.substring(beginIndex, endIndex);
        return mimeType;
    }

    public static String encodeAsString(byte[] imgBytes, String mimeType) {
        String imgBase64 = Base64.getEncoder().encodeToString(imgBytes);
        imgBase64 = "data:$mimeType;base64,$imgBase64".replace("$mimeType", mimeType).replace("$imgBase64", imgBase64);
        return imgBase64;
    }

    public static byte[] decodeAsBytes(String imgBase64) {
        // 1. mimetype parsing
        String mimeType = getMimeType(imgBase64);
        //System.out.println(mimeType);

        // 2. img parsing
        int prefixEndIndex = imgBase64.indexOf(",");
        String img = imgBase64.substring(prefixEndIndex + 1);
        //System.out.println(img);

        // 3. base64 decode to byte[]
        byte[] imgBytes = Base64.getDecoder().decode(img);
        return imgBytes;
    }
}
