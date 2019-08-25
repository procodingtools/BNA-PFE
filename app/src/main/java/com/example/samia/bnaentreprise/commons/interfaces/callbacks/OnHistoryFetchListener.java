package com.example.samia.bnaentreprise.commons.interfaces.callbacks;

import com.example.samia.bnaentreprise.commons.entities.HistoryEntity;

import java.util.List;

public interface OnHistoryFetchListener {
    void onHistoryFetch(List<HistoryEntity> data);
    void onFailed();
}
