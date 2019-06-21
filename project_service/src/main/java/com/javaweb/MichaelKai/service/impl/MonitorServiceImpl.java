package com.javaweb.MichaelKai.service.impl;

import com.javaweb.MichaelKai.common.properties.MonitorProperties;
import com.javaweb.MichaelKai.common.utils.BytesUtil;
import com.javaweb.MichaelKai.common.utils.SpringContextUtil;
import com.javaweb.MichaelKai.entity.MonitorInfoEntity;
import com.javaweb.MichaelKai.service.MonitorService;
import com.sun.management.OperatingSystemMXBean;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.management.ManagementFactory;

/**
 * @program: project_base
 * @description: 系统监控实现impl
 * @author: YuKai Fan
 * @create: 2019-06-04 09:34
 **/
@Service
public class MonitorServiceImpl implements MonitorService {

    @Override
    public MonitorInfoEntity getMonitorInfo() throws Exception {
        int kb = 1024;

        //可使用内存
        long totalMemory = Runtime.getRuntime().totalMemory() / kb;
        //剩余内存
        long freeMemory = Runtime.getRuntime().freeMemory() / kb;
        //最大可使用内存
        long maxMemory = Runtime.getRuntime().maxMemory() / kb;

        OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        //操作系统
        String osName = System.getProperty("os.name");
        //总的物理内存
        long totalMemorySize = operatingSystemMXBean.getTotalPhysicalMemorySize() / kb;
        // 剩余的物理内存
        long freePhysicalMemorySize = operatingSystemMXBean.getFreePhysicalMemorySize() / kb;
        //已使用的物理内存
        long usedMemory = (operatingSystemMXBean.getTotalPhysicalMemorySize() - operatingSystemMXBean.getFreePhysicalMemorySize()) / kb;

        //获取线程总数
        ThreadGroup threadGroup;
        for (threadGroup = Thread.currentThread().getThreadGroup(); threadGroup.getParent() != null; threadGroup = threadGroup.getParent())
            ;
        int totalThread = threadGroup.activeCount();

        double cpuRatio = 0;
        if (osName.toLowerCase().startsWith("windows")) {
            cpuRatio = getCpuRatioForWindows();
        }

        MonitorInfoEntity monitorInfoEntity = new MonitorInfoEntity();
        monitorInfoEntity.setCpuRatio(cpuRatio);
        monitorInfoEntity.setFreeMemory(freeMemory);
        monitorInfoEntity.setFreePhysicalMemorySize(freePhysicalMemorySize);
        monitorInfoEntity.setMaxMemory(maxMemory);
        monitorInfoEntity.setOsName(osName);
        monitorInfoEntity.setTotalMemory(totalMemory);
        monitorInfoEntity.setTotalThread(totalThread);
        monitorInfoEntity.setTotalMemorySize(totalMemorySize);
        monitorInfoEntity.setUsedMemory(usedMemory);
        return monitorInfoEntity;
    }

    /**
     * 获取windows系统的cpu使用率
     * @return
     */
    private double getCpuRatioForWindows() {
        try {
            MonitorProperties properties = SpringContextUtil.getBean(MonitorProperties.class);
            String procCmd = System.getenv("windir")
                    + "//system32//wbem//wmic.exe process get Caption,CommandLine,"
                    + "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
            //获取进程信息
            long[] c0 = readCPU(Runtime.getRuntime().exec(procCmd));
            Thread.sleep(properties.getCPU_TIME());
            long[] c1 = readCPU(Runtime.getRuntime().exec(procCmd));

            if (c0 != null && c1 != null) {
                long idletime = c1[0] - c0[0];
                long busytime = c1[1] - c0[1];
                return (double) (properties.getPERCENT() * (busytime) / (busytime + idletime));
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    /**
     * 读取cpu信息
     * @param proc
     * @return
     */
    private long[] readCPU(final Process proc) {
        long[] retn = new long[2];
        try {
            MonitorProperties properties = SpringContextUtil.getBean(MonitorProperties.class);
            proc.getOutputStream().close();
            InputStreamReader ir = new InputStreamReader(proc.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line = input.readLine();
            if (line == null || line.length() < properties.getFAULT_LENGTH()) {
                return null;
            }
            int capidx = line.indexOf("Caption");
            int cmdidx = line.indexOf("CommandLine");
            int rocidx = line.indexOf("ReadOperationCount");
            int umtidx = line.indexOf("UserModeTime");
            int kmtidx = line.indexOf("KernelModeTime");
            int wocidx = line.indexOf("WriteOperationCount");
            long idletime = 0;
            long kneltime = 0;
            long usertime = 0;
            while ((line = input.readLine()) != null) {
                if (line.length() < wocidx) {
                    continue;
                }
                // 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,
                // ThreadCount,UserModeTime,WriteOperation
                String caption = BytesUtil.substring(line, capidx, cmdidx - 1)
                        .trim();
                String cmd = BytesUtil.substring(line, cmdidx, kmtidx - 1).trim();
                if (cmd.contains("wmic.exe")) {
                    continue;
                }
                // log.info("line="+line);
                if (caption.equals("System Idle Process")
                        || caption.equals("System")) {
                    idletime += Long.valueOf(
                            BytesUtil.substring(line, kmtidx, rocidx - 1).trim());
                    idletime += Long.valueOf(
                            BytesUtil.substring(line, umtidx, wocidx - 1).trim());
                    continue;
                }

                if (!BytesUtil.substring(line, kmtidx, rocidx - 1).trim().equals("")) {
                    kneltime += Long.valueOf(
                            BytesUtil.substring(line, kmtidx, rocidx - 1).trim());
                }

                if (!BytesUtil.substring(line, umtidx, rocidx - 1).trim().equals("")) {
                    usertime += Long.valueOf(
                            BytesUtil.substring(line, umtidx, wocidx - 1).trim());
                }
            }
            retn[0] = idletime;
            retn[1] = kneltime + usertime;
            return retn;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                proc.getInputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        MonitorServiceImpl monitorService = new MonitorServiceImpl();
        MonitorInfoEntity monitorInfo = monitorService.getMonitorInfo();
        System.out.println("cpu占有率=" + monitorInfo.getCpuRatio());

        System.out.println("可使用内存=" + monitorInfo.getTotalMemory());
        System.out.println("剩余内存=" + monitorInfo.getFreeMemory());
        System.out.println("最大可使用内存=" + monitorInfo.getMaxMemory());

        System.out.println("操作系统=" + monitorInfo.getOsName());
        System.out.println("总的物理内存=" + monitorInfo.getTotalMemorySize() + "kb");
        System.out.println("剩余的物理内存=" + monitorInfo.getFreeMemory() + "kb");
        System.out.println("已使用的物理内存=" + monitorInfo.getUsedMemory() + "kb");
        System.out.println("线程总数=" + monitorInfo.getTotalThread() + "kb");
    }
}