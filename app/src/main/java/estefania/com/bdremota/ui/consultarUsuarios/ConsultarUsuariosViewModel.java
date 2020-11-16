package estefania.com.bdremota.ui.consultarUsuarios;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConsultarUsuariosViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ConsultarUsuariosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is tools fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}