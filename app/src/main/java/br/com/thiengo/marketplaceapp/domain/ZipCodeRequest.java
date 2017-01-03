package br.com.thiengo.marketplaceapp.domain;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import br.com.thiengo.marketplaceapp.ZipCodeSearchActivity;

/**
 * Created by viniciusthiengo on 02/01/17.
 */

public class ZipCodeRequest extends AsyncTask<Void, Void, Void> {
    private WeakReference<ZipCodeSearchActivity> activity;

    public ZipCodeRequest( ZipCodeSearchActivity activity ){
        this.activity = new WeakReference<>( activity );
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.get().lockFields( true );
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try{
            String jsonString = JsonRequest.request( activity.get().getUriRequest() );

            JSONArray jsonArray = new JSONArray(jsonString);
            Gson gson = new Gson();
            activity.get().getAddresses().clear(); /* PARA REAPROVEITAR O OBJETO DE LISTA */

            for( int i = 0; i < jsonArray.length(); i++ ){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                activity.get().getAddresses().add( gson.fromJson(jsonObject.toString(), Address.class) );
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void data) {
        super.onPostExecute(data);

        if( activity.get() != null ){
            activity.get().lockFields( false );
            activity.get().updateListView();
        }
    }
}
