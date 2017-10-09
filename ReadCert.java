package readcert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import javax.net.ssl.KeyManagerFactory;




public class ReadCert {

    public static void main(String [] args) throws Exception {
     ReadCert RC = new ReadCert();
     
    // RC.Read_PEM_DER_Cert("d:\\104623\\104623.pem");
     RC.Read_JKS("d:\\cert.jks", "123456");

 }
    // Certificate file extensions: *.pem, *.der, *.cer, *.crt
   public void Read_PEM_DER_Cert(String cert_file) throws FileNotFoundException, IOException, CertificateException
   {
       try 
       {    
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate)cf.generateCertificate(new FileInputStream(cert_file));
        System.out.println("NotBefore: " +cert.getNotBefore());
        System.out.println("NotAfter: " +cert.getNotAfter());
        System.out.println("SigAlgName: " +cert.getSigAlgName());
        System.out.println("Type: " +cert.getType());
        System.out.println("SerialNumber: " +cert.getSerialNumber());
        System.out.println("SubjectDN: " +cert.getSubjectDN());
        System.out.println("Version: " +cert.getVersion());
        
     
         }
       catch( CertificateException e)
       {
       System.out.println(e.toString());
       }
 }      
      
   // Certificate file extensions: *.pfx, *.p12, *.cer, *.crt
 public void Read_PFX_Cert(String cert_file, String PFX_pass) {

        try {
            
            KeyManagerFactory kmf = javax.net.ssl.KeyManagerFactory.getInstance("SunX509");
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            char[] password = PFX_pass.toCharArray();

            keystore.load(new FileInputStream(cert_file),password);
            //keystore.load(new FileInputStream(certificate), password);
            kmf.init(keystore, password);
            Enumeration<String> aliases = keystore.aliases();
            while(aliases.hasMoreElements()){
                String alias = aliases.nextElement();
                if(keystore.getCertificate(alias).getType().equals("X.509")){
                Date expDate = ((X509Certificate) keystore.getCertificate(alias)).getNotAfter();
                Date fromDate= ((X509Certificate) keystore.getCertificate(alias)).getNotBefore();
        
                System.out.println("Created Date: " + fromDate);
                System.out.println("Expiray Date: " + expDate );
                }
            }
        } 
        catch (IOException | KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | CertificateException e) {
        System.out.println(e.toString());
        }

    }
   
 // Certificate file extensions: *.p7b, *.p7c, *.cer, *.crt
public void Read_PKCS7_Cert(String cert_file) throws FileNotFoundException, CertificateException
 {       
 try {
    File file = new File(cert_file);
    FileInputStream fis = new FileInputStream(file);
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    Collection c = cf.generateCertificates(fis);
    Iterator i = c.iterator();
    while (i.hasNext()) {
      X509Certificate cert509 = (X509Certificate) i.next();
      System.out.println("IssuerDN: " + cert509.getIssuerDN());
      System.out.println("NotAfter: " + cert509.getNotAfter());
      System.out.println("SerialNumber: " + cert509.getSerialNumber());
      System.out.println("SigAlgName: " + cert509.getSigAlgName());
      System.out.println("IssuerUniqueID: " + Arrays.toString(cert509.getIssuerUniqueID()));
      System.out.println("Signature: " + Arrays.toString(cert509.getSignature()));
      System.out.println("SubjectDN: " + cert509.getSubjectDN());
    }
  }
  catch (FileNotFoundException | CertificateException th) {
      System.out.println(th.toString());
  }
 }


public void Read_JKS(String file_name, String keypass)
{
try {
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(new FileInputStream(file_name), keypass.toCharArray());
        Enumeration<String> aliases = keystore.aliases();
        while(aliases.hasMoreElements()){
            String alias = aliases.nextElement();
            if(keystore.getCertificate(alias).getType().equals("X.509")){
                System.out.println(alias + " expires " + ((X509Certificate) keystore.getCertificate(alias)).getNotAfter());
                System.out.println("signalg: " + ((X509Certificate) keystore.getCertificate(alias)).getSigAlgName());
                /*
                .....
                */

            }
        }
    } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
        System.out.println(e.toString());
    }
}
}

