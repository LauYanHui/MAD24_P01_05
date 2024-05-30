package sg.edu.np.mad.myapplication;
import java.util.List;

public class User {
    public String Username;

    private String Password;

    public int id;
    public boolean noAllergy;

    public boolean Gluten;
    public boolean Eggs;
    public boolean Dairy;
    public boolean Fish;
    public boolean Shellfish;
    public boolean Soy;
    public boolean Peanut;
    public boolean Sesame;
    public boolean TreeNut;

    public List<Boolean> allergyList;

    public User(String name, String password, int id, boolean no_allergy, boolean gluten, boolean eggs, boolean dairy, boolean fish, boolean shellfish, boolean soy, boolean peanut,
                boolean sesame, boolean treeNut){
        this.Username = name;
        this.Password = password;
        this.id = id;
        this.noAllergy= no_allergy;
        this.Gluten = gluten;
        this.Eggs = eggs;
        this.Dairy = dairy;
        this.Fish = fish;
        this.Shellfish = shellfish;
        this.Soy = soy;
        this.Peanut = peanut;
        this.Sesame = sesame;
        this.TreeNut = treeNut;
    }
    public String getName(){return Username;}

    public String getDescription(){return  Password;}

    public List<Boolean> getAllergyList() {
        return allergyList;
    }
}

