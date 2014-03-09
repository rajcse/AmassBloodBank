package sshopbd.com;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class UserRegistrationActivity extends Activity {
	private EditText et1 , et2 , et3 , et4 ;
	private Spinner sp1 , sp2 ;
	private String className ;
	
	private void commonInit() {
		ThreadPolicyHandler.doSetThreadPolicy() ;
		getActionBar().setTitle( ProjectConstants.getProjectName() ) ;
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_registration);
		this.className = "UserRegistrationActivity" ;
		this.commonInit() ;
		this.init() ;
	}
	
	private void init() {
		this.et1 = ( EditText ) findViewById( R.id.editText1 ) ;
		this.et2 = ( EditText ) findViewById( R.id.editText2 ) ;
		this.et3 = ( EditText ) findViewById( R.id.editText3 ) ;
		this.et4 = ( EditText ) findViewById( R.id.editText4 ) ;
		this.sp1 = ( Spinner ) findViewById( R.id.spinner1 ) ;
		this.sp2 = ( Spinner ) findViewById( R.id.spinner2 ) ;
		
		DropdownDataHandler dddhObj = new DropdownDataHandler() ;
		dddhObj.setRefernece( this ) ;
		dddhObj.setSpinnerDistrictData( this.sp1 ) ;
		
		dddhObj = new DropdownDataHandler() ;
		dddhObj.setRefernece( this ) ;
		dddhObj.setSpinnerBloodGroupData( this.sp2 ) ;
		
	}

	public boolean onCreateOptionsMenu( Menu menu ) {
		getMenuInflater().inflate( R.menu.user_registration, menu ) ;
		return true;
	}
	
	private boolean checkEmail( String data ) {
		int i , len , j , cn , k ;
		boolean fl ;
		len = data.length() ;
		fl = false ;
		j = -1 ;
		k = -1 ;
		for( i = 0 ; i < len ; i++ ) {
			if( data.charAt( i ) == '@' ) {
				if( i != 0 ) {
					fl = true ;
					j = i ;
					break ;
				}
			}
		}
		if( fl == false ) {
			return fl ;
		}
		cn = 0 ;
		for( i = 0 ; i < len ; i++ ) {
			if( data.charAt( i ) == '.' ) {
				cn++ ;
				k = i ;
			}
		}
		if( cn == 0 ) {
			return false ;
		}
		return true ;
	}
	
	private boolean checkPhoneNumber( String numberParam ) {
		int i , len ;
		len = numberParam.length() ;
		for( i = 0 ; i < len ; i++ ) {
			if( numberParam.charAt( i ) >= '0' && numberParam.charAt( i ) <= '9' ) {
			}
			else {
				return false ;
			}
		}
		if( len >= 7 && len <= 11 ) {
			return true ;
		}
		return false ;
	}
	public void doRegister( View v ) {
		EncryptionMethods emObj ;
		String response , urlParameters ;
		String[] arr ;
		Intent iObj ;
		urlParameters = new String( "" ) ;
		emObj = new EncryptionMethods() ;
		arr = new String[ 100 ] ;
		int i , cn ;
		boolean fl ;
		cn = 0 ;
		response = null ;
		arr[ cn++ ] = this.et1.getText().toString() ;
		arr[ cn++ ] = this.sp2.getSelectedItem().toString() ;
		arr[ cn++ ] = this.sp1.getSelectedItem().toString() ;
		arr[ cn++ ] = "Bangladesh" ;
		arr[ cn++ ] = this.et3.getText().toString() ;
		arr[ cn++ ] = this.et2.getText().toString() ;
		arr[ cn++ ] = emObj.md5Encryption( this.et4.getText().toString() ) ;
		arr[ cn++ ] = "0" ;
		fl = true ;
		for( i = 0 ; i < cn ; i++ ) {
			if( arr[ i ] == null || arr[ i ].compareTo( "" ) == 0 ) {
				AlertDialogHandler.showDialog( this , "Error!" , "You need to fill up all the fields to register!" ) ;
				fl = false ;
				break ;
			}
		}
		if( this.checkEmail( arr[ 5 ] ) == false ) {
			AlertDialogHandler.showDialog( this , "Error!" , "Invalid Email Address!" ) ;
			fl = false ;
		}
		else if( this.checkPhoneNumber( arr[ 4 ] ) == false ) {
			AlertDialogHandler.showDialog( this , "Error!" , "Invalid Phone Number!" ) ;
			fl = false ;
		}
		if( fl == true ) {
			for( i = 0 ; i < cn ; i++ ) {
				arr[ i ] = emObj.base64Encode( arr[ i ] ) ;
			}
			SendApiRequest sarObj = new SendApiRequest() ;
			sarObj.setActivity( this ) ;
			urlParameters = "1=" + arr[ 0 ] + "&2=" + arr[ 1 ] + "&3=" + arr[ 2 ] + "&4=" + arr[ 3 ] + "&5=" + arr[ 4 ] + "&6=" + arr[ 5 ] + "&7=" + arr[ 6 ] + "&8=" + arr[ 7 ] ;
			response = sarObj.sendRequest( this , urlParameters , "addNewUser.php" , "Error occurred in " + this.className + " class." , "Registration Module!" ) ;
			if( response == null ) {
			}
			else if( response.charAt( 0 ) == 'o' && response.charAt( 1 ) == 'k' ) {
				iObj = new Intent( getBaseContext() , MainActivity.class ) ;
				startActivity( iObj ) ;
				AlertDialogHandler.showDialog( this , "Registration Module!" , "Added the user successfully!" ) ;
			}
			else {
				AlertDialogHandler.showDialog( this , "Registration Module!" , "Error occured on the server side!" ) ;
			}
		}
	}
}

