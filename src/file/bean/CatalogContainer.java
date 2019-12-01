package file.bean;

import java.util.ArrayList;

/**
 * @author Rorke
 * @date 2019/11/21 23:35
 */
public class CatalogContainer extends ArrayList<Catalog> {
    private int presentIndex;
    private static CatalogContainer tables = null;
    private CatalogContainer(Catalog rootTable){
        this.add(rootTable);
        presentIndex = this.size();
    }
    public static CatalogContainer getInstance(Catalog rootTable){
        if(tables==null){
            tables = new CatalogContainer(rootTable);
        }
        return tables;
    }
    public void backward(){
        if (presentIndex != 0) {
            presentIndex--;
        }
    }

    public Catalog getTop(){
        return this.get(presentIndex-1);
    }

    public void setTop(Catalog catalog) {
        if(presentIndex<size()) {
            this.set(presentIndex++, catalog);
        }else {
            presentIndex++;
            this.add(catalog);
        }
    }

    public Catalog getRoot() {
        presentIndex = 1;
        return this.get(0);
    }

    public int getPresentIndex() {
        return presentIndex;
    }

    public void setRoot() {
        presentIndex = 1;
    }
}
