package br.com.thiengo.marketplaceapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import br.com.thiengo.marketplaceapp.domain.Address;
import br.com.thiengo.marketplaceapp.domain.ZipCodeListener;

public class SignUpActivity extends AppCompatActivity {
    private EditText etZipCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        etZipCode = (EditText) findViewById(R.id.et_zip_code);
        etZipCode.addTextChangedListener( new ZipCodeListener(this) );

        Spinner spStates = (Spinner) findViewById(R.id.sp_state);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_spinner_item);
        spStates.setAdapter(adapter);
    }

    public String getZipCode(){
        return etZipCode.getText().toString();
    }


    public void setAddressFields( Address address){
        setField( R.id.et_street, address.getLogradouro() );
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
        setLockField( R.id.et_street, isToLock );
        setLockField( R.id.et_neighbor, isToLock );
        setLockField( R.id.et_city, isToLock );
        setLockField( R.id.sp_state, isToLock );
    }

    private void setLockField( int fieldId, boolean isToLock ){
        findViewById( fieldId ).setEnabled( !isToLock );
    }
}
