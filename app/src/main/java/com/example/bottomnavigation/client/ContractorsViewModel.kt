import androidx.lifecycle.ViewModel
import com.example.bottomnavigation.models.ContractorID

class ContractorsViewModel : ViewModel() {
    var contractorList: ArrayList<ContractorID> = arrayListOf()
}
