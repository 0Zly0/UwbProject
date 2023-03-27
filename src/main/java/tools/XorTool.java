package tools;

import DataInfo.UwbReceiveInfo;

/**
 * @author 张璐洋1
 */
public class XorTool {
    public static int jiSuan(UwbReceiveInfo uwbReceiveInfo){
        int n = 35 ^ 50 ^ 1;
        Integer[] squId = uwbReceiveInfo.getSequenceId();
         n = xunHuan(squId, n);
        Integer[] versionId = uwbReceiveInfo.getVersionId();
        n = xunHuan(versionId,n);
        Integer[] anchorId = uwbReceiveInfo.getAnchorId();
        n = xunHuan(anchorId,n);
        Integer[] tagId = uwbReceiveInfo.getTagId();
        n= xunHuan(tagId,n);
        Integer[] distance = uwbReceiveInfo.getDistance();
        n = xunHuan(distance,n);
        n = n ^ uwbReceiveInfo.getTagStatus() ^ uwbReceiveInfo.getBatchSn();
        return n;



    }


    public static int xunHuan(Integer[] zhi,int n){
        for (int i = 0; i < zhi.length; i++) {
            n = n ^ zhi[i];
        }
        return n;
    }
}
