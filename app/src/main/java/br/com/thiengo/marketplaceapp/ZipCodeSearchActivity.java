package br.com.thiengo.marketplaceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import br.com.thiengo.marketplaceapp.domain.Address;
import br.com.thiengo.marketplaceapp.domain.AddressAdapter;
import br.com.thiengo.marketplaceapp.domain.Util;
import br.com.thiengo.marketplaceapp.domain.ZipCodeRequest;

public class ZipCodeSearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Spinner spStates;
    private ListView lvAddress;
    private List<Address> addresses;
    private Util util;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip_code_search);

        addresses = new ArrayList<>();
        lvAddress = (ListView) findViewById(R.id.lv_address);
        AddressAdapter adapter = new AddressAdapter(this, addresses);
        lvAddress.setAdapter( adapter );
        lvAddress.setOnItemClickListener( this );

        spStates = (Spinner) findViewById(R.id.sp_state);
        spStates.setAdapter( ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_spinner_item) );

        util = new Util(this,
                R.id.et_street,
                R.id.et_city,
                R.id.sp_state);
    }

    public void lockFields( boolean isToLock ){
        util.lockFields( isToLock );
    }

    private String getState(){
        String[] stateArray = ((String) spStates.getSelectedItem()).split("\\(");

        if( stateArray.length == 2 ){
            stateArray = stateArray[1].split("\\)");
            return stateArray[0];
        }
        return "";
    }

    private String getCity(){
        return ((EditText) findViewById(R.id.et_city)).getText().toString();
    }

    private String getStreet(){
        return ((EditText) findViewById(R.id.et_street)).getText().toString();
    }

    public String getUriZipCode(){
        String uri = "https://viacep.com.br/ws/";
        uri += getState()+"/";
        uri += getCity()+"/";
        uri += getStreet()+"/json/";
        return uri;
    }

    public List<Address> getAddresses(){
        return addresses;
    }

    public void updateListView(){
        ((AddressAdapter) lvAddress.getAdapter()).notifyDataSetChanged();
    }

    public void searchAddress( View view ){
        new ZipCodeRequest( this ).execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String[] zipCodeArray = addresses.get( i ).getCep().split("-");
        String zipCode = zipCodeArray[0]+zipCodeArray[1];

        Intent intent = new Intent();
        intent.putExtra( Address.ZIP_CODE_KEY, zipCode );
        setResult( RESULT_OK, intent );
        finish();
    }
}