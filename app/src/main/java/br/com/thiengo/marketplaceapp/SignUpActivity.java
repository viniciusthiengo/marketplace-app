package br.com.thiengo.marketplaceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import br.com.thiengo.marketplaceapp.domain.Address;
import br.com.thiengo.marketplaceapp.domain.Util;
import br.com.thiengo.marketplaceapp.domain.ZipCodeListener;

public class SignUpActivity extends AppCompatActivity {
    private EditText etZipCode;
    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        etZipCode = (EditText) findViewById(R.id.et_zip_code);
        etZipCode.addTextChangedListener( new ZipCodeListener(this) );

        Spinner spStates = (Spinner) findViewById(R.id.sp_state);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this,
                        R.array.states,
                        android.R.layout.simple_spinner_item);
        spStates.setAdapter(adapter);

        util = new Util(this,
                R.id.et_street,
                R.id.tv_zip_code_search,
                R.id.et_complement,
                R.id.et_neighbor,
                R.id.et_city,
                R.id.sp_state);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == Address.RESQUEST_ZIP_CODE_CODE
                && resultCode == RESULT_OK ){
            etZipCode.setText( data.getStringExtra( Address.ZIP_CODE_KEY ) );
        }
    }

    private String getZipCode(){
        return etZipCode.getText().toString();
    }

    public String getUriRequest(){
        return "https://viacep.com.br/ws/"+getZipCode()+"/json/";
    }


    public void setAddressFields( Address address){
        setField( R.id.et_street, address.getLogradouro() );
        setField( R.id.et_complement, address.getComplemento() );
        setField( R.id.et_neighbor, address.getBairro() );
        setField( R.id.et_city, address.getLocalidade() );
        setSpinner( R.id.sp_state, R.array.states, address.getUf() );
    }

    private void setField( int fieldId, String data ){
        ((EditText) findViewById( fieldId )).setText( data );
    }

    private void setSpinner( int fieldId, int arrayId, String uf ){
        Spinner spinner = (Spinner) findViewById( fieldId );
        String[] states = getResources().getStringArray(arrayId);

        for( int i = 0; i < states.length; i++ ){
            if( states[i].endsWith("("+uf+")") ){
                spinner.setSelection( i );
                break;
            }
        }
    }


    public void lockFields( boolean isToLock ){
        util.lockFields( isToLock );
    }


    public void searchZipCode(View view){
        Intent intent = new Intent(this, ZipCodeSearchActivity.class);
        startActivityForResult( intent, Address.RESQUEST_ZIP_CODE_CODE );
    }
}
