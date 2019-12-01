package file.operation;

import file.bean.CatalogEntry;

/**
 * @author Rorke
 * @date 2019/11/11 12:36
 */
public class CopyOperation extends AbstractOperation {
    private CatalogEntry entry =null;
    private String[] fileContext;
    public CatalogEntry getEntry() {
        return entry;
    }

    public void setEntry(CatalogEntry entry) {
        this.entry = entry;
    }

    public void setFileContext(String[] str) {
        fileContext = new String[str.length];
        for (int i = 0; i < str.length; i++) {
            fileContext[i] = String.valueOf((char) fileUtils.binaryToDec(str[i]));
        }
    }

    public String[] getFileContext() {
        return fileContext;
    }
}
