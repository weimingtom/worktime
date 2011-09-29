package eu.vranckaert.worktime.utils.tracker;

import android.content.Context;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.google.android.apps.analytics.Item;
import com.google.android.apps.analytics.Transaction;
import eu.vranckaert.worktime.utils.context.ContextUtils;

/**
 * User: DIRK VRANCKAERT
 * Date: 17/08/11
 * Time: 17:06
 */
public class AnalyticsTracker {
    private static final String ACCOUNT_UA = "UA-3183255-5";
    private static final int DISPATCH_INTERVAL_SEC = 60;

    private Context ctx;

    private GoogleAnalyticsTracker gat;

    private AnalyticsTracker() {}

    public static AnalyticsTracker getInstance(Context ctx) {
        AnalyticsTracker tracker = new AnalyticsTracker();
        tracker.ctx = ctx;

        if (!isStableBuild(ctx)) {
            return tracker;
        }

        tracker.gat = GoogleAnalyticsTracker.getInstance();
        tracker.gat.startNewSession(ACCOUNT_UA, DISPATCH_INTERVAL_SEC, ctx);

        return tracker;
    }

    private static boolean isStableBuild(Context ctx) {
        String[] nonFinalBuildNames = {"unstable", "alpha", "beta", "rc"};
        String version = ContextUtils.getCurrentApplicationVersionName(ctx).toLowerCase();

        for (String nonFinalBuildName : nonFinalBuildNames) {
            if (version.contains(nonFinalBuildName)) {
                return false;
            }
        }

        return true;
    }

    public void addTransaction(Transaction transaction) {
        if (!isStableBuild(ctx)) {
            return;
        }
        gat.addTransaction(transaction);
    }

    public void addItem(Item item) {
        if (!isStableBuild(ctx)) {
            return;
        }
        gat.addItem(item);
    }

    public void trackTransactions() {
        if (!isStableBuild(ctx)) {
            return;
        }
        gat.trackTransactions();
    }

    public void clearTransactions() {
        if (!isStableBuild(ctx)) {
            return;
        }
        gat.clearTransactions();
    }

    public void trackEvent(String source, String action) {
        if (!isStableBuild(ctx)) {
            return;
        }
        gat.trackEvent(source, action, "", -1);
    }

    public void trackPageView(String pageView) {
        if (!isStableBuild(ctx)) {
            return;
        }
        gat.trackPageView(pageView);
    }

    public void stopSession() {
        if (!isStableBuild(ctx)) {
            return;
        }
        gat.stopSession();
    }
}
