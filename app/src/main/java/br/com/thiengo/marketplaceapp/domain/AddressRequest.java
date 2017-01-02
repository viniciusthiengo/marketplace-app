package br.com.thiengo.marketplaceapp.domain;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.thiengo.marketplaceapp.SignUpActivity;

/**
 * Created by viniciusthiengo on 02/01/17.
 */

public class AddressRequest extends AsyncTask<Void, Void, Address> {
    private WeakReference<SignUpActivity> activity;

    public AddressRequest( SignUpActivity activity ){
        this.activity = new WeakReference<>( activity );
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.get().lockFields( true );
    }

    @Override
    protected Address doInBackground(Void... voids) {

        try{
            URL url = new URL( "https://viacep.com.br/ws/"+activity.get().getZipCode()+"/json/" );
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));

            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                jsonString.append(line);
            }

            urlConnection.disconnect();

            Gson gson = new Gson();
            Address address = gson.fromJson(jsonString.toString(), Address.class);

            return address;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Address address) {
        super.onPostExecute(address);

        if( activity.get() != null ){
            activity.get().lockFields( false );

            if( address != null ){
                activity.get().setAddressFields(address);
            }
        }
    }
}
