package aloeio.buzapp.app.Utils;

import aloeio.buzapp.app.Fragments.ReportFragment;
import aloeio.buzapp.app.MainActivity;
import aloeio.buzapp.app.Services.SpeakingService;
import aloeio.buzapp.app.Utils.UI.MyDialog;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by eduardo on 3/15/15.
 */
public class Utils implements ReportFragment.OnFragmentInteractionListener{
    private String message = null;
    private ConnectivityManager cm = null;
    private NetworkInfo netInfo = null;
    private MyDialog dialog = null;
    private boolean onBackgroundFlag = true;
    private ArrayList<String> muralMessage;
    private SpeakingService speakingService = null;

    public Utils(){
        this.dialog = new MyDialog();
    }

    public MyDialog getDialog(Activity activity){
        if(this.dialog == null)
            this.dialog = new MyDialog(activity);
        else
            this.dialog.setContext(activity);
        return this.dialog;
    }

    public void hideSoftKeyboard(final Activity activity) {
        if(activity.getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void createWarningToast(String type, final Activity activity) {
//        if(this.speakingService == null)
//            this.speakingService = new SpeakingService(activity);
//
//        if(type.toLowerCase().equals("no connection")) {
//            message = "Conexão com a internet perdida.\nCheque sua conexão.";
//            this.speakingService.speakWords(message);
//        }
//        else if(type.toLowerCase().equals("reconnected")) {
//            message = "Conexão reestabelecida.\nContinuando atividades.";
//            this.speakingService.speakWords(message);
//        }
//        else if(type.toLowerCase().equals("bad url"))
//            message = "Falha na comunicacao com o servidor.\nTente novamente mais tarde.";
//        else if(type.toLowerCase().equals("exception"))
//            message = "Oooops... Algo ruim aconteceu.\nContacte os desenvolvedores, por favor.";
//        else if(type.toLowerCase().equals("logout"))
//            message = "Sua conta foi desconectada.";
//        else if(type.toLowerCase().equals("bad backend"))
//            message = "Tivemos um problema com nossos servidores.\nTente novamente mais tarde.";
//        else if(type.toLowerCase().equals("no route"))
//            message = "Rota não encontrada.\nBaixando do servidor.";
//
//        if(onBackgroundFlag && !this.message.equals(""))
//            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public void verifyConnection(final Activity activity) {
        if (!this.checkConnection(activity))
            buildAlertMessageNoConnection(activity);
    }

    public void verifyGPS(final Activity activity) {
        if (!this.checkGPS(activity))
            buildAlertMessageNoGps(activity);
    }

    public boolean checkConnection(final Activity activity) {
        cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public boolean checkGPS(final Activity activity) {
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void setOnBackgroundFlag(boolean bool){
        onBackgroundFlag = bool;
    }

    public void copyMapFile(String inputPath, String inputFile, String outputPath, Activity activity) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


//            in = new FileInputStream(inputPath + inputFile);
            in =  activity.getAssets().open(inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        }  catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

    public void moveMapFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputPath + inputFile).delete();


        }

        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

    public void buildAlertMessageBackPressed(final Activity activity) {
        dialog = dialog.setContext(activity);
//    public void buildAlertMessageBackPressed() {
        dialog.setTitle("Sair do BuzApp");
        dialog.setMessage("Quer mesmo sair?");
        dialog.setPositiveButton("Sair", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
//                callMainActivity(activity);
            }
        }).setNegativeButton("Cancelar", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.close();
            }
        }).show();
    }

    public void buildAlertLogoff(final Activity activity) {
        dialog = dialog.setContext(activity);
        dialog.setTitle("Alerta");
        dialog.setMessage("Você foi desconectado.");
        dialog.setPositiveButton("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.close();
                callMainActivity(activity);
            }
        }).show();
    }

    public void buildAlertMessageNoConnection(final Activity activity) {
        dialog = dialog.setContext(activity);
        dialog.setTitle("Internet não detectada");
        dialog.setMessage("Para usar o aplicativo, conecte-se à internet: ");
        dialog.setPositiveButton("3/4G", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setComponent(new ComponentName(
                        "com.android.settings",
                        "com.android.settings.Settings$DataUsageSummaryActivity"));
                activity.startActivity(intent);
                dialog.close();
                System.exit(1);
            }
        }).setNeutralButton("Wifi", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                activity.startActivity(intent);
                dialog.close();
                System.exit(1);
            }
        }).setNegativeButton("Cancelar", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.close();
                System.exit(1);
            }
        }).show();
    }

    public void buildAlertMessageNoGps(final Activity activity) {
        dialog = dialog.setContext(activity);
        dialog.setTitle("GPS não detectado");
        dialog.setMessage("Seu GPS está desligado, por favor ative-o antes de prosseguir.");
        dialog.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                dialog.close();
            }
        }).setNegativeButton("Cancelar", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.close();
            }
        }).show();
    }

    public void buildAlertSendReportWarning(final Activity activity){
        dialog = dialog.setContext(activity);
        dialog.setTitle("Antes de continuar...");
        dialog.setMessage("O seu email podera ser anexado a mensagem para que possamos contacta-lo.\nSe voce deseja prosseguir, clique em Continuar.");
        dialog.setPositiveButton("Continuar", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                activity.startActivity(new Intent(activity, ReportActivity.class));
                dialog.close();
            }
        }).setNegativeButton("Cancelar", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.close();
            }
        }).show();
    }

//    public void buildAlertSendReportSuccess(final Activity activity){
//        dialog = dialog.setContext(activity);
//        dialog.setTitle("Aviso");
//        dialog.setMessage("Informação enviada com sucesso.");
//        dialog.setPositiveButton("Ok", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.close();
//                activity.onBackPressed();
//            }
//        }).show();
//    }
//
//    public void buildAlertSendReportFailure(final Activity activity){
//        dialog = dialog.setContext(activity);
//        dialog.setTitle("Aviso");
//        dialog.setMessage("Informação não foi enviada. Verifique os campos");
//        dialog.setPositiveButton("Ok", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.close();
//            }
//        }).show();
//    }

    public void buildAlertLoginFail(final Activity activity) {
        dialog = dialog.setContext(activity);
        dialog.setTitle("Falha no Login");
        dialog.setMessage("Tente novamente mais tarde ou acesse sem logar.");
        dialog.setPositiveButton("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.close();
            }
        }).show();
    }

    public void callMainActivity(final Activity activity){
        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
    }

    public void closeAlert(){
        dialog.close();
    }

    private void deleteFile(String inputPath, String inputFile) {
        try {
            // delete the original file
            new File(inputPath + inputFile).delete();


        }
//        catch (FileNotFoundException fnfe1) {
//            Log.e("tag", fnfe1.getMessage());
//        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    public void buildMural(final Activity activity, Boolean containsUrl) {
        dialog = dialog.setContext(activity);
        dialog.setTitle("Mural Buzapp");
        if(containsUrl == true){
            dialog.setMessageWithLink(loadMuralMessage().get(0),loadMuralMessage().get(1), loadMuralMessage().get(2), loadMuralMessage().get(3));
        }else {
            dialog.setMessage(loadMuralMessage().get(0));
        }
        dialog.setPositiveButton("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.close();
            }
        }).show();
    }

    private ArrayList<String> loadMuralMessage(){
       // gets message from server here by Buzapp mural priority
        muralMessage = new ArrayList<String>();
        muralMessage.add("Olá, este é o mural do Buzapp, onde você receberá mensagens importantes da equipe do Buzapp e de nossos ");
        muralMessage.add("http://www.aloeio.com/partners");
        muralMessage.add("parceiros.");
        muralMessage.add("\nBem vindo a uma nova experiência ao andar de ônibus!");
        return muralMessage;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
