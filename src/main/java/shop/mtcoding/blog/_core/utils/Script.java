package shop.mtcoding.blog._core.utils;

public class Script {

    public static String unknownBack(String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("alert('" + msg.substring(0, 50) + "');");
        sb.append("history.back();");
        sb.append("</script>");
        return sb.toString();
    }

    public static String back(String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("alert('" + msg + "');");
        sb.append("history.back();");
        sb.append("</script>");
        return sb.toString();
    }

    public static String href(String url) {
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("location.href='" + url + "';");
        sb.append("</script>");
        return sb.toString();
    }

    public static String href(String url, String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("alert('" + msg + "');");
        sb.append("location.href='" + url + "';");
        sb.append("</script>");
        return sb.toString();
    }
}