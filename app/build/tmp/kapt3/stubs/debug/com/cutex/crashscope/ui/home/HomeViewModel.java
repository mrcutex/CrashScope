package com.cutex.crashscope.ui.home;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u000b\u001a\u00020\fR\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/cutex/crashscope/ui/home/HomeViewModel;", "Landroidx/lifecycle/ViewModel;", "dao", "Lcom/cutex/crashscope/data/db/CrashDao;", "(Lcom/cutex/crashscope/data/db/CrashDao;)V", "crashes", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/cutex/crashscope/data/model/CrashEvent;", "getCrashes", "()Lkotlinx/coroutines/flow/StateFlow;", "refresh", "", "app_debug"})
public final class HomeViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.cutex.crashscope.data.db.CrashDao dao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.cutex.crashscope.data.model.CrashEvent>> crashes = null;
    
    public HomeViewModel(@org.jetbrains.annotations.NotNull()
    com.cutex.crashscope.data.db.CrashDao dao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.cutex.crashscope.data.model.CrashEvent>> getCrashes() {
        return null;
    }
    
    public final void refresh() {
    }
}