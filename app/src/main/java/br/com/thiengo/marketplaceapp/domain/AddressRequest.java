package br.com.thiengo.marketplaceapp.domain;

import android.os.AsyncTask;
import android.os.SystemClock;

import com.google.gson.Gson;

import java.lang.ref.WeakReference;

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
            String jsonString = JsonRequest.request( activity.get().getUriRequest() );
            Gson gson = new Gson();
            return gson.fromJson(jsonString, Address.class);
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