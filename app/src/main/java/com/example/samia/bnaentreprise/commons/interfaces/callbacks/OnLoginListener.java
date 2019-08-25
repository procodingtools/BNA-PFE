package com.example.samia.bnaentreprise.commons.interfaces.callbacks;

import com.example.samia.bnaentreprise.commons.entities.EntrepriseEntity;

public interface OnLoginListener {
    void onLogin(EntrepriseEntity entity);
    void onError(int statusCode);
}
