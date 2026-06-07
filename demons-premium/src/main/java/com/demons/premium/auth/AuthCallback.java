package com.demons.premium.auth;

import com.demons.premium.models.User;

/**
 * AuthCallback interface untuk handle authentication results
 */
public interface AuthCallback {
    void onSuccess(User user);
    void onError(String error);
    void onCancel();
}
