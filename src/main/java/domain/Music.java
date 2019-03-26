package domain;

/**
 * @name 歌名
 * @Singer 歌手
 * @path 路径
 *
 */
public class Music {

    private String name;
    private String singer;
    private String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
