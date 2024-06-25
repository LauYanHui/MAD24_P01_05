package sg.edu.np.mad.cookbuddy.models;

public class Technique {
    private int imagePath; //to be used for Glide library to load images
    private String title;
    private String purpose; //short description on what technique does
    private String description; //longer description
    private String videoPath; //to be used for when loading technique videos


    //constructor for each technique
    public Technique(int imagePath, String title, String purpose, String description, String videoPath){
        this.imagePath = imagePath;
        this.title = title;
        this.purpose = purpose;
        this.description = description;
        this.videoPath = videoPath;
    }

    //handle getters and setters for each technique
    public int getImagePath() {return imagePath;}

    public void setImagePath(int imagePath) {this.imagePath = imagePath; }

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getVideoPath() {return videoPath;}

    public void setVideoPath() {this.videoPath = videoPath;}
    public String getPurpose() {return purpose;}
    public void setPurpose(String purpose){ this.purpose = purpose;}

    public String getDescription() {return description;}

    public void setDescription(String description){ this.description = description;}
}
