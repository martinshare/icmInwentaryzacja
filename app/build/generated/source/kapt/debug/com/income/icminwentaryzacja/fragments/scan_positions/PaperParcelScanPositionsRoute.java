// Generated by paperparcel.PaperParcelProcessor (https://github.com/grandstaish/paperparcel).
package com.income.icminwentaryzacja.fragments.scan_positions;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.income.icminwentaryzacja.backstack.BaseRoute;
import paperparcel.TypeAdapter;
import paperparcel.internal.ParcelableAdapter;
import paperparcel.internal.StaticAdapters;

final class PaperParcelScanPositionsRoute {
  static final TypeAdapter<BaseRoute> BASE_ROUTE_PARCELABLE_ADAPTER =
      new ParcelableAdapter<BaseRoute>(null);

  @NonNull
  static final Parcelable.Creator<ScanPositionsRoute> CREATOR =
      new Parcelable.Creator<ScanPositionsRoute>() {
        @Override
        public ScanPositionsRoute createFromParcel(Parcel in) {
          String tag = StaticAdapters.STRING_ADAPTER.readFromParcel(in);
          BaseRoute returnRoute =
              PaperParcelScanPositionsRoute.BASE_ROUTE_PARCELABLE_ADAPTER.readFromParcel(in);
          boolean isReturning = in.readInt() == 1;
          ScanPositionsRoute data = new ScanPositionsRoute(tag);
          data.setReturnRoute(returnRoute);
          data.setReturning(isReturning);
          return data;
        }

        @Override
        public ScanPositionsRoute[] newArray(int size) {
          return new ScanPositionsRoute[size];
        }
      };

  private PaperParcelScanPositionsRoute() {}

  static void writeToParcel(@NonNull ScanPositionsRoute data, @NonNull Parcel dest, int flags) {
    StaticAdapters.STRING_ADAPTER.writeToParcel(data.getTag(), dest, flags);
    PaperParcelScanPositionsRoute.BASE_ROUTE_PARCELABLE_ADAPTER.writeToParcel(
        data.getReturnRoute(), dest, flags);
    dest.writeInt(data.isReturning() ? 1 : 0);
  }
}
