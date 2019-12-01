package util;

public class ProcessIdGenerator {
    public static Integer processId = 0;
    public static Integer generateProcessId(){
        return processId++;
    }
}
