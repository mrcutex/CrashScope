package com.cutex.crashscope.ui.home;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00006\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\u001a\u001e\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001a\u0012\u0010\u0006\u001a\u00020\u00012\b\b\u0002\u0010\u0007\u001a\u00020\bH\u0007\u001a$\u0010\t\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u000b2\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\rH\u0007\u001a\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002\u00a8\u0006\u0012"}, d2 = {"CrashItem", "", "crash", "Lcom/cutex/crashscope/data/model/CrashEvent;", "onClick", "Lkotlin/Function0;", "EmptyState", "modifier", "Landroidx/compose/ui/Modifier;", "HomeScreen", "viewModel", "Lcom/cutex/crashscope/ui/home/HomeViewModel;", "onCrashClick", "Lkotlin/Function1;", "formatDate", "", "timestamp", "", "app_debug"})
public final class HomeScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void HomeScreen(@org.jetbrains.annotations.NotNull()
    com.cutex.crashscope.ui.home.HomeViewModel viewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.cutex.crashscope.data.model.CrashEvent, kotlin.Unit> onCrashClick) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void CrashItem(@org.jetbrains.annotations.NotNull()
    com.cutex.crashscope.data.model.CrashEvent crash, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void EmptyState(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    private static final java.lang.String formatDate(long timestamp) {
        return null;
    }
}