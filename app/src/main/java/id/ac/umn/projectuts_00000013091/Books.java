package id.ac.umn.projectuts_00000013091;

public class Books {
    private String asin;
    private String group;
    private String format;
    private String title;
    private String author;
    private String publisher;

    public Books(String asin, String group, String format, String title, String author, String publisher){
        this.asin=asin;
        this.group=group;
        this.format=format;
        this.title=title;
        this.author=author;
        this.publisher=publisher;
    }

    public String getAsin(){
        return this.asin;
    }
    public String getGroup(){
        return this.group;
    }
    public String getFormat(){
        return this.format;
    }
    public String getTitle(){
        return this.title;
    }
    public String getAuthor(){
        return this.author;
    }
    public String getPublisher(){
        return this.publisher;
    }
}
