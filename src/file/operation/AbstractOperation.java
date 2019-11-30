package file.operation;

import file.service.FileService;
import file.util.FileUtils;

/**
 * @author Rorke
 * @date 2019/11/21 18:54
 */
public abstract class AbstractOperation {
    protected FileUtils fileUtils = FileUtils.getInstance();
}
