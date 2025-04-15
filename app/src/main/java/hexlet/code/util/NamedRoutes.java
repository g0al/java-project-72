package hexlet.code.util;

public class NamedRoutes {

    public static String indexPath() {
        return "/";
    }

    public static String urlsPath() {
        return "/urls";
    }

    public static String urlPath(Long id) {
        return urlPath(String.valueOf(id));
    }

    public static String urlPath(String id) {
        return "/urls/" + id;
    }

    public static String checkUrlPath(Long id) {
        return checkUrlPath(String.valueOf(id));
    }

    public static String checkUrlPath(String id) {
        return "/urls/" + id + "/checks";
    }

}
