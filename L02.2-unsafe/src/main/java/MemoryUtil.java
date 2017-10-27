import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.Map;
import javax.management.ListenerNotFoundException;
import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

import com.sun.management.GarbageCollectionNotificationInfo;
import com.sun.management.GcInfo;

/**
 *
 */
public class MemoryUtil {
    private static final int NORM_NAME_LENGTH = 25;
    private static final long SIZE_KB = 1024;
    private static final long SIZE_MB = SIZE_KB * 1024;
    private static final long SIZE_GB = SIZE_MB * 1024;
    private static final String SPACES = "                    ";
    private static Map<String, MemRegion> memRegions;

    // Вспомогательный класс для хранения информации о регионах памяти
    private static class MemRegion {
        private boolean heap;        // Признак того, что это регион кучи
        private String normName;    // Имя, доведенное пробелами до универсальной длины
        public MemRegion(String name, boolean heap) {
            this.heap = heap;
            normName = name.length() < NORM_NAME_LENGTH ? name.concat(SPACES.substring(0, NORM_NAME_LENGTH - name.length())) : name;
        }
        public boolean isHeap() {
            return heap;
        }
        public String getNormName() {
            return normName;
        }
    }

    static {
        // Запоминаем информацию обо всех регионах памяти
        memRegions = new HashMap<String, MemRegion>(ManagementFactory.getMemoryPoolMXBeans().size());
        for(MemoryPoolMXBean mBean: ManagementFactory.getMemoryPoolMXBeans()) {
            memRegions.put(mBean.getName(), new MemRegion(mBean.getName(), mBean.getType() == MemoryType.HEAP));
        }
    }

    // Обработчик сообщений о сборке мусора
    private static NotificationListener gcHandler = new NotificationListener() {
        @Override
        public void handleNotification(Notification notification, Object handback) {
            if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                System.out.println("0XDEADBEAF");
                GarbageCollectionNotificationInfo notifInfo = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

                GcInfo gcInfo = notifInfo.getGcInfo();
                System.out.printf("Action: %s, %s, %s\n", notifInfo.getGcAction(), notifInfo.getGcCause(), notifInfo.getGcName());
                System.out.printf("Time: %d, %d, %d\n", gcInfo.getStartTime(), gcInfo.getEndTime(), gcInfo.getDuration());
                System.out.printf("Memory: %s, %s\n", gcInfo.getMemoryUsageBeforeGc().toString(), gcInfo.getMemoryUsageAfterGc().toString());



                Map<String, MemoryUsage> memBefore = notifInfo.getGcInfo().getMemoryUsageBeforeGc();
                Map<String, MemoryUsage> memAfter = notifInfo.getGcInfo().getMemoryUsageAfterGc();
                StringBuilder sb = new StringBuilder();
                sb.append("[").append(notifInfo.getGcAction()).append(" / ").append(notifInfo.getGcCause())
                        .append(" / ").append(notifInfo.getGcName()).append(" / (");
                appendMemUsage(sb, memBefore);
                sb.append(") -> (");
                appendMemUsage(sb, memAfter);
                sb.append("), ").append(notifInfo.getGcInfo().getDuration()).append(" ms]");
                System.out.println(sb.toString());
            }
        }
    };

    /**
     * Выводит в stdout информацию о текущем состоянии различных разделов памяти.
     */
    public static void printUsage(boolean heapOnly) {
        for(MemoryPoolMXBean mBean: ManagementFactory.getMemoryPoolMXBeans()) {
            if (!heapOnly || mBean.getType() == MemoryType.HEAP) {
                printMemUsage(mBean.getName(), mBean.getUsage());
            }
        }
    }

    /**
     * Запускает процесс мониторинга сборок мусора.
     */
    public static void startGCMonitor() {
        for(GarbageCollectorMXBean mBean: ManagementFactory.getGarbageCollectorMXBeans()) {
            ((NotificationEmitter) mBean).addNotificationListener(gcHandler, null, null);
            System.out.println("GC bean:" + mBean.getName());
        }

        for(MemoryPoolMXBean mBean: ManagementFactory.getMemoryPoolMXBeans()) {
            System.out.println("Memory pool: " + mBean.getName() + ", " + mBean.getType());
        }
    }

    /**
     * Останавливает процесс мониторинга сборок мусора.
     */
    public static void stopGCMonitor() {
        for(GarbageCollectorMXBean mBean: ManagementFactory.getGarbageCollectorMXBeans()) {
            try {
                ((NotificationEmitter) mBean).removeNotificationListener(gcHandler);
            } catch(ListenerNotFoundException e) {
            }
        }
    }

    private static void printMemUsage(String title, MemoryUsage usage) {
        System.out.println(String.format("%s%s\t%.1f%%\t[%s]",
                memRegions.get(title).getNormName(),
                formatMemory(usage.getUsed()),
                usage.getMax() < 0 ? 0.0 : (double)usage.getUsed() / (double)usage.getMax() * 100,
                formatMemory(usage.getMax())));
    }

    private static String formatMemory(long bytes) {
        if (bytes > SIZE_GB) {
            return String.format("%.2fG", bytes / (double)SIZE_GB);
        } else if (bytes > SIZE_MB) {
            return String.format("%.2fM", bytes / (double)SIZE_MB);
        } else if (bytes > SIZE_KB) {
            return String.format("%.2fK", bytes / (double)SIZE_KB);
        }
        return Long.toString(bytes);
    }

    private static void appendMemUsage(StringBuilder sb, Map<String, MemoryUsage> memUsage) {
        for(Map.Entry<String, MemoryUsage> entry: memUsage.entrySet()) {
            if (memRegions.get(entry.getKey()).isHeap()) {
                sb.append(entry.getKey()).append(" used=")
                        .append(entry.getValue().getUsed() >> 10)
                        .append("K; ");
            }
        }
    }
}