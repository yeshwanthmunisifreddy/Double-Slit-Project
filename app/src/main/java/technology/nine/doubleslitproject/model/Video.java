package technology.nine.doubleslitproject.model;

public class Video {
    private String id;
    private Snippet snippet;

    public Video(String id, Snippet snippet) {
        this.id = id;
        this.snippet = snippet;
    }

    public String getId() {
        return id;
    }

    public Snippet getSnippet() {
        return snippet;
    }

    public static class Snippet {
        private String title;
        private Thumbnail thumbnails;

        public Snippet(String title, Thumbnail thumbnails) {
            this.title = title;
            this.thumbnails = thumbnails;
        }

        public String getTitle() {
            return title;
        }

        public Thumbnail getThumbnails() {
            return thumbnails;
        }
    }

    public static class Thumbnail {
        private High high;

        public Thumbnail(High high) {
            this.high = high;
        }

        public High getHigh() {
            return high;
        }
    }


    public static class High {
        private String url;
        private int width;
        private int height;

        public High(String url, int width, int height) {
            this.url = url;
            this.width = width;
            this.height = height;
        }

        public String getUrl() {
            return url;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
