package com.chariotsolutions.example.http;

import java.io.File;

/**
 * Authentication parameters, including client cert, server cert, user name, and password.
 */
public class AuthenticationParameters {
    private File clientCertificate = null;
    private String clientCertificatePassword = null;
    private String caCertificate = null;

    public File getClientCertificate() {
        return clientCertificate;
    }

    public void setClientCertificate(File clientCertificate) {
        this.clientCertificate = clientCertificate;
    }

    public String getClientCertificatePassword() {
        return clientCertificatePassword;
    }

    public void setClientCertificatePassword(String clientCertificatePassword) {
        this.clientCertificatePassword = clientCertificatePassword;
    }

    public String getCaCertificate() {
        return caCertificate;
    }

    public void setCaCertificate(String caCertificate) {
        this.caCertificate = caCertificate;
    }
}
