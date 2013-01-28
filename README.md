android-ssl
===========

An example Android project using HTTPS/SSL with client certificates and self-signed server certificate.

Note: To successfully run this application, you will need to do the following things:

1. Have or set up an HTTPS server with a self-signed SSL certificate

2. Generate a client certificate (.p12 file) and sign it with the server's private (CA) key

3. Export the server's certificate chain as a .pem file, and replace this project's /assets/master-cacert.pem with that file.
Be sure to keep the name the same.

4. Copy the client certificate to the root directory of your phone or emulator's sdcard as client-cert.p12

5. Update this project's /res/values/strings.xml file - change the value of example_url to point to your server.
The default value is https://www.google.com - this will work, but doesn't really test this code, as Google's SSL
cert is trusted, and Google doesn't require a client certificate.
