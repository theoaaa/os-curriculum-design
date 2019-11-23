package file.operation;

/**
 * @author Rorke
 * @date 2019/11/11 12:35
 */
public class CreateFile extends AbstractOperation{
    private String name;
    private String expandedName;
    private String attribute;
    private int startedIndex;
    private int size;
    private String[] entryContext;

    public CreateFile(String name, String expandedName, String attribute, int startedIndex, int size) {
        this.name = name;
        this.expandedName = expandedName;
        this.attribute = attribute;
        this.startedIndex = startedIndex;
        this.size = size;
    }

    public String[] getEntryContext() {
        for(int i=0;i<3;i++){
            if(i<name.length()) {
                entryContext[i] = fileUtils.decToBinary(name.charAt(i), 8);
            }else {
                entryContext[i] = "00000000";
            }
        }
        entryContext[3] = fileUtils.decToBinary(expandedName.toCharArray()[0],8);
        entryContext[4] = fileUtils.decToBinary(attribute.toCharArray()[0],8);
        entryContext[5] = fileUtils.decToBinary(startedIndex,8);
        String str = fileUtils.decToBinary(size,16);
        entryContext[6] = str.substring(0,7);
        entryContext[7] = str.substring(8);
        return entryContext;
    }
}
