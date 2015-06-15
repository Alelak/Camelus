package util;

// On utilise cette class pour faciliter l'utilisation d'un ChoiceBox
public class Choice {
    Integer id; String displayString;
    
    public Choice(Integer id){ this(id, null); }
    public Choice(String  displayString){ this(null, displayString); }
    public Choice(Integer id, String displayString) { 
    	this.id = id; 
    	this.displayString = displayString; 
    }
    
    @Override public String toString() { return displayString; }
    
    @Override public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Choice choice = (Choice) o;
      return displayString != null && displayString.equals(choice.displayString) || id != null && id.equals(choice.id);
    }
    
    @Override public int hashCode() {
      int result = id != null ? id.hashCode() : 0;
      result = 31 * result + (displayString != null ? displayString.hashCode() : 0);
      return result;
    }
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDisplayString() {
		return displayString;
	}
	public void setDisplayString(String displayString) {
		this.displayString = displayString;
	}
    
    
}
