package Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class DataRefreshService {

    public static final String DASHBOARD = "Dashboard.fxml";
    public static final String INTAKE = "IntakeService.fxml";
    public static final String REPAIR = "RepairOrders.fxml";
    public static final String BILLING = "BillingCollections.fxml";
    public static final String LOOKUP = "QuickLookup.fxml";
    public static final String REPORTS = "StatisticalReports.fxml";
    public static final String PARTS = "PartsManagement.fxml";

    private static final Set<String> dirtyPages = Collections.synchronizedSet(new HashSet<>());

    private DataRefreshService() {
    }

    public static void markDirty(String... fxmlFiles) {
        if (fxmlFiles == null) {
            return;
        }

        for (String fxmlFile : fxmlFiles) {
            if (fxmlFile != null && !fxmlFile.trim().isEmpty()) {
                dirtyPages.add(fxmlFile);
            }
        }
    }

    public static boolean consumeDirty(String fxmlFile) {
        return dirtyPages.remove(fxmlFile);
    }
}
