package sg.edu.np.mad.myapplication;
import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    public String Username;

    private String Password;

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

//    public User(String name, String password, boolean no_allergy, boolean gluten, boolean eggs, boolean dairy, boolean fish, boolean shellfish, boolean soy, boolean peanut,
//                boolean sesame, boolean treeNut){
//        this.Username = name;
//        this.Password = password;
//        this.noAllergy= no_allergy;
//        this.Gluten = gluten;
//        this.Eggs = eggs;
//        this.Dairy = dairy;
//        this.Fish = fish;
//        this.Shellfish = shellfish;
//        this.Soy = soy;
//        this.Peanut = peanut;
//        this.Sesame = sesame;
//        this.TreeNut = treeNut;
//    }
    public User(String name, String password, List<Boolean> AllergyList){
        this.Username = name;
        this.Password = password;
        this.allergyList = AllergyList;
    }
    public String getName(){return Username;}

    public String getDescription(){return  Password;}

    public List<Boolean> getAllergyList() {
        return allergyList;
    }
}

