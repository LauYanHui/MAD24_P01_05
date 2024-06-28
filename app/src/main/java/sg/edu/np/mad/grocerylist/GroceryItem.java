package sg.edu.np.mad.grocerylist;

public class GroceryItem {
    private String name;
    private boolean isChecked;

    public GroceryItem(String name) {
        this.name = name;
        this.isChecked = false;
    }

    public String getName() {
        return name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setName(String string) {
    }
}
