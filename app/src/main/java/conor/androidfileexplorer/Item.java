package conor.androidfileexplorer;

/**
 * Created by Conor on 10/31/2017.
 */
public class Item implements Comparable<Item>{
    private String name;
    private String data;
    private String date;
    private String path;
    private String image;

    public Item(String name, String data, String date, String path, String image)
    {
        this.name = name;
        this.data = data;
        this.date = date;
        this.path = path;
        this.image = image;
    }
    public String getName()
    {
        return this.name;
    }
    public String getData()
    {
        return this.data;
    }
    public String getDate()
    {
        return this.date;
    }
    public String getPath()
    {
        return this.path;
    }
    public String getImage() {
        return this.image;
    }
    public int compareTo(Item o) {
        if(this.name != null)
            return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
        else
            throw new IllegalArgumentException();
    }
}
