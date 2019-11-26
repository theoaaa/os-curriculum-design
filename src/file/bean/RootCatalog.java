package file.bean;

import disk.bean.DiskBlock;

/**
 * @author Rorke
 * @date 2019/11/26 19:59
 */
public class RootCatalog extends Catalog {
    public RootCatalog(DiskBlock catalogBlock) {
        super(catalogBlock);
    }

}
