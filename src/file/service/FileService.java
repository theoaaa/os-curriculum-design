package file.service;

import disk.bean.DiskBlock;
import disk.service.DiskService;
import file.bean.Catalog;
import file.bean.CatalogContainer;
import file.bean.CatalogEntry;
import file.bean.RootCatalog;
import file.operation.*;
import file.util.FileUtils;

import java.util.ArrayList;


/**
 * @author Rorke
 * @date 2019/11/11 12:37
 */
public class FileService {
    private DiskService diskService;
    private static FileService fileService = new FileService();
    private DiskBlock[] FATBlocks;
    private CatalogContainer tables;
    private FileUtils fileUtils = FileUtils.getInstance();
    private CopyOperation copyOperation = new CopyOperation();
    private RootCatalog rootCatalog;
    private CatalogEntry lastEntry;
    private FileService(){
        diskService = DiskService.getInstance();
        rootCatalog = new RootCatalog(diskService.getDiskBlock(2));
        FATBlocks =  diskService.getFATBlocks();
        tables = CatalogContainer.getInstance(rootCatalog);
    }

    public RootCatalog getRootCatalog() {
        return rootCatalog;
    }

    /**
     * 前进方法
     * @param fileName 文件夹名字
     * @return 目录
     */
    public String[] forward(String fileName){
        tables.forward();
        return openFile(fileName,"D");
    }

    /**
     * 后退方法
     * @param fileName 目录名
     * @return 目录
     */
    public void  backward(){
        tables.getRoot();
    }

    /**
     * 改变文件扩展名
     * @param fileName
     * @param expandedName
     */
    public void changeFileAttribute(String fileName,String expandedName){
        CatalogEntry targetEntry = fileUtils.getTargetEntryByTables(fileName,expandedName,tables,FATBlocks);
        if(expandedName.equals("D")){
            return;
        }else{
            switch (targetEntry.getExpandedName()){
                case "E": targetEntry.setExpandedName("T");
                    break;
                case "T": targetEntry.setExpandedName("E");
            }
        }
    }
    /**
     * 复制文件方法
     * @param fileName 文件名
     * @param expandedName 文件拓展名
     */
    public void copyFile(String fileName,String expandedName){
        CatalogEntry targetEntry = fileUtils.getTargetEntryByTables(fileName,expandedName,tables,FATBlocks);
        copyOperation.setEntry(targetEntry);
    }

    /**
     * 黏贴文件方法
     */
    public boolean pasteFile(){
        CatalogEntry entry = copyOperation.getEntry();
        boolean statement = createFile(entry.getName(),entry.getExpandedName(),entry.getAttribute(),entry.getSize());
        if(statement) {
            String[] context = copyOperation.getFileContext(FATBlocks);
            saveFile(entry.getName(), entry.getExpandedName(), context);
        }
        return statement;
    }

    public boolean renameFile(String oldFileName,String oldExpandedName,String newName){
        CatalogEntry targetEntry = fileUtils.getTargetEntryByTables(newName,oldExpandedName,tables,FATBlocks);
        boolean statement = false;
        if(targetEntry.isEmpty()){
            targetEntry = fileUtils.getTargetEntryByTables(oldFileName,oldExpandedName,tables,FATBlocks);
            targetEntry.setName(newName, oldExpandedName);
            statement = true;
        }
        return statement;
    }

    /**
     * 移动文件
     * @param oldPath  旧路径
     * @param newPath  新路径
     * @param fileName 文件名
     * @param expandedName 文件拓展名
     * @return 移动结果
     */
    public boolean moveFile(String oldPath,String newPath,String fileName,String expandedName){
        MoveOperation moveOperation = new MoveOperation(oldPath, newPath,FATBlocks);
        return moveOperation.moveFile(fileName,expandedName);
    }

    /**
     * 删除文件
     * @param fileName 文件名
     * @param expandedName 文件拓展名
     * @return 删除结果
     */
    public boolean deleteFile(String fileName,String expandedName){
        boolean statement = false;
        DeleteOperation deleteOperation = new DeleteOperation();
        ArrayList<Catalog> targetCatalog = fileUtils.getFullCatalog(tables,FATBlocks);
        CatalogEntry targetEntry = fileUtils.getTargetEntryByTables(fileName,expandedName,tables,FATBlocks);
        if(!targetEntry.isEmpty()){
            if ("D".equals(expandedName)) {
                statement = deleteOperation.deleteDirectory(targetEntry, targetCatalog, FATBlocks);
            } else {
                statement = deleteOperation.deleteFile(targetEntry, targetCatalog, FATBlocks);
            }
            lastEntry.setSize(lastEntry.getSize()-8);
        }
        return statement;
    }

    /**
     * 打开文件方法
     * @param fileName 文件名称
     * @param expandedName 文件拓展名
     * @return 内容
     */
    public String[] openFile(String fileName,String expandedName){
        CatalogEntry targetEntry =  fileUtils.getTargetEntryByTables(fileName,expandedName,tables,FATBlocks);
        lastEntry = targetEntry;
        Catalog targetCatalog = new Catalog(diskService.getDiskBlock(targetEntry.getStartedBlockIndex()));
        if(expandedName.equals("D")) {
            tables.setTop(targetCatalog);
        }
        OpenOperation openOperation = new OpenOperation();
        return openOperation.open(FATBlocks,targetEntry);
    }

    /**
     * 创建文件方法
     * @param  fileName 文件名
     * @param  expandedName 扩展名，T--文本文件 E--可执行文件 D--文件夹
     * @param  attribute 属性--这个暂时没有用可以塞个R--只读  或者 W--可写给它
     * @param  size 文件的大小，统一为0，因为只是在创建先让他站着磁盘的位置先
     * @return 创建结果
     */
    public boolean createFile(String fileName, String expandedName, String attribute, int size) {
        if(tables.getTop().isFull()&&tables.getPresentIndex()!=1){
            fileUtils.modifyFAT(tables.getTop().getIndex(),fileUtils.getEmptyBlockIndex(FATBlocks),FATBlocks);
        }
        CreateOperation createOperation = new CreateOperation();
        boolean statement = false;
        CatalogEntry targetEntry = fileUtils.getTargetEntryByTables(fileName,expandedName,tables,FATBlocks);
        int getEmptyBlockIndex =  fileUtils.getEmptyBlockIndex(FATBlocks);
        if(targetEntry.isEmpty()&&getEmptyBlockIndex!=-1){
            targetEntry.setContext(createOperation.getEntryContext(fileName, expandedName, attribute, getEmptyBlockIndex, size));
            fileUtils.modifyFAT(targetEntry.getStartedBlockIndex(),1,FATBlocks);
            statement = true;
            diskService.modifyDisk();
        }
        return statement;
    }

    /**
     * 保存文件方法
     * @param fileName    文件名
     * @param fileContext 文件内容
     */
    public void saveFile(String fileName, String expandedName,String[] fileContext) {
        CreateOperation createOperation = new CreateOperation();
        CatalogEntry targetEntry = fileUtils.getTargetEntryByTables(fileName,expandedName,tables,FATBlocks);
        if (!targetEntry.isEmpty()) {
            targetEntry.setSize(fileContext.length);
            DiskBlock targetDiskBlock = diskService.getDiskBlock(targetEntry.getStartedBlockIndex());
            fileUtils.modifyNextBlock(FATBlocks,targetDiskBlock);
            fileUtils.modifyFAT(targetDiskBlock.getIndex(),1,FATBlocks);
            for(int i=0;i*128<fileContext.length;i++){
                boolean longer = false;
                int contextLength;
                if((i+1)*128<fileContext.length){
                    contextLength = 128;
                    longer = true;
                }else{
                    contextLength = fileContext.length-i*128;
                }
                String[] subStr = fileUtils.getSubContext(fileContext,i*128,i*128+contextLength);
                createOperation.setFileContext(subStr,targetDiskBlock);
                int result = fileUtils.getFileFatResult(FATBlocks,targetDiskBlock);
                int nextBlockIndex;
                if(result!=1) {
                    nextBlockIndex = longer ? fileUtils.getFileFatResult(FATBlocks,targetDiskBlock):1;
                }else{
                    nextBlockIndex = longer ? fileUtils.getEmptyBlockIndex(FATBlocks) : 1;//新创建文件且length>128，后续的盘fatResult = 0
                }
                fileUtils.modifyFAT(targetDiskBlock.getIndex(),nextBlockIndex,FATBlocks);
                targetDiskBlock = diskService.getDiskBlock(nextBlockIndex);
            }
            diskService.modifyDisk();
        }
    }


    /**
     * 单例模式
     * @return 同一个FileService对象
     */
    public static FileService getInstance(){
        if(fileService==null){
            fileService = new FileService();
        }
        return fileService;
    }
}
