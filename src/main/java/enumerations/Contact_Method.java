package enumerations;

public enum Contact_Method {

    TELEPHONE("Telephone"),
    E_MAIL("E-Mail");

    private String label;

    Contact_Method(String str){
        this.label = str;
        System.out.println("Enumeration Contact_Method constructed");
    }

    public String getLabel()
    {
        return this.label;
    }
    public void setLabel(String label)
    {
        this.label = label;
    }
    @Override
    public String toString() {
        return "Contact_Method{" +
                "label='" + label + '\'' +
                "} ";
    }
}
